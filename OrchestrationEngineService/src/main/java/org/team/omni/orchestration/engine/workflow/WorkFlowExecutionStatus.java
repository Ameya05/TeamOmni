package org.team.omni.orchestration.engine.workflow;

public enum WorkFlowExecutionStatus {
	EXECUTION_STARTED("Execution has started"),EXECUTING("WorkFlow is Executing"), EXECUTION_COMPLETE("Work Flow Execution Completed Successfully"), EXECUTION_FAILURE("Workflow Execution Failed"), EXECUTION_TERMINATED("Work Flow Execution has been Terminated"), EXECUTION_NOT_REQUIRED("No Storm detected");

	private final String value;

	private WorkFlowExecutionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
