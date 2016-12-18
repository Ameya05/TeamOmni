package org.team.omni;

import org.jooq.impl.EnumConverter;
import org.team.omni.orchestration.engine.workflow.WorkFlowExecutionStatus;

public class WorkFlowExecutionStatusEnumConverter extends EnumConverter<String, WorkFlowExecutionStatus> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6008276314682324021L;

	public WorkFlowExecutionStatusEnumConverter(Class<String> arg0, Class<WorkFlowExecutionStatus> arg1) {
		super(arg0, arg1);
	}

}
