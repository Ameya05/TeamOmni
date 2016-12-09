package org.team.omni;

import java.io.IOException;

import org.jooq.Converter;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherDetailsConverter implements Converter<String, WeatherDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7625437558707252886L;

	private ObjectMapper objectMapper;

	public WeatherDetailsConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public WeatherDetails from(String arg0) {
		try {if(arg0==null){
			return null;
		}
			return objectMapper.readValue(arg0, WeatherDetails.class);
		} catch (IOException e) {
			throw new OrchestrationEngineException("Unable to convert the weatherdetails JSON to corresponding object", e);
		}
	}

	@Override
	public Class<String> fromType() {
		return String.class;
	}

	@Override
	public String to(WeatherDetails arg0) {
		try {
			if(arg0==null){
				return null;
			}
			return objectMapper.writeValueAsString(arg0);
		} catch (JsonProcessingException e) {
			throw new OrchestrationEngineException("Unable to convert the weatherdetails object to corresponding JSON", e);
		}
	}

	@Override
	public Class<WeatherDetails> toType() {
		return WeatherDetails.class ;
	}

}
