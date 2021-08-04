package gavengers.wag.util.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class User {
    private String nickName;
    private String email;     // Id
    private String userId;
    private Timestamp registerTime;
    private ArrayList<String> tokenList;

    public User(String nickName, String email, String userId, Timestamp registerTime, ArrayList<String> tokenList) {
        this.nickName = nickName;
        this.email = email;
        this.userId = userId;
        this.registerTime = registerTime;
        this.tokenList = tokenList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getRegisterTime(){
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime){
        this.registerTime = registerTime;
    }

    public ArrayList<String> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<String> tokenList) {
        this.tokenList = tokenList;
    }

}