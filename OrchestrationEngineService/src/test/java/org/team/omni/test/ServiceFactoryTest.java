package org.team.omni.test;

import static org.junit.Assert.*;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team.omni.exceptions.ServiceCreationException;
import org.team.omni.orchestration.engine.services.DataIngestionService;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

public class ServiceFactoryTest {
	private CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("54.70.147.185:2181", new RetryNTimes(3, 1000));
	private ServiceFactory serviceFactory;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;

	@Before
	public void setUp() throws Exception {
		curatorFramework.start();
		serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).basePath("services").client(curatorFramework).build();
		serviceFactory = ServiceFactory.getServiceFactory();
		serviceFactory.setServiceDiscovery(serviceDiscovery);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ServiceCreationException {
		DataIngestionService dataIngestionService = serviceFactory.createService(DataIngestionService.class,new WorkFlowState(null, null, 0));
		assertNotNull(dataIngestionService);
	}

}
