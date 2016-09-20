package org.team.omni;

import java.util.HashMap;

public class OrchestrationEngineValueStore extends HashMap<String, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8675947681233590209L;

	public static final String SERVICE_FOLDER = "ServiceFolder";

	private static OrchestrationEngineValueStore orchestrationEngineValueStore = new OrchestrationEngineValueStore();

	private OrchestrationEngineValueStore() {
	}

	public static OrchestrationEngineValueStore getOrchestrationEngineValueStore() {
		return orchestrationEngineValueStore;
	}

	public String getServiceFolder() {
		return (String) get(SERVICE_FOLDER);
	}

	public void setServiceFolder(String serviceFolder) {
		put(SERVICE_FOLDER, serviceFolder);
	}

}
