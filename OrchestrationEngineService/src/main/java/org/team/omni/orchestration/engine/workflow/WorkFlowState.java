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
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO WORK_FLOW_DETAILS (USER_ID,STATUS) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, userId);
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
