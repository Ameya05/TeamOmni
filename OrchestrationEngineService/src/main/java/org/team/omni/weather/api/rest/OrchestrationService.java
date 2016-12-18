package org.team.omni.weather.api.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.team.omni.DataHolder;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.orchestration.engine.workflow.OrchestrationEngineWorkFlow;
import org.team.omni.orchestration.engine.workflow.WorkFlowExecutionStatus;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;

/**
 * 
 * @author Eldho Mathulla
 *
 */
@Path("/{parameter: services|test}")
public class OrchestrationService {

	@Inject
	private WorkFlowMap workFlowMap;

	@GET
	@Path("/initiate")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String initiate(@QueryParam("date") String date, @QueryParam("time") String time, @QueryParam("station") String stationName, @QueryParam("uid") String id, @QueryParam("idtoken") String idtoken) {
		WorkFlowExecutionStatus executionStatus = workFlowMap.createWorkFlow(id, stationName, LocalDateTime.parse(date + time, DateTimeFormatter.ofPattern("MM/dd/yyyyHHmmss")));
		return executionStatus.getValue();
	}

	@GET
	@Path("/queryStatus/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DataHolder queryStatus(@PathParam("user_id") String userID) {
		OrchestrationEngineWorkFlow<WeatherDetails> orchestrationEngineWorkFlow = workFlowMap.fetchLatestWorkFlow(userID);
		return createDataHolder(orchestrationEngineWorkFlow);
	}

	@GET
	@Path("/queryStatus/{user_id}/{work_flow_id}")
	public DataHolder queryStatus(@PathParam("user_id") String userID, @PathParam("work_flow_id") int workFlowId) {
		return createDataHolder(workFlowMap.fetchWorkFlow(userID, workFlowId));
	}

	@GET
	@Path("/fetch/workflows/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DataHolder fetchWorkFlowIds(@PathParam("user_id") String userID) {
		return new DataHolder(workFlowMap.fetchWorkFlows(userID));
	}

	private DataHolder createDataHolder(OrchestrationEngineWorkFlow<WeatherDetails> workFlow) {
		WorkFlowState workFlowState = workFlow.getWorkFlowState();
		switch (workFlowState.getExecutionStatus()) {
		case EXECUTION_COMPLETE:
			return new DataHolder(workFlow.fetchResult(), "result");
		case EXECUTION_FAILURE:
			return new DataHolder(new WorkFlowState(workFlowState), "status");
		default:
			return new DataHolder(workFlowState, "status");
		}
	}

}
