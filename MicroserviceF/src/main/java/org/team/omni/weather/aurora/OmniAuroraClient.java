package org.team.omni.weather.aurora;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.team.omni.weather.aurora.client.AuroraThriftClient;
import org.team.omni.weather.aurora.bean.IdentityBean;
import org.team.omni.weather.aurora.bean.JobConfigBean;
import org.team.omni.weather.aurora.bean.JobDetailsResponseBean;
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
import org.team.omni.weather.aurora.client.sdk.ScheduleStatus;
import org.team.omni.weather.aurora.client.sdk.ScheduledTask;
import org.team.omni.weather.aurora.client.sdk.TaskConfig;
import org.team.omni.weather.aurora.utils.AuroraThriftClientUtil;
import org.team.omni.weather.aurora.utils.Constants;
import org.team.omni.weather.aurora.utils.ResponseCodeEnum;
import org.team.omni.weather.mesos.MesosService;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

public class OmniAuroraClient {

	final static Logger logger = Logger.getLogger(OmniAuroraClient.class);
	
	private final static String SLAVE_ONE_IP = "52.53.179.0";
	private final static String SLAVE_TWO_IP = "52.53.179.0";
	private final static String SLAVE_ONE_HOST = "sga-mesos-slave-1";
	private final static String SLAVE_TWO_HOST = "sga-mesos-slave-2";
	private final static String PORT = "1338";
	
	private MesosService mesosService;
	
	public OmniAuroraClient(MesosService mesosService)
	{
		this.mesosService = mesosService;
	}
	
	/** The properties. */
	
	/**
	 * Gets the job summary.
	 *
	 * @param client the client
	 * @return the job summary
	 */
	public void getJobSummary(ReadOnlyScheduler.Client client) 
	{
		try 
		{
			Response response = client.getJobs("team-omni");
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
	
	/**
	 * Returns the imageURL generated from the job
	 * @param requestID
	 * @return imageURL
	 * @throws Exception
	 */
	public String createJob(String requestID) throws Exception 
	{
		JobDetailsResponseBean jobStatus;
		logger.info("Inside OmniAuroraClient.createJob()");
		JobKeyBean jobKey = new JobKeyBean("devel", "team-omni", "omni_wrf_"+requestID);
		IdentityBean owner = new IdentityBean("team-omni");
		String dockerContainerName = "omni-postproc-"+requestID ;
		
		ProcessBean proc1 = new ProcessBean("process_1", "docker run -i --volumes-from wpsgeog --volumes-from wrfinputsandy -v ~/wrfoutput:/wrfoutput --name omni-ncarwrfsandy-"+requestID+" bigwxwrf/ncar-wrf /wrf/run-wrf", false);
		ProcessBean proc2 = new ProcessBean("process_2","docker run -i --rm=true -v ~/wrfoutput:/wrfoutput --name "+dockerContainerName+" bigwxwrf/ncar-ncl",false);
		Set<ProcessBean> processes = new HashSet<>();
		processes.add(proc1);
		processes.add(proc2);
		
		
		
		
		ResourceBean resources = new ResourceBean(0.2, 200, 200);
		
		TaskConfigBean taskConfig = new TaskConfigBean("run_forecast_task", processes, resources);
		
		ArrayList<String> order = new ArrayList<String>();
		order.add("process_1");
		order.add("process_2");
			
		taskConfig.setOrder(order);
		
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		logger.info(executorConfigJson);
		
		AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		
		logger.info(response);
		logger.info("Done with createJob()");
		ScheduledTask currentTask = new ScheduledTask();
		
		do
		{
			jobStatus = client.getJobDetails(jobKey);
			for(ScheduledTask task : jobStatus.getTasks())
			{
				if(task.getAssignedTask().getTask().getExecutorConfig().getData().indexOf(dockerContainerName+" ") > 0)
				{
					currentTask = task;
					break;
				}
			}
			logger.info("Status of task: "+currentTask.getStatus().toString()+ " \nSleeping for 10 seconds..");
			Thread.sleep(10000);
		}
		while(	!( currentTask.getStatus().name().equals(ScheduleStatus.FINISHED.name()) ||
					currentTask.getStatus().name().equals(ScheduleStatus.FAILED.name()) ||
					currentTask.getStatus().name().equals(ScheduleStatus.KILLED.name()) ));

		logger.info("Task finished !");
		
		String imageURL = getOutputLink(currentTask);
		logger.info("imageURL: "+imageURL);
		
		return imageURL;
	}
	
	/**
	 * Given a task, generate the output image file url
	 * @param task
	 * @return imageURL
	 */
	private String getOutputLink(ScheduledTask task)
	{
		String imageURL = "http://";
		
		if (task.getAssignedTask().getSlaveHost().equals(SLAVE_ONE_HOST))
			imageURL += SLAVE_ONE_IP;
		else if (task.getAssignedTask().getSlaveHost().equals(SLAVE_TWO_HOST))
			imageURL += SLAVE_TWO_IP;
		
		imageURL += ":"+PORT+"/download/";
		imageURL += task.getAssignedTask().getTaskId();
		imageURL += "/wrfoutput/Precip_total.gif";
		
		return imageURL;
	}
	
	public void killTasks(String jobName) throws Exception {
		JobKeyBean jobKey = new JobKeyBean("devel", "team-omni", jobName);
		AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.killTasks(jobKey, new HashSet<>());
		logger.info(response);
	}
	
}
