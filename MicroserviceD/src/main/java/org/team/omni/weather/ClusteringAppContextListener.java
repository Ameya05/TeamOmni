package org.team.omni.weather;

import java.io.File;

import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;
/**
 * 
 * @author Ameya Advankar
 *
 */
public class ClusteringAppContextListener extends ApplicationContextListener {

	static {
		System.setProperty("my.log", System.getProperty("user.dir") + File.separator + "MicroDlogs.log");
	}
	final static Logger logger = Logger.getLogger(ClusteringAppContextListener.class);
	
	public ClusteringAppContextListener() {
		this.address = System.getenv("DOCKER_HOST");
		logger.info("Set this.address: "+this.address);
		System.out.println("Set this.address: "+this.address);
		this.port = 8082;
		this.maxWorkLoad = 100;
		this.serviceName = "StormClusteringService";
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		super.contextDestroyed(arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("Context Path " + servletContextEvent.getServletContext().getContextPath());
		try{
			super.contextInitialized(servletContextEvent);
		}
		catch(Exception e){
			logger.error("Exception while initializing context",e);
		}
	}

}