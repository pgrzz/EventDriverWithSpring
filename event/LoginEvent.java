package EventModel.event;

/**
 * Created by lenovo on 2016/9/9.
 */
public class LoginEvent extends Event {

    // 比如 定义一些 登陆时间和ip 等
    private String IP;
    private String loginTime;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
