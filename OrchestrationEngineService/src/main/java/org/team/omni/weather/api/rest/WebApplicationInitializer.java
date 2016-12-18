package org.team.omni.weather.api.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;
import org.team.omni.weather.InstanceDetails;

@WebListener
public class WebApplicationInitializer implements ServletContextListener {
	private static final String ZOOKEPER_SLEEPS_RETRIES = "zookeper.sleeps.retries";
	private static final String ZOOKEPER_RETRY_TIMES = "zookeper.retry.times";
	private static final String ZOOKEEPER_ADDRESS = "zookeeper.address";
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private CuratorFramework curatorFramework;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;
	private DataSource dataSource;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("Starting up!");
		String configFolderName = (String) servletContextEvent.getServletContext().getInitParameter("configuration_folder");
		File propFile = new File(configFolderName + "/service.properties");
		File hikariCPConfigFile = new File(configFolderName + "/hikaricp_config.properties");
		Properties hikariCPConfig = new Properties();
		ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();
		Properties props = new Properties();
		try {
			File logsFolder = new File(configFolderName + "/logs");
			logsFolder.mkdirs();
			FileHandler fileHandler = new FileHandler(logsFolder.getAbsolutePath() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".log");
			fileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.INFO);
			props.load(new FileInputStream(propFile));
			hikariCPConfig.load(new FileInputStream(hikariCPConfigFile));
		} catch (SecurityException | IOException e) {
			throw new OrchestrationEngineException(e);
		}
		OrchestrationEngineValueStore.getOrchestrationEngineValueStore().setServiceFolder(configFolderName);
		dataSource = OrchestrationEngineUtils.getOrchestrationEngineUtils().createDataSource(hikariCPConfig);
		DSLContext create = DSL.using(dataSource, SQLDialect.POSTGRES_9_3);
		WorkFlowMap.createDefaultWorkFlow(create);
		if (!props.containsKey(ZOOKEEPER_ADDRESS)) {
			throw new OrchestrationEngineException("Zookeeper Address is not set");
		} else if (!props.containsKey(ZOOKEPER_RETRY_TIMES)) {
			throw new OrchestrationEngineException("Zookeeper Retry Times is not set");
		} else if (!props.containsKey(ZOOKEPER_SLEEPS_RETRIES)) {
			throw new OrchestrationEngineException("Zookeeper Retry Sleep time is not set");
		}
		String zookeeperAddress = props.getProperty(ZOOKEEPER_ADDRESS);
		int zookeeperRetryTimes = Integer.parseInt(props.getProperty(ZOOKEPER_RETRY_TIMES));
		int zookeeperSleepRetryTime = Integer.parseInt(props.getProperty(ZOOKEPER_SLEEPS_RETRIES));
		curatorFramework = CuratorFrameworkFactory.newClient(zookeeperAddress, new RetryNTimes(zookeeperRetryTimes, zookeeperSleepRetryTime));
		curatorFramework.start();
		serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).basePath("services").client(curatorFramework).build();
		serviceFactory.setServiceDiscovery(serviceDiscovery);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Shutting down!");
		try {
			if (serviceDiscovery != null) {
				serviceDiscovery.close();
			}
		} catch (IOException e) {
			throw new OrchestrationEngineException(e);
		}
		curatorFramework.close();
	}
}