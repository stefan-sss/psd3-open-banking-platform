package ro.stefan.gateway.dto;

public class GatewayHealthResponse {

	private String service;
	private String status;
	private String timestatus;
	
	public GatewayHealthResponse(String service, String status, String timestatus) {
		this.service=service;
		this.status=status;
		this.timestatus=timestatus;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimestatus() {
		return timestatus;
	}

	public void setTimestatus(String timestatus) {
		this.timestatus = timestatus;
	}
	
	
}
