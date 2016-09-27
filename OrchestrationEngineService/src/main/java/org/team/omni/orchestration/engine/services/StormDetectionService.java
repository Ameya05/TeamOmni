package org.team.omni.orchestration.engine.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;

public class StormDetectionService extends Service {

	public StormDetectionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/storm/detection"), orchestrationEngineValueStore);
	}

	public File generateKMLFile(String key) throws IOException, ParseException {
		Response response = serviceAddress.queryParam("key", key).request().get();
		return OrchestrationEngineUtils.saveFileFromResposne(response, orchestrationEngineValueStore.getServiceFolder());
	}
}
