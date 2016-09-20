package org.team.omni.orchestration.engine.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.ws.rs.client.WebTarget;

import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.exceptions.ServiceCreationException;

public class ServiceFactory {
	private static final ServiceFactory SERVICE_FACTORY = new ServiceFactory();

	private OrchestrationEngineValueStore orchestrationEngineValueStore = null;
	private Map<String, String> serviceAddressDirectory;

	private ServiceFactory() {
	}

	public static ServiceFactory getServiceFactory() {
		return SERVICE_FACTORY;
	}

	public <T> T createService(Class<T> serviceClass) throws ServiceCreationException {
		try {
			return serviceClass.getConstructor(WebTarget.class, OrchestrationEngineValueStore.class).newInstance(getServiceAddress(serviceClass), orchestrationEngineValueStore);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ServiceCreationException(e);
		}
	}

	public String getServiceAddress(Class<?> servcieClass) {
		return serviceAddressDirectory.get(servcieClass.getName());
	}

	public Map<String, String> getServiceAddressDirectory() {
		return serviceAddressDirectory;
	}

	public void setServiceAddressDirectory(Map<String, String> serviceAddressDirectory) {
		this.serviceAddressDirectory = serviceAddressDirectory;
	}

}
