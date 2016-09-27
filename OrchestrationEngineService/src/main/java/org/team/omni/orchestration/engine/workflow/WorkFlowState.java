package org.team.omni.orchestration.engine.workflow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.team.omni.exceptions.OrchestrationEngineException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkFlowState {
	private String currentService = "";
	private String previousService = "";
	private String errorMessage = "";
	private String detailedErrorMessage = "";
	private WorkFlowExecutionStatus executionStatus = WorkFlowExecutionStatus.EXECUTING;
	private long userId;
	private long workFlowId;
	private DataSource dataSource;

	public WorkFlowState(DataSource dataSource, long userId) {
		this.setDataSource(dataSource);
		this.setUserId(userId);
		createEntry();
	}

	private void createEntry() {
		try (Connection connection = dataSource.getConnection();) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO WORK_FLOW_DETAILS (USER_ID,STATUS) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, userId);
			preparedStatement.setString(2, executionStatus.getValue());
			int rowsUpadted = preparedStatement.executeUpdate();
			if (rowsUpadted == 0) {
				throw new OrchestrationEngineException("Work Flow Details could not be saved");
			}
			try (ResultSet resultSet = preparedStatement.getGeneratedKeys();) {
				if (resultSet.next()) {
					setWorkFlowId(resultSet.getLong(1));
				}
			}
		} catch (SQLException e) {
			throw new OrchestrationEngineException(e);
		}
	}

	public void setCurrentService(String service) {
		this.previousService = currentService;
		this.currentService = service;
	}

	public String getCurrentService() {
		return this.currentService;
	}

	public String getPreviousService() {
		return this.previousService;
	}

	public void setError(Exception e) {
		this.errorMessage = e.getMessage();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		this.detailedErrorMessage = stringWriter.toString();
		this.executionStatus = WorkFlowExecutionStatus.EXECUTION_FAILURE;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String getDetailedErrorMessage() {
		return this.detailedErrorMessage;
	}

	public WorkFlowExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	@JsonProperty
	public void setExecutionStatus(WorkFlowExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
		try (Connection connection = dataSource.getConnection();) {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE WORK_FLOW_DETAILS SET STATUS=? WHERE ID=?");
			preparedStatement.setString(1, executionStatus.getValue());
			preparedStatement.setLong(2, workFlowId);
			int updateRows = preparedStatement.executeUpdate();
			if (updateRows == 0) {
				throw new OrchestrationEngineException("Work Flow Details could not be updated");
			}
		} catch (SQLException exception) {
			throw new OrchestrationEngineException(exception);
		}
	}

	public long getUserId() {
		return userId;
	}

	@JsonProperty
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getWorkFlowId() {
		return workFlowId;
	}

	@JsonProperty
	public void setWorkFlowId(long workFlowId) {
		this.workFlowId = workFlowId;
	}

	@JsonIgnore
	public DataSource getDataSource() {
		return dataSource;
	}

	@JsonProperty
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
