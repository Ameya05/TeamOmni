package org.team.omni.orchestration.engine.services;

import javax.ws.rs.client.WebTarget;

import org.apache.curator.x.discovery.ServiceInstance;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.weather.InstanceDetails;

/**
 * This class act as base class for all the micro service classes. All micro
 * services should extend this class
 * 
 * @author Eldho Mathulla
 *
 */
public abstract class Service {

	protected WebTarget serviceAddress;
	protected OrchestrationEngineValueStore orchestrationEngineValueStore;
	private ServiceInstance<InstanceDetails> serviceInstance = null;

	/**
	 * 
	 * @param serviceAddress
	 *            the url for the microservice
	 * @param orchestrationEngineValueStore
	 */
	public Service(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore, ServiceInstance<InstanceDetails> serviceInstance) {
		this.setServiceAddress(serviceAddress);
		this.orchestrationEngineValueStore = orchestrationEngineValueStore;
		this.setServiceInstance(serviceInstance);
	}

	public Service(WebTarget servicePath) {
		this(servicePath, OrchestrationEngineValueStore.getOrchestrationEngineValueStore(), null);
	}

	public WebTarget getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(WebTarget serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public ServiceInstance<InstanceDetails> getServiceInstance() {
		return serviceInstance;
	}

	public void setServiceInstance(ServiceInstance<InstanceDetails> serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

}
