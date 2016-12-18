package org.team.omni.weather.aurora.bean;

import org.team.omni.weather.aurora.client.sdk.Identity;
import org.team.omni.weather.aurora.client.sdk.JobKey;
import org.team.omni.weather.aurora.client.sdk.TaskConfig;

public class JobConfigurationBean {

	private JobKey job;
	private Identity owner ;
	private TaskConfigBean taskConfig;
	private String cluster;
	private boolean isService;
	private int priority;
	private boolean production;
	private int maxTaskFailures;
	private int instances;
	
	public JobConfigurationBean(JobKey job, Identity owner, TaskConfigBean taskConfig, String cluster) {
		this.setJob(job);
		this.setOwner(owner);
		this.setTaskConfig(taskConfig);
		this.setCluster(cluster);
		
		// set defaults
		this.setService(false);
		this.setMaxTaskFailures(1);
		this.setInstances(1);
	}



	public JobKey getJob() {
		return job;
	}


	public void setJob(JobKey job) {
		this.job = job;
	}


	public Identity getOwner() {
		return owner;
	}


	public void setOwner(Identity owner) {
		this.owner = owner;
	}


	public TaskConfigBean getTaskConfig() {
		return taskConfig;
	}


	public void setTaskConfig(TaskConfigBean taskConfig) {
		this.taskConfig = taskConfig;
	}


	public String getCluster() {
		return cluster;
	}


	public void setCluster(String cluster) {
		this.cluster = cluster;
	}



	public boolean isService() {
		return isService;
	}



	public void setService(boolean isService) {
		this.isService = isService;
	}



	public int getPriority() {
		return priority;
	}



	public void setPriority(int priority) {
		this.priority = priority;
	}



	public boolean isProduction() {
		return production;
	}



	public void setProduction(boolean production) {
		this.production = production;
	}



	public int getMaxTaskFailures() {
		return maxTaskFailures;
	}



	public void setMaxTaskFailures(int maxTaskFailures) {
		this.maxTaskFailures = maxTaskFailures;
	}



	public int getInstances() {
		return instances;
	}



	public void setInstances(int instances) {
		this.instances = instances;
	}
	
}
