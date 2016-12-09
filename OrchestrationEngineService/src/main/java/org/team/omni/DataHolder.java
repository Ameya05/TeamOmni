package org.team.omni;

public class DataHolder {
	private Object data = null;
	private String type = "";

	public DataHolder() {
	}

	public DataHolder(Object data) {
		this.data = data;
	}

	public DataHolder(Object data, String type) {
		this(data);
		this.type = type;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
