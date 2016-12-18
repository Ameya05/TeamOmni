package org.team.omni.weather.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.team.omni.weather.mesos.MesosService;
import org.team.omni.weather.mesos.MesosStatus;

@Path("/")
public class StormForecastService {

	final static Logger logger = Logger.getLogger(StormForecastService.class);

	/**
	 * This will start he execution weather forecast given the work flow id
	 * @param workFlowID
	 * @return
	 */
	@GET
	@Path("/execute/{work_flow_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MesosStatus startMesosExecution(@PathParam("work_flow_id") String workFlowID) {
		MesosService mesosService = MesosService.createMesosService(workFlowID);
		mesosService.execute();
		return mesosService.consume();
	}

	@GET
	@Path("/status/{work_flow_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MesosStatus fetchMesosExecutionStatus(@PathParam("work_flow_id") String workFlowID) {
		return MesosService.createMesosService(workFlowID).consume();
	}

}
