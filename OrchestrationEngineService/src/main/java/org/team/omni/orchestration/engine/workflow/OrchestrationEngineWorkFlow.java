package org.team.omni.orchestration.engine.workflow;

public interface OrchestrationEngineWorkFlow<T> {

	public void executeWorkFlow();

	public WorkFlowState getWorkFlowState();

	public T fetchResult();

}
