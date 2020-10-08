package gavengers.wag;

public class Model__friend__addFriend {
    private String mytoken; // Own token
    private String targetnickname; // target's email
    private String result;
    public Model__friend__addFriend(String token, String targetnickname){
        this.mytoken = token;
        this.targetnickname = targetnickname;
    }
    @Override
    public String toString(){
        return "Model__friend__addFriend{"+
                "mytoken='"+getToken()+
                "\', targetnickname='"+getTargetnickname()+
                "\', result='"+getResult()+"\'}";
    }
    public void setToken(String token){this.mytoken = token;}
    public String getToken(){ return this.mytoken;}

    public void setTargetnickname(String targetnickname){ this.targetnickname = targetnickname;}
    public String getTargetnickname(){ return this.targetnickname;}

    public void setResult(String result){ this.result = result;}
    public String getResult(){ return this.result;}
}
