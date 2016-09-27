package org.team.omni.orchestration.engine.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.exceptions.ServiceCreationException;

public class ServiceFactory {
	private static final ServiceFactory SERVICE_FACTORY = new ServiceFactory();

	private OrchestrationEngineValueStore orchestrationEngineValueStore = OrchestrationEngineValueStore.getOrchestrationEngineValueStore();
	private Map<String, String> serviceAddressDirectory;
	private Client client;

	private ServiceFactory() {
		client = ClientBuilder.newBuilder().register(new MultiPartFeature()).build();
	}

	public static ServiceFactory getServiceFactory() {
		return SERVICE_FACTORY;
	}

	public <T> T createService(Class<T> serviceClass) throws ServiceCreationException {
		try {
			return serviceClass.getConstructor(WebTarget.class, OrchestrationEngineValueStore.class).newInstance(client.target(getServiceAddress(serviceClass)), orchestrationEngineValueStore);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ServiceCreationException("Service: " + serviceClass.getName(), e);
		}
	}

	public String getServiceAddress(Class<?> servcieClass) throws ServiceCreationException {
		String serviceClassName = servcieClass.getSimpleName();
		if (serviceAddressDirectory.containsKey(serviceClassName)) {
			return serviceAddressDirectory.get(serviceClassName);
		} else {
			throw new ServiceCreationException("Service Creation Failed for the service: " + serviceClassName);
		}
	}

	public Map<String, String> getServiceAddressDirectory() {
		return serviceAddressDirectory;
	}

	public void setServiceAddressDirectory(Map<String, String> serviceAddressDirectory) {
		this.serviceAddressDirectory = serviceAddressDirectory;
	}

}
