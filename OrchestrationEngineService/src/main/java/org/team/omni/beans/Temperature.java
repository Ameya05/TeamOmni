package org.team.omni.beans;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Temperature {
	public static final int FARENHEIT = 0;
	public static final int CELCIOUS = 1;

	private int value = -100000;
	private int unitType = CELCIOUS;

	public Temperature() {
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@JsonGetter("unit_type")
	public int getUnitType() {
		return unitType;
	}

	@JsonSetter("unit_type")
	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}

}
