package org.team.omni.orchestration.engine.services;


import javax.ws.rs.client.WebTarget;

import org.team.omni.OrchestrationEngineValueStore;

public abstract class Service {

	protected WebTarget serviceAddress;
	protected OrchestrationEngineValueStore orchestrationEngineValueStore;

	public Service(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		this.setServiceAddress(serviceAddress);
		this.orchestrationEngineValueStore = orchestrationEngineValueStore;
	}

	public WebTarget getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(WebTarget serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

}
