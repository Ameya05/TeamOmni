package org.team.omni.orchestration.engine.workflow;

@FunctionalInterface
public interface ServiceExecution<T> {

	public T execute() throws Exception;

}
