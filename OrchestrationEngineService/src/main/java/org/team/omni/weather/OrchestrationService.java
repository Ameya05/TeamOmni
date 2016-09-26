package org.team.omni.weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.team.omni.orchestration.engine.workflow.WorkFlowExecutionStatus;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;

@Path("/")
public class OrchestrationService {

	@Inject
	private WorkFlowMap workFlowMap;

	@POST
	@Path("/initiate")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String initiate(@FormParam("date") String date, @FormParam("time") String time, @FormParam("station") String stationName, @FormParam("id") Long id, @FormParam("idtoken") String idtoken) {
		WorkFlowExecutionStatus executionStatus = workFlowMap.createWorkFlow(id, stationName, LocalDateTime.parse(date + time, DateTimeFormatter.ofPattern("MM/dd/yyyyHH:mm:ss")));
		return executionStatus.getValue();
	}

	@GET
	@Path("/queryStatus/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public WorkFlowState queryStatus(@PathParam("id") long id) {
		return workFlowMap.fetchWorkFlowState(id);
	}

}
