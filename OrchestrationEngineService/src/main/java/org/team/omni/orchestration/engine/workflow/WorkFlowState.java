package org.team.omni.orchestration.engine.workflow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import static org.jooq.impl.DSL.*;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.SQLDataType;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.exceptions.OrchestrationEngineException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkFlowState {
	private final Field<WorkFlowExecutionStatus> statusField = field("STATUS", OrchestrationEngineUtils.createWorkFlowExecutionStatusDataType());
	private final Field<Object> currentServiceField = field("current_service");
	private final Field<String> previousServiceField = field("previous_service", String.class);
	private final Table<Record> workflowDetailsTable = table("work_flow_details");
	private final Field<String> errorMessageField = field("error_message", String.class);
	private final Field<String> detailedErrorMessageField = field("detailed_error_message", String.class);

	private String currentService = "";
	private String previousService = "";
	private String errorMessage = "";
	private String detailedErrorMessage = "";
	private WorkFlowExecutionStatus executionStatus = WorkFlowExecutionStatus.EXECUTING;
	private String userId;
	private int workFlowId;
	private DSLContext create;

	public WorkFlowState(DSLContext create, String userId, int workFlowId) {
		setCreate(create);
		setUserId(userId);
		setWorkFlowId(workFlowId);
	}

	public WorkFlowState(DSLContext create, String userId, int workFlowId, String currentService, String previousService, String errorMessage, String detailedErrorMessage, WorkFlowExecutionStatus workFlowExecutionStatus) {
		this(create, userId, workFlowId);
		this.setCurrentService(currentService);
		this.setPreviousService(previousService);
		this.setErrorMessage(errorMessage);
		this.setDetailedErrorMessage(detailedErrorMessage);
		this.setExecutionStatus(workFlowExecutionStatus);
	}

	public synchronized void processCurrentService(String service) {
		this.setPreviousService(getCurrentService());
		this.setCurrentService(service);
		int updateCount = create.update(workflowDetailsTable).set(currentServiceField, getCurrentService()).set(previousServiceField, getPreviousService()).where(field("ID").equal(getWorkFlowId())).execute();
		if (updateCount == 0) {
			throw new OrchestrationEngineException("Current Service Updation of Work_Flow_Details table failed");
		}
		log("Service execution started for: " + service);
	}

	public synchronized void processError(Exception e) {
		this.setErrorMessage(e.getMessage());
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		this.setDetailedErrorMessage(stringWriter.toString());
		setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_FAILURE, getErrorMessage(), getDetailedErrorMessage());
	}

	@JsonIgnore
	public synchronized void setExecutionStatus(WorkFlowExecutionStatus executionStatus, String errorMessage, String detailedErrorMessage) {
		this.executionStatus = executionStatus;
		UpdateSetMoreStep<Record> updateStep = create.update(workflowDetailsTable).set(statusField, executionStatus);
		if (errorMessage != null) {
			updateStep = updateStep.set(errorMessageField, errorMessage);
		}
		if (detailedErrorMessage != null) {
			updateStep = updateStep.set(detailedErrorMessageField, detailedErrorMessage);
		}
		int updateRows = updateStep.where(field("ID").equal(getWorkFlowId())).execute();
		if (updateRows == 0) {
			throw new OrchestrationEngineException("Work Flow Details could not be updated");
		}
		log("Execution Status Change : " + executionStatus.getValue());

	}

	public synchronized void processExecutionStatus(WorkFlowExecutionStatus executionStatus) {
		setExecutionStatus(executionStatus, null, null);
	}

	public synchronized void log(String log) {
		if (log != null) {
			int updateCount = create.insertInto(table("work_flow_history"), field("execution_id"), field("history"), field("time_stamp", SQLDataType.LOCALDATETIME)).values(getWorkFlowId(), log, LocalDateTime.now()).execute();
			if (updateCount == 0) {
				throw new OrchestrationEngineException("Work Flow loging failed");
			}
		}
	}

	public synchronized List<String> fetchCompleteHistory() {
		return create.select(field("history")).from(table("work_flow_history")).where(field("execution_id").equal(getWorkFlowId())).fetch(field("history", String.class));
	}

	@JsonIgnore
	public synchronized void setCreate(DSLContext create) {
		this.create = create;
	}

	@JsonIgnore
	public synchronized DSLContext getCreate() {
		return create;
	}

	@JsonProperty
	public synchronized void setUserId(String userId) {
		this.userId = userId;
	}

	public synchronized String getUserId() {
		return userId;
	}

	@JsonProperty
	public synchronized void setWorkFlowId(int workFlowId) {
		this.workFlowId = workFlowId;
	}

	public synchronized long getWorkFlowId() {
		return workFlowId;
	}

	@JsonProperty
	public synchronized void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public synchronized String getErrorMessage() {
		return this.errorMessage;
	}

	@JsonProperty
	public synchronized void setDetailedErrorMessage(String detailedErrorMessage) {
		this.detailedErrorMessage = detailedErrorMessage;
	}

	public synchronized String getDetailedErrorMessage() {
		return this.detailedErrorMessage;
	}

	@JsonProperty
	public synchronized void setExecutionStatus(WorkFlowExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
	}

	public synchronized WorkFlowExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	@JsonProperty
	public synchronized void setCurrentService(String currentService) {
		this.currentService = currentService;
	}

	public synchronized String getCurrentService() {
		return this.currentService;
	}

	@JsonProperty
	public synchronized void setPreviousService(String previousService) {
		this.previousService = previousService;
	}

	public synchronized String getPreviousService() {
		return this.previousService;
	}

}
