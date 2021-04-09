package com.sony.remoteControl;

import java.util.List;
import java.util.Map;

public class RequestBody {
	public String method;
	public int id;
	public List<Map<String, String>> params;
	public String version;

	public RequestBody() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Map<String, String>> getParams() {
		return params;
	}

	public void setParams(List<Map<String, String>> params) {
		this.params = params;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "RequestBody [method=" + method + ", id=" + id + ", params=" + params + ", version=" + version + "]";
	}

}
