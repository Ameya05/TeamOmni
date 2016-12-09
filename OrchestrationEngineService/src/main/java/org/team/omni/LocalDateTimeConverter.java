package org.team.omni;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.jooq.Converter;

public class LocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7002641984226432550L;
	private ZoneId zoneId = ZoneId.of("UTC");

	public LocalDateTimeConverter() {
	}

	@Override
	public LocalDateTime from(Timestamp timestamp) {
		long time = timestamp.getTime();
		System.out.println(time);
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), zoneId);
	}

	@Override
	public Class<Timestamp> fromType() {
		return Timestamp.class;
	}

	@Override
	public Timestamp to(LocalDateTime arg0) {
		return new Timestamp(arg0.toInstant(ZoneOffset.UTC).toEpochMilli());
	}

	@Override
	public Class<LocalDateTime> toType() {
		return LocalDateTime.class;
	}

}
