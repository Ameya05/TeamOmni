package org.team.omni.orchestration.engine.workflow;

public enum WorkFlowExecutionStatus {
	EXECUTING("WorkFlow is Executing"), EXECUTION_COMPLETE("Work Flow Executon Compleed Successfully"), EXECUTION_FAILURE("Workflow Execution Failed"), EXECUTION_TERMINATED("Work Flow Execution has been Terminated");

	private final String value;

	private WorkFlowExecutionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
