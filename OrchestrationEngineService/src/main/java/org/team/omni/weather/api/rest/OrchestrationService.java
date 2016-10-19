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

import org.team.omni.orchestration.engine.workflow.WorkFlowExecutionStatus;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;

@Path("/")
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
	@Path("/queryStatus/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public WorkFlowState queryStatus(@PathParam("id") String id) {
		return workFlowMap.fetchWorkFlowState(id);
	}

}
