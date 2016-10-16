package org.team.omni.orchestration.engine.workflow;

import javax.sql.DataSource;

public class WorkFlowStateFactory {
	private static final WorkFlowStateFactory WORK_FLOW_STATE_FACTORY = new WorkFlowStateFactory();

	private DataSource dataSource;

	private WorkFlowStateFactory() {
	}

	public static WorkFlowStateFactory getWorkFlowStateFactory() {
		return WORK_FLOW_STATE_FACTORY;
	}

	public WorkFlowState createWorkFlowState(String userId) {
		return new WorkFlowState(dataSource, userId);

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
