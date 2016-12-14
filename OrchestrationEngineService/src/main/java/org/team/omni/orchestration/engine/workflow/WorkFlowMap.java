package org.team.omni.orchestration.engine.workflow;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectOffsetStep;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.WeatherDetailsConverter;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.services.ServiceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WorkFlowMap {
	private Map<String, List<OrchestrationEngineWorkFlow<WeatherDetails>>> workFlowDetails;
	private DSLContext create;

	private final Field<String> userIdField = field("user_id", String.class);
	private final Field<Integer> workFlowIdField = field("id", Integer.class);
	private final Field<WorkFlowExecutionStatus> statusField = field("status", OrchestrationEngineUtils.createWorkFlowExecutionStatusDataType());
	private final Field<String> currentServiceField = field("current_service", String.class);
	private final Table<Record> workflowDetailsTable = table("work_flow_details");
	private final Field<WeatherDetails> resultField = field("result", SQLDataType.VARCHAR.asConvertedDataType(new WeatherDetailsConverter(new ObjectMapper())));
	private final Field<String> previousServiceField = field("previous_service", String.class);
	private final Field<String> stationNameField = field("station_name", String.class);
	private final Field<LocalDateTime> inputTimeStampField = field("input_time_stamp", LocalDateTime.class);
	private final Field<String> errorMessageField = field("error_message", String.class);
	private final Field<String> detailedErrorMessageField = field("detailed_error_message", String.class);
	private final Field<LocalDateTime> executionTimeStampField = field("execution_time_stamp", SQLDataType.LOCALDATETIME);
	private static WorkFlowMap WORK_FLOW_MAP = null;

	protected WorkFlowMap(DSLContext create) {
		workFlowDetails = new ConcurrentHashMap<>();
		this.create = create;
	}

	public WorkFlowExecutionStatus createWorkFlow(String userId, String stationName, LocalDateTime timeStamp) {
		List<OrchestrationEngineWorkFlow<WeatherDetails>> workFlowQueue;
		if (workFlowDetails.containsKey(userId)) {
			workFlowQueue = workFlowDetails.get(userId);
		} else {
			workFlowQueue = Collections.synchronizedList(new LinkedList<>());
			workFlowDetails.put(userId, workFlowQueue);
		}
		LocalDateTime executionTimeStamp = LocalDateTime.now();
		int id = createId(userId, stationName, timeStamp, WorkFlowExecutionStatus.EXECUTION_STARTED, executionTimeStamp);
		OrchestrationEngineWorkFlow<WeatherDetails> workFlow = new SimpleWorkFlow(id, ServiceFactory.getServiceFactory(), stationName, timeStamp, new WorkFlowState(create, userId, id), create, executionTimeStamp);
		workFlowQueue.add(workFlow);
		workFlow.executeWorkFlow();
		return workFlow.getWorkFlowState().getExecutionStatus();
	}

	private int createId(String userId, String stationName, LocalDateTime timeStamp, WorkFlowExecutionStatus executionStatus, LocalDateTime executionTimeStamp) {
		Optional<Record> record = create.insertInto(workflowDetailsTable, userIdField, statusField, currentServiceField, resultField, previousServiceField, stationNameField, inputTimeStampField, errorMessageField, detailedErrorMessageField, executionTimeStampField).values(userId, executionStatus, "", null, "", stationName, timeStamp, null, null, executionTimeStamp).returning(workFlowIdField).fetchOptional();
		if (record.isPresent()) {
			return record.get().getValue(workFlowIdField);
		} else {
			throw new OrchestrationEngineException("Work Flow Details could not be saved");
		}
	}

	public OrchestrationEngineWorkFlow<WeatherDetails> fetchLatestWorkFlow(String userID) {
		return fetchWorkFlow(userID, null);
	}

	private OrchestrationEngineWorkFlow<WeatherDetails> createAndAddWorkFlow(Record result) {
		if (result == null) {
			return null;
		} else {
			String userId;
			OrchestrationEngineWorkFlow<WeatherDetails> workFlow = createOrchestrationEngineWorkFlow(result);
			userId = workFlow.getWorkFlowState().getUserId();
			if (workFlowDetails.containsKey(userId)) {
				List<OrchestrationEngineWorkFlow<WeatherDetails>> workFlows = workFlowDetails.get(userId);
				if (!workFlows.isEmpty()) {
					int workFlowSize = workFlows.size();
					for (int i = 0; i < workFlowSize; i++) {
						OrchestrationEngineWorkFlow<WeatherDetails> tempWorkFlow = workFlows.get(i);
						if (tempWorkFlow.getWorkFlowState().getWorkFlowId() >= workFlow.getWorkFlowState().getWorkFlowId()) {
							workFlows.add(i, workFlow);
							break;
						}
					}
					if (workFlowSize == workFlows.size()) {
						workFlows.add(workFlow);
					}
				} else {
					workFlows.add(workFlow);
				}
			} else {
				List<OrchestrationEngineWorkFlow<WeatherDetails>> workFlows = new ArrayList<>();
				workFlows.add(workFlow);
				this.workFlowDetails.put(userId, workFlows);
			}
			return workFlow;
		}
	}

	private OrchestrationEngineWorkFlow<WeatherDetails> createOrchestrationEngineWorkFlow(Record result) {
		int id = result.getValue(workFlowIdField);
		String userId = result.getValue(userIdField);
		WorkFlowExecutionStatus workFlowExecutionStatus = result.get(statusField, WorkFlowExecutionStatus.class);
		WorkFlowState workFlowState = new WorkFlowState(create, userId, id, result.getValue(currentServiceField), result.getValue(previousServiceField), result.getValue(errorMessageField), result.getValue(detailedErrorMessageField), workFlowExecutionStatus);
		LocalDateTime inputTimeStamp = result.getValue(inputTimeStampField, LocalDateTime.class);
		LocalDateTime executionTimeStamp = result.getValue(executionTimeStampField, LocalDateTime.class);
		OrchestrationEngineWorkFlow<WeatherDetails> workFlow = new SimpleWorkFlow(id, ServiceFactory.getServiceFactory(), result.getValue(stationNameField), inputTimeStamp, workFlowState, create, executionTimeStamp);
		return workFlow;
	}

	public OrchestrationEngineWorkFlow<WeatherDetails> fetchWorkFlow(String userID, Integer workFlowId) {
		Condition condition = userIdField.equal(userID);
		if (workFlowId != null) {
			condition = condition.and(workFlowIdField.equal(workFlowId));
		}
		SelectOffsetStep<Record> selectSeekStep1 = create.select().from(workflowDetailsTable).where(condition).orderBy(field("ID").desc()).limit(1);
		if (!workFlowDetails.containsKey(userID) && createAndAddWorkFlow(selectSeekStep1.fetchOne()) == null) {
			throw new OrchestrationEngineException("User Id could not be found: " + userID);
		}
		List<OrchestrationEngineWorkFlow<WeatherDetails>> workFlows = workFlowDetails.get(userID);
		if (workFlowId == null && !workFlows.isEmpty()) {
			return workFlows.get(workFlows.size() - 1);
		} else {
			for (OrchestrationEngineWorkFlow<WeatherDetails> workFlow : workFlows) {
				if (workFlow.getWorkFlowState().getWorkFlowId() == workFlowId) {
					return workFlow;
				}
			}
			throw new OrchestrationEngineException("No execution for the execution Id :" + workFlowId + " was found");
		}
	}

	public List<Integer> fetchWorkFlowIds(String userID) {
		return create.select(field("ID", Integer.class)).from(workflowDetailsTable).where(userIdField.equal(userID)).orderBy(field("ID").desc()).fetch().getValues(field("ID", Integer.class));
	}

	public List<OrchestrationEngineWorkFlow<WeatherDetails>> fetchWorkFlows(String userID) {
		Record[] records = create.select().from(workflowDetailsTable).where(userIdField.equal(userID)).orderBy(field("ID").desc()).fetchArray();
		return Arrays.stream(records).map((Record record) -> createAndAddWorkFlow(record)).collect(Collectors.toList());
	}

	public void removeWorkFlows(String userId) {
		workFlowDetails.remove(userId);
	}

	public static WorkFlowMap createDefaultWorkFlow(DSLContext create) {
		if (WORK_FLOW_MAP == null)
			WORK_FLOW_MAP = new WorkFlowMap(create);
		return WORK_FLOW_MAP;
	}

	public static WorkFlowMap getWorkFlowMap() {
		return WORK_FLOW_MAP;
	}

}
