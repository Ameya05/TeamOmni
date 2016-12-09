package org.team.omni.orchestration.engine.services;

import java.io.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.curator.x.discovery.ServiceInstance;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class ForecastTriggerService extends Service {

	public ForecastTriggerService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore,ServiceInstance<InstanceDetails> serviceInstance,WorkFlowState workFlowState) {
		super(serviceAddress.path("/forecast/trigger"), orchestrationEngineValueStore,serviceInstance,workFlowState);
	}

	public ForecastTriggerService(WebTarget servicePath) {
		super(servicePath);
	}

	public boolean triggerWeatherForecast(File clusteringFile) {
		MultiPart multiPartEntity = new FormDataMultiPart();
		multiPartEntity.bodyPart(new FileDataBodyPart("clustering", clusteringFile,MediaType.TEXT_PLAIN_TYPE));
		return serviceAddress.request().post(Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA)).readEntity(Boolean.class);
	}

}
