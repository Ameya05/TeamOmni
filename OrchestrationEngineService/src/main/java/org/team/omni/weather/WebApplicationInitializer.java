package org.team.omni.weather;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.workflow.WorkFlowStateFactory;

public class WebApplicationInitializer implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger("Orchestration");

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("Starting up!");
		String configFolderName = (String) servletContextEvent.getServletContext().getAttribute("configuration_folder");
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
			props.load(new FileInputStream(propFile));
			hikariCPConfig.load(new FileInputStream(hikariCPConfigFile));
		} catch (SecurityException | IOException e) {
			throw new OrchestrationEngineException(e);
		}
		DataSource dataSource = OrchestrationEngineUtils.createDataSource(hikariCPConfig);
		Set<Object> keys = props.keySet();
		Map<String, String> serviceAddressDirectory = new HashMap<>();
		keys.forEach((Object key) -> {
			String keyStr = (String) key;
			serviceAddressDirectory.put(keyStr, props.getProperty(keyStr));
		});
		WorkFlowStateFactory.getWorkFlowStateFactory().setDataSource(dataSource);
		serviceFactory.setServiceAddressDirectory(serviceAddressDirectory);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Shutting down!");
	}
}