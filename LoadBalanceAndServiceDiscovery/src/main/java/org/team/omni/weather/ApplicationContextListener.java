package org.team.omni.weather;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public abstract class ApplicationContextListener implements ServletContextListener {
	private CuratorFramework curatorFramework;
	private ServiceRegistration serviceRegistration;
	protected String serviceName;
	protected String servicePath;
	protected String address;
	protected int port;
	protected int maxWorkLoad;

	public ApplicationContextListener() {
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
		String zookeeperAddres = servletContextEvent.getServletContext().getInitParameter("zookeeper");
		servicePath = servletContextEvent.getServletContext().getContextPath();
		curatorFramework = CuratorFrameworkFactory.newClient(zookeeperAddres, new RetryNTimes(3, 1000));
		curatorFramework.start();
		serviceRegistration = new ServiceRegistration(curatorFramework, address, port, serviceName, servicePath, new InstanceDetails(100));
		try {
			serviceRegistration.registerService();
			LoadBalancingRequestFilter.setServiceRegistration(serviceRegistration);
			LoadBalancingResponseFilter.setServiceRegistration(serviceRegistration);
		} catch (ServiceRegistrationException e) {
			throw new ServiceException(e);
		}
	}

}
