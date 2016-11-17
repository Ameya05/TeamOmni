package org.team.omni.orchestration.engine.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.curator.x.discovery.ServiceInstance;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.weather.InstanceDetails;

public class StormClusteringService extends Service {

	public StormClusteringService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore,ServiceInstance<InstanceDetails> serviceInstance) {
		super(serviceAddress.path("/storm/clustering"), orchestrationEngineValueStore,serviceInstance);
	}

	public StormClusteringService(WebTarget serviceAddress) {
		super(serviceAddress);
	}

	public File genrateStormClusteringFile(File kmlFile) throws IOException, ParseException {
		MultiPart multiPartEntiry = new FormDataMultiPart();
		multiPartEntiry.bodyPart(new FileDataBodyPart("kml", kmlFile));
		return OrchestrationEngineUtils.getOrchestrationEngineUtils().saveFileFromResponse(serviceAddress.request().post(Entity.entity(multiPartEntiry, MediaType.MULTIPART_FORM_DATA)), orchestrationEngineValueStore.getServiceFolder());
	}

}
