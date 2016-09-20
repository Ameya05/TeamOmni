package org.team.omni.orchestration.engine.services;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;

public class StormDetectionService extends Service {

	public StormDetectionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/storm/detection"), orchestrationEngineValueStore);
	}

	public File generateKMLFile(String key) throws IOException {
		MultivaluedMap<String, String> map = new MultivaluedStringMap();
		map.add("key", key);
		return OrchestrationEngineUtils.saveFileFromResposne(serviceAddress.request().post(Entity.form(map)), orchestrationEngineValueStore.getServiceFolder());
	}

}
