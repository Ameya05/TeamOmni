package org.team.omni.weather.aurora.client;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.team.omni.weather.aurora.bean.*;
import org.team.omni.weather.aurora.client.sdk.*;
import org.team.omni.weather.aurora.utils.*;

public class AuroraThriftClient {
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(AuroraThriftClient.class);
	
	/** The properties. */
	private static Properties properties = new Properties();
	
	/** The read only scheduler client. */
	private ReadOnlyScheduler.Client readOnlySchedulerClient = null;
	
	/** The aurora scheduler manager client. */
	private AuroraSchedulerManager.Client auroraSchedulerManagerClient = null;
	
	public ReadOnlyScheduler.Client getReadOnlySchedulerClient() {
		return readOnlySchedulerClient;
	}

	public void setReadOnlySchedulerClient(
			ReadOnlyScheduler.Client readOnlySchedulerClient) {
		this.readOnlySchedulerClient = readOnlySchedulerClient;
	}

	public AuroraSchedulerManager.Client getAuroraSchedulerManagerClient() {
		return auroraSchedulerManagerClient;
	}

	public void setAuroraSchedulerManagerClient(
			AuroraSchedulerManager.Client auroraSchedulerManagerClient) {
		this.auroraSchedulerManagerClient = auroraSchedulerManagerClient;
	}

	/** The thrift client. */
	private static AuroraThriftClient thriftClient = null;
	
	/**
	 * Instantiates a new aurora thrift client.
	 */
	private AuroraThriftClient() {}
	
	/**
	 * Gets the Aurora thrift client.
	 *
	 * @param auroraSchedulerPropFile the aurora scheduler prop file
	 * @return the Aurora thrift client
	 * @throws Exception
	 */
	public static AuroraThriftClient getAuroraThriftClient(String auroraSchedulerPropFile) throws Exception {
		try {
			if(thriftClient == null) {
				thriftClient = new AuroraThriftClient();
				
				// construct connection url for scheduler
				properties.load(AuroraThriftClient.class.getClassLoader().getResourceAsStream(auroraSchedulerPropFile));
				String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
				String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
				String connectionUrl = MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort);
				
				thriftClient.readOnlySchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(connectionUrl);
				thriftClient.auroraSchedulerManagerClient = AuroraSchedulerClientFactory.createSchedulerManagerClient(connectionUrl);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return thriftClient;
	}
	
	/**
	 * Creates the job.
	 *
	 * @param jobConfigBean the job config bean
	 * @return the response bean
	 * @throws Exception the exception
	 */
	public ResponseBean createJob(JobConfigBean jobConfigBean) throws Exception {
		ResponseBean response = null;
		try {
			if(jobConfigBean != null) {
				JobConfiguration jobConfig = AuroraThriftClientUtil.getAuroraJobConfig(jobConfigBean);
				Response createJobResponse = this.auroraSchedulerManagerClient.createJob(jobConfig);
				response = AuroraThriftClientUtil.getResponseBean(createJobResponse, ResponseResultType.CREATE_JOB);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Gets the job details.
	 *
	 * @param jobKeyBean the job key bean
	 * @return the job details
	 * @throws Exception the exception
	 */
	public JobDetailsResponseBean getJobDetails(JobKeyBean jobKeyBean) throws Exception {
		JobDetailsResponseBean response = null;
		try {
			if(jobKeyBean != null) {
				JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
				Set<JobKey> jobKeySet = new HashSet<>();
				jobKeySet.add(jobKey);
				
				TaskQuery query = new TaskQuery();
				query.setJobKeys(jobKeySet);
				
				Response jobDetailsResponse = this.readOnlySchedulerClient.getTasksStatus(query);
				response = (JobDetailsResponseBean) AuroraThriftClientUtil.getResponseBean(jobDetailsResponse, ResponseResultType.GET_JOB_DETAILS);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Gets the job list.
	 *
	 * @param ownerRole the owner role
	 * @return the job list
	 * @throws Exception the exception
	 */
	public GetJobsResponseBean getJobList(String ownerRole) throws Exception {
		GetJobsResponseBean response = null;
		try {
				Response jobListResponse = this.readOnlySchedulerClient.getJobs(ownerRole);
				response = (GetJobsResponseBean) AuroraThriftClientUtil.getResponseBean(jobListResponse, ResponseResultType.GET_JOBS);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	
	/**
	 * Kill tasks.
	 *
	 * @param jobKeyBean the job key bean
	 * @param instances the instances
	 * @return the response bean
	 * @throws Exception the exception
	 */
	public ResponseBean killTasks(JobKeyBean jobKeyBean, Set<Integer> instances) throws Exception {
		ResponseBean response = null;
		try {
			if(jobKeyBean != null) {
				JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
				Response killTaskResponse = this.auroraSchedulerManagerClient.killTasks(jobKey, instances);
				response = AuroraThriftClientUtil.getResponseBean(killTaskResponse, ResponseResultType.KILL_TASKS);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
}