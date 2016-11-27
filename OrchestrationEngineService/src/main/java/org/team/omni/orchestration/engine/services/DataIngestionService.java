package org.team.omni.orchestration.engine.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.client.WebTarget;

import org.apache.curator.x.discovery.ServiceInstance;
import org.team.omni.DataHolder;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.weather.InstanceDetails;

/**
 * This class act as an interface for Data Ingestor Micro Service
 * 
 * @author Eldho Mathulla
 *
 */
public class DataIngestionService extends Service {

	public DataIngestionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore, ServiceInstance<InstanceDetails> serviceInstance) {
		super(serviceAddress.path("/nexrad/generate/url/"), orchestrationEngineValueStore, serviceInstance);
	}

	public DataIngestionService(WebTarget servicePath) {
		super(servicePath);
	}

	/**
	 * Construct the NEXRAD file download url is station name and time stamp is
	 * given
	 * 
	 * @param stationName
	 * @param timestamp
	 * @return
	 */
	public String constructDataFileURL(String stationName, LocalDateTime timestamp) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("/MM/dd/yyyy/HH/mm/ss");
		return (String) serviceAddress.path(stationName).path(dateTimeFormatter.format(timestamp)).request().get().readEntity(DataHolder.class).getData();
	}

}
