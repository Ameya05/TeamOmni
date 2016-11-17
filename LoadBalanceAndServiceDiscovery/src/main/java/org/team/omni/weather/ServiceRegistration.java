package org.team.omni.weather;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class ServiceRegistration {

	private CuratorFramework curatorFramework;
	private String address;
	private int port;
	private InstanceDetails instanceDetails;
	private String serviceName;
	private ServiceInstance<InstanceDetails> serviceInstance;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;
	private String path;

	protected ServiceRegistration(CuratorFramework curatorFramework, String address, int port, String serviceName, String path, InstanceDetails instanceDetails) {
		this.curatorFramework = curatorFramework;
		this.address = address;
		this.port = port;
		this.instanceDetails = instanceDetails;
		this.serviceName = serviceName;
		this.path = path;
	}

	public void registerService() throws ServiceRegistrationException {
		try {
			ServiceInstanceBuilder<InstanceDetails> serviceInstanceBuilder = ServiceInstance.builder();
			serviceInstance = serviceInstanceBuilder.address(address).port(port).name(serviceName).uriSpec(new UriSpec("{scheme}://{address}:{port}" + path)).payload(instanceDetails).build();
			ServiceDiscoveryBuilder<InstanceDetails> serviceDiscoveryBuilder = ServiceDiscoveryBuilder.builder(InstanceDetails.class);
			JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<InstanceDetails>(InstanceDetails.class);
			serviceDiscovery = serviceDiscoveryBuilder.basePath("services").client(curatorFramework).serializer(serializer).thisInstance(serviceInstance).build();
			serviceDiscovery.start();
		} catch (Exception e) {
		}
	}

	public void unregisterService() throws ServiceRegistrationException {
		try {
			serviceDiscovery.unregisterService(serviceInstance);
		} catch (Exception e) {
			throw new ServiceRegistrationException(e);
		}
	}

	public void updateWorkLoad(int workLoad) throws ServiceUpdationException {
		try {
			instanceDetails.setWorkLoad(instanceDetails.getWorkLoad() + workLoad);
			serviceDiscovery.updateService(serviceInstance);
		} catch (Exception e) {
			throw new ServiceUpdationException(e);
		}
	}

}
