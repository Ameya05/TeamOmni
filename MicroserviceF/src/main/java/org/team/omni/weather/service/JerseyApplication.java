package org.team.omni.weather.service;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class JerseyApplication extends ResourceConfig {

	public JerseyApplication() {
		register(new ObjectMapperResolver());
		register(new JacksonFeature());
		packages("org.team.omni.weather.service");
	}

	public JerseyApplication(Set<Class<?>> classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public JerseyApplication(Class<?>... classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public JerseyApplication(ResourceConfig original) {
		super(original);
		// TODO Auto-generated constructor stub
	}

}
