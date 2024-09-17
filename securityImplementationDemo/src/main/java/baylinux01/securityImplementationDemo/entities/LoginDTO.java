package baylinux01.securityImplementationDemo.entities;

public class LoginDTO {
	
	private String jwt;
	private long appUserId;
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(long appUserId) {
		this.appUserId = appUserId;
	}
	
	

}
