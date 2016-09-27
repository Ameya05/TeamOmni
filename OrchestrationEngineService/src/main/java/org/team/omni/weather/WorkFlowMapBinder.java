package org.team.omni.weather;

import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;

@Provider
public class WorkFlowMapBinder extends AbstractBinder {

	public WorkFlowMapBinder() {
	}

	@Override
	protected void configure() {
		bind(WorkFlowMap.getWorkFlowMap()).to(WorkFlowMap.class);

	}

}
