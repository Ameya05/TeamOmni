package org.team.omni.orchestration.engine.workflow;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.services.ServiceFactory;

public class WorkFlowMap {
	private Map<String, Deque<OrchestrationEngineWorkFlow<WeatherDetails>>> workFlowDetails;

	private static final WorkFlowMap WORK_FLOW_MAP = new WorkFlowMap();

	protected WorkFlowMap() {
		workFlowDetails = new ConcurrentHashMap<>();
	}

	public WorkFlowExecutionStatus createWorkFlow(String userId, String stationName, LocalDateTime timeStamp) {
		Deque<OrchestrationEngineWorkFlow<WeatherDetails>> workFlowQueue;
		if (workFlowDetails.containsKey(userId)) {
			workFlowQueue = workFlowDetails.get(userId);
		} else {
			workFlowQueue = new ConcurrentLinkedDeque<>();
			workFlowDetails.put(userId, workFlowQueue);
		}
		OrchestrationEngineWorkFlow<WeatherDetails> workFlow = new SimpleWorkFlow(ServiceFactory.getServiceFactory(), stationName, timeStamp, WorkFlowStateFactory.getWorkFlowStateFactory().createWorkFlowState(userId));
		workFlowQueue.add(workFlow);
		workFlow.executeWorkFlow();
		return workFlow.getWorkFlowState().getExecutionStatus();
	}

	public WorkFlowState fetchWorkFlowState(String userId, String idToken) {
		if (!workFlowDetails.containsKey(userId)) {
			throw new OrchestrationEngineException("User Id could not be found: " + userId);
		}
		return workFlowDetails.get(userId).getLast().getWorkFlowState();

	}

	public static WorkFlowMap getWorkFlowMap() {
		return WORK_FLOW_MAP;
	}

}
