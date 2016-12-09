package org.team.omni.weather.aurora;

import java.util.HashSet;
import java.util.Set;

import org.team.omni.weather.aurora.client.AuroraThriftClient;
import org.team.omni.weather.aurora.bean.IdentityBean;
import org.team.omni.weather.aurora.bean.JobConfigBean;
import org.team.omni.weather.aurora.bean.JobKeyBean;
import org.team.omni.weather.aurora.bean.ProcessBean;
import org.team.omni.weather.aurora.bean.ResourceBean;
import org.team.omni.weather.aurora.bean.ResponseBean;
import org.team.omni.weather.aurora.bean.TaskConfigBean;
import org.team.omni.weather.aurora.client.sdk.ExecutorConfig;
import org.team.omni.weather.aurora.client.sdk.GetJobsResult;
import org.team.omni.weather.aurora.client.sdk.Identity;
import org.team.omni.weather.aurora.client.sdk.JobConfiguration;
import org.team.omni.weather.aurora.client.sdk.JobKey;
import org.team.omni.weather.aurora.client.sdk.ReadOnlyScheduler;
import org.team.omni.weather.aurora.client.sdk.Response;
import org.team.omni.weather.aurora.client.sdk.TaskConfig;
import org.team.omni.weather.aurora.utils.AuroraThriftClientUtil;
import org.team.omni.weather.aurora.utils.Constants;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

public class OmniAuroraClient {

	final static Logger logger = Logger.getLogger(OmniAuroraClient.class);
	
	/** The properties. */
	
	/**
	 * Gets the job summary.
	 *
	 * @param client the client
	 * @return the job summary
	 */
	public static void getJobSummary(ReadOnlyScheduler.Client client) 
	{
		try 
		{
			Response response = client.getJobs("centos");
			logger.info("Response status: " + response.getResponseCode().name());
			if(response.getResult().isSetGetJobsResult()) 
			{
				GetJobsResult result = response.getResult().getGetJobsResult();
				logger.info(result);
				Set<JobConfiguration> jobConfigs = result.getConfigs();
				for(JobConfiguration jobConfig : jobConfigs) 
				{
					logger.info(jobConfig);
					JobKey jobKey = jobConfig.getKey();
					Identity owner = jobConfig.getOwner();
					TaskConfig taskConfig = jobConfig.getTaskConfig();
					ExecutorConfig exeConfig = taskConfig.getExecutorConfig();
					
					logger.info("\n**** JOB CONFIG ****");
					logger.info("\t # instanceCount: " + jobConfig.getInstanceCount());
					logger.info("\t >> Job Key <<");
					logger.info("\t\t # name: " + jobKey.getName());
					logger.info("\t\t # role: " + jobKey.getRole());
					logger.info("\t\t # environment: " + jobKey.getEnvironment());
					logger.info("\t >> Identity <<");
					logger.info("\t\t # owner: " + owner.getUser());
					logger.info("\t >> Task Config <<");
					logger.info("\t\t # numCPUs: " + taskConfig.getNumCpus());
					logger.info("\t\t # diskMb: " + taskConfig.getDiskMb());
					logger.info("\t\t # ramMb: " + taskConfig.getRamMb());
					logger.info("\t\t # priority: " + taskConfig.getPriority());
					logger.info("\t >> Executor Config <<");
					logger.info("\t\t # name: " + exeConfig.getName());
					logger.info("\t\t # data: " + exeConfig.getData());
				}
				
			}
		} catch (TException e) {
			logger.error("Error while executing getJobSummary",e);
		}
	}
	
	public static void createJob() throws Exception {
		
		logger.info("Inside OmniAuroraClient.createJob()");
		JobKeyBean jobKey = new JobKeyBean("devel", "team-omni", "hello_world");
		IdentityBean owner = new IdentityBean("centos");
		
		ProcessBean proc1 = new ProcessBean("process_1", "echo 'hello_world'", false);
		Set<ProcessBean> processes = new HashSet<>();
		processes.add(proc1);
		
		ResourceBean resources = new ResourceBean(0.2, 8, 1);
		
		TaskConfigBean taskConfig = new TaskConfigBean("hello_world_task", processes, resources);
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		logger.info(executorConfigJson);
		
		AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		logger.info(response);
		logger.info("Done with createJob()");
	}
}
