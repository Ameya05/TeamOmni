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
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.workflow.WorkFlowStateFactory;
import org.team.omni.weather.InstanceDetails;

@WebListener
public class WebApplicationInitializer implements ServletContextListener {
	private static final String ZOOKEPER_SLEEPS_RETRIES = "zookeper.sleeps.retries";
	private static final String ZOOKEPER_RETRY_TIMES = "zookeper.retry.times";
	private static final String ZOOKEEPER_ADDRESS = "zookeeper.address";
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private CuratorFramework curatorFramework;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;

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
		DataSource dataSource = OrchestrationEngineUtils.getOrchestrationEngineUtils().createDataSource(hikariCPConfig);
		CuratorFrameworkFactory.newClient(props.getProperty(ZOOKEEPER_ADDRESS), new RetryNTimes(Integer.parseInt(props.getProperty(ZOOKEPER_RETRY_TIMES)), Integer.parseInt(props.getProperty(ZOOKEPER_SLEEPS_RETRIES))));
		curatorFramework.start();
		WorkFlowStateFactory.getWorkFlowStateFactory().setDataSource(dataSource);
		serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).basePath("services").client(curatorFramework).build();
		serviceFactory.setServiceDiscovery(serviceDiscovery);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Shutting down!");
		try {
			serviceDiscovery.close();
		} catch (IOException e) {
			throw new OrchestrationEngineException(e);
		}
		curatorFramework.close();
	}
}