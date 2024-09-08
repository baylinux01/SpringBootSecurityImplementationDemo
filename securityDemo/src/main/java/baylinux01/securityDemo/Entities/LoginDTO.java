package baylinux01.securityDemo.Entities;

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
