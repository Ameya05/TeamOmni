package org.team.omni.weather;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import org.apache.log4j.Logger;
/**
 * 
 * @author Ameya Advankar
 *
 */
@WebListener
public class ClusteringAppContextListener implements ServletContextListener {

	private CuratorFramework curatorFramework;
	private ServiceRegistration serviceRegistration;
	protected String serviceName;
	protected String servicePath;
	protected String address;
	protected int port;
	protected int maxWorkLoad;
	
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
		try {
			serviceRegistration.unregisterService();
		} catch (ServiceRegistrationException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("Context Path " + servletContextEvent.getServletContext().getContextPath());
		System.out.println("Context Path " + servletContextEvent.getServletContext().getContextPath());
		try{
			//super.contextInitialized(servletContextEvent);
			String zookeeperAddres = servletContextEvent.getServletContext().getInitParameter("zookeeper");
			servicePath = servletContextEvent.getServletContext().getContextPath();
			curatorFramework = CuratorFrameworkFactory.newClient(zookeeperAddres, new RetryNTimes(3, 1000));
			curatorFramework.start();
			serviceRegistration = new ServiceRegistration(curatorFramework, address, port, serviceName, servicePath, new InstanceDetails(100));
			try {
				serviceRegistration.registerService();
				//LoadBalancingRequestFilter.setServiceRegistration(serviceRegistration);
				//LoadBalancingResponseFilter.setServiceRegistration(serviceRegistration);
			} catch (ServiceRegistrationException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
		}
		catch(Exception e){
			logger.error("Exception while initializing context",e);
		}
	}

}