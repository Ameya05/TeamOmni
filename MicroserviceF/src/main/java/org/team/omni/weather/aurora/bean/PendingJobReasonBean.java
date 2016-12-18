package org.team.omni.weather.aurora.bean;

import java.util.Set;

import org.team.omni.weather.aurora.client.sdk.PendingReason;

public class PendingJobReasonBean extends ResponseBean {

private Set<PendingReason> reasons;
	
	/**
	 * Instantiates a new pending job response bean.
	 *
	 * @param responseBean the response bean
	 */
	public PendingJobReasonBean(ResponseBean responseBean) {
		this.setResponseCode(responseBean.getResponseCode());
		this.setServerInfo(responseBean.getServerInfo());
	}

	/**
	 * Gets the reasons.
	 *
	 * @return the reasons
	 */
	public Set<PendingReason> getReasons() {
		return reasons;
	}

	/**
	 * Sets the reasons.
	 *
	 * @param reasons the new reasons
	 */
	public void setReasons(Set<PendingReason> reasons) {
		this.reasons = reasons;
	}

	/* (non-Javadoc)
	 * @see org.apache.airavata.cloud.aurora.client.bean.ResponseBean#toString()
	 */
	@Override
	public String toString() {
		return "PendingJobResponseBean [reasons=" + reasons + "]";
	}
	
}
