package org.team.omni.weather;

import java.io.File;

import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;
/**
 * 
 * @author Eldho Mathulla
 *
 */
public class CurrentApplicationContextListener extends ApplicationContextListener {

	static {
		System.setProperty("my.log", System.getProperty("user.dir") + File.separator + "MicroClogs.log");
	}
	final static Logger logger = Logger.getLogger(CurrentApplicationContextListener.class);
	
	public CurrentApplicationContextListener() {
		this.address = System.getenv("DOCKER_HOST");
		logger.info("Set this.address: "+this.address);
		
		this.port = 8080;
		this.maxWorkLoad = 100;
		this.serviceName = "StormDetectionService";
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		super.contextDestroyed(arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("Context Path " + servletContextEvent.getServletContext().getContextPath());
		super.contextInitialized(servletContextEvent);
	}

}
