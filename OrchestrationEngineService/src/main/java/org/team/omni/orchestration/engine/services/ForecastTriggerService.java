package org.team.omni.orchestration.engine.services;

import java.io.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.team.omni.OrchestrationEngineValueStore;

public class ForecastTriggerService extends Service {

	public ForecastTriggerService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/forecast/trigger"), orchestrationEngineValueStore);
	}

	public boolean triggerWeatherForecast(File clusteringFile) {
		MultiPart multiPartEntiry = new FormDataMultiPart();
		multiPartEntiry.bodyPart(new FileDataBodyPart("clustering", clusteringFile));
		return serviceAddress.request().post(Entity.entity(multiPartEntiry, MediaType.MULTIPART_FORM_DATA)).readEntity(Boolean.class);
	}

}
