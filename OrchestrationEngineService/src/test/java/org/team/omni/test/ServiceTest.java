package org.team.omni.test;

import static org.mockito.Mockito.mock;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

public class ServiceTest {
	protected WebTarget mockServiceAddress;
	protected Builder mockBuilder;
	protected Response mockResponse;

	public ServiceTest() {
		mockServiceAddress = mock(WebTarget.class);
		mockBuilder = mock(Builder.class);
		mockResponse = mock(Response.class);
	}

}
