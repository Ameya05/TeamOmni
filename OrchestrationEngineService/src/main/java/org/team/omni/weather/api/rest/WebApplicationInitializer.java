package org.team.omni.weather.api.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
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
			configureLogs(logsFolder);
			props.load(new FileInputStream(propFile));
			hikariCPConfig.load(new FileInputStream(hikariCPConfigFile));
		} catch (SecurityException | IOException e) {
			throw new OrchestrationEngineException(e);
		}
		dataSource = OrchestrationEngineUtils.getOrchestrationEngineUtils().createDataSource(hikariCPConfig);
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
		WorkFlowStateFactory.getWorkFlowStateFactory().setDataSource(dataSource);
		serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).basePath("services").client(curatorFramework).build();
		serviceFactory.setServiceDiscovery(serviceDiscovery);
	}

	private void configureLogs(File logsFolder) {
		String logFolderName = logsFolder.getAbsolutePath();
		ConfigurationBuilder<BuiltConfiguration> configurationBuilder = ConfigurationBuilderFactory.newConfigurationBuilder();
		configurationBuilder.setStatusLevel(Level.INFO);
		configurationBuilder.setConfigurationName("Builder");
		ComponentBuilder<?> triggeringPolicy = configurationBuilder.newComponent("Policies").addComponent(configurationBuilder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?")).addComponent(configurationBuilder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "2M"));
		AppenderComponentBuilder rollingFileAppenderComponentBuilder = configurationBuilder.newAppender("rolling", "RollingFile");
		LayoutComponentBuilder layoutComponentBuilder = configurationBuilder.newLayout("PatternLayout");
		layoutComponentBuilder.addAttribute("pattern", "%d [%t] %-5level: %msg%n").addAttribute("alwaysWriteExceptions", true);
		rollingFileAppenderComponentBuilder.add(layoutComponentBuilder).addAttribute("fileName", logFolderName + "/OrchestrationEngine.log");
		rollingFileAppenderComponentBuilder.addAttribute("filePattern", logFolderName + "/archive/orchestration-%d{MM-dd-yyyy}.log.gz");
		rollingFileAppenderComponentBuilder.addComponent(triggeringPolicy);
		AppenderComponentBuilder consoleAppenderComponentBuilder = configurationBuilder.newAppender("Stdout", "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
		consoleAppenderComponentBuilder.add(layoutComponentBuilder);
		RootLoggerComponentBuilder rootLoggerComponentBuilder = configurationBuilder.newRootLogger(Level.INFO);
		rootLoggerComponentBuilder.addComponent(rootLoggerComponentBuilder).addComponent(consoleAppenderComponentBuilder).add(configurationBuilder.newAppenderRef("Stdout"));
		configurationBuilder.add(rollingFileAppenderComponentBuilder).add(consoleAppenderComponentBuilder).add(rootLoggerComponentBuilder);
		System.out.println("Testing-1");
		BuiltConfiguration builtConfiguration = configurationBuilder.build();
		LoggerContext loggerContext = Configurator.initialize(builtConfiguration);
		LogManager.setFactory(new DefaultLoggerContextFactory(loggerContext));
		System.out.println("Testing-Last");
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