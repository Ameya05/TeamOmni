package org.team.omni.orchestration.engine.workflow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import static org.jooq.impl.DSL.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.team.omni.exceptions.OrchestrationEngineException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkFlowState {
	private String currentService = "";
	private String previousService = "";
	private String errorMessage = "";
	private String detailedErrorMessage = "";
	private WorkFlowExecutionStatus executionStatus = WorkFlowExecutionStatus.EXECUTING;
	private String userId;
	private long workFlowId;
	private DataSource dataSource;

	public WorkFlowState(DataSource dataSource, String userId) {
		this.setDataSource(dataSource);
		this.setUserId(userId);
		createEntry();
	}

	private void createEntry() {
		try (Connection connection = dataSource.getConnection();) {
			DSLContext create = DSL.using(connection);
			Optional<Record> record = create.insertInto(table(name("WORK_FLOW_DETAILS")), field("USER_ID"), field("STATUS")).values(userId, executionStatus.getValue()).returning(field("ID")).fetchOptional();
			if (record.isPresent()) {
				setWorkFlowId(record.get().getValue(field("ID", Long.class)));
			} else {
				throw new OrchestrationEngineException("Work Flow Details could not be saved");
			}
		} catch (SQLException e) {
			throw new OrchestrationEngineException(e);
		}
	}

	public synchronized void setCurrentService(String service) {
		this.previousService = currentService;
		this.currentService = service;
	}

	public synchronized String getCurrentService() {
		return this.currentService;
	}

	public synchronized String getPreviousService() {
		return this.previousService;
	}

	public synchronized void setError(Exception e) {
		this.errorMessage = e.getMessage();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		this.detailedErrorMessage = stringWriter.toString();
		this.executionStatus = WorkFlowExecutionStatus.EXECUTION_FAILURE;
	}

	public synchronized String getErrorMessage() {
		return this.errorMessage;
	}

	public synchronized String getDetailedErrorMessage() {
		return this.detailedErrorMessage;
	}

	public synchronized WorkFlowExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	@JsonProperty
	public synchronized void setExecutionStatus(WorkFlowExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
		try (Connection connection = dataSource.getConnection();) {
			DSLContext create = DSL.using(connection);
			int updateRows = create.update(table("WORK_FLOW_DETAILS")).set(field("STATUS"), executionStatus.toString()).where(field("ID").equal(workFlowId)).execute();
			if (updateRows == 0) {
				throw new OrchestrationEngineException("Work Flow Details could not be updated");
			}
		} catch (SQLException exception) {
			throw new OrchestrationEngineException(exception);
		}
	}

	public synchronized String getUserId() {
		return userId;
	}

	@JsonProperty
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}

	public synchronized long getWorkFlowId() {
		return workFlowId;
	}

	@JsonProperty
	public synchronized void setWorkFlowId(long workFlowId) {
		this.workFlowId = workFlowId;
	}

	@JsonIgnore
	public synchronized DataSource getDataSource() {
		return dataSource;
	}

	@JsonProperty
	public synchronized void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
