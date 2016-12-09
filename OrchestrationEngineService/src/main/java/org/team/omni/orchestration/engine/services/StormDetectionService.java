package org.team.omni.orchestration.engine.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.curator.x.discovery.ServiceInstance;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

public class StormDetectionService extends Service {

	public StormDetectionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore,ServiceInstance<InstanceDetails> serviceInstance,WorkFlowState workFlowState) {
		super(serviceAddress.path("/storm/detection"), orchestrationEngineValueStore,serviceInstance,workFlowState);
	}

	public File generateKMLFile(String key) throws IOException, ParseException {
		Response response = serviceAddress.queryParam("key", key).request().get();
		return OrchestrationEngineUtils.getOrchestrationEngineUtils().saveFileFromResponse(response, orchestrationEngineValueStore.getServiceFolder());
	}
}
