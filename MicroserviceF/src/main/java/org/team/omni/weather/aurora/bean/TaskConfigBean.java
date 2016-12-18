package org.team.omni.weather.aurora.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TaskConfigBean {

private String taskName;
	
	/** The resources. */
	private ResourceBean resources;
	
	/** The max failures. */
	private int maxFailures;
	
	/** The processes. */
	private Set<ProcessBean> processes;
	
	/**
	 *  The order of processes
	 */
	private ArrayList<String> order;

	/**
	 * Instantiates a new task config bean.
	 *
	 * @param taskName the task name
	 * @param processes the processes
	 * @param resources the resources
	 */
	public TaskConfigBean(String taskName, Set<ProcessBean> processes, ResourceBean resources) {
		this.taskName = taskName;
		this.processes = processes;
		this.resources = resources;
		
		// set default value
		this.maxFailures = 1;
	}
	
	/**
	 * Gets the task name.
	 *
	 * @return the task name
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * Sets the task name.
	 *
	 * @param taskName the new task name
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}



	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	public ResourceBean getResources() {
		return resources;
	}

	/**
	 * Sets the resources.
	 *
	 * @param resources the new resources
	 */
	public void setResources(ResourceBean resources) {
		this.resources = resources;
	}

	/**
	 * Gets the max failures.
	 *
	 * @return the max failures
	 */
	public int getMaxFailures() {
		return maxFailures;
	}

	/**
	 * Sets the max failures.
	 *
	 * @param maxFailures the new max failures
	 */
	public void setMaxFailures(int maxFailures) {
		this.maxFailures = maxFailures;
	}

	/**
	 * Gets the processes.
	 *
	 * @return the processes
	 */
	public Set<ProcessBean> getProcesses() {
		if(processes == null) {
			processes = new HashSet<>();
		}
		
		return processes;
	}

	/**
	 * Sets the processes.
	 *
	 * @param processes the new processes
	 */
	public void setProcesses(Set<ProcessBean> processes) {
		this.processes = processes;
	}

	public ArrayList<String> getOrder() {
		return order;
	}

	public void setOrder(ArrayList<String> order) {
		this.order = order;
	}
	
}
