package org.team.omni.weather;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class CurrentApplicationContextListener extends ApplicationContextListener {

	public CurrentApplicationContextListener() {
		this.address = "localhost";
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
		System.out.println("Conext Path " + servletContextEvent.getServletContext().getContextPath());
		super.contextInitialized(servletContextEvent);
	}

}
