package org.team.omni.orchestration.engine.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;

public class StormClusteringService extends Service {

	public StormClusteringService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/storm/clustering"), orchestrationEngineValueStore);
	}

	public File genrateStormClusteringFile(File kmlFile) throws IOException, ParseException {
		MultiPart multiPartEntiry = new FormDataMultiPart();
		multiPartEntiry.bodyPart(new FileDataBodyPart("kml", kmlFile));
		return OrchestrationEngineUtils.saveFileFromResposne(serviceAddress.request().post(Entity.entity(multiPartEntiry, MediaType.MULTIPART_FORM_DATA)), orchestrationEngineValueStore.getServiceFolder());
	}

}
