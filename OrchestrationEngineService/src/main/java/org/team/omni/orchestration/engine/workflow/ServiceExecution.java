package org.team.omni.orchestration.engine.workflow;

import org.team.omni.orchestration.engine.services.Service;

/**
 * 
 * @author Eldho Mathulla
 *
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface ServiceExecution<T,U extends Service> {

	public T execute(U service) throws Exception;

}
