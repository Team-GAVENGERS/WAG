package gavengers.wag;

public class Model__friend__addFreind {
    private String mytoken; // Own token
    private String targetemail; // target's email
    private String result;
    public Model__friend__addFreind(String token,String targetEmail){
        this.mytoken = token;
        this.targetemail = targetEmail;
    }
    @Override
    public String toString(){
        return "Model__friend__addFriend{"+
                "mytoken='"+getToken()+
                "\', targetemail='"+getTargetEmail()+
                "\', result='"+getResult()+"\'}";
    }
    public void setToken(String token){this.mytoken = token;}
    public String getToken(){ return this.mytoken;}

    public void setTargetEmail(String targetEmail){ this.targetemail = targetEmail;}
    public String getTargetEmail(){ return this.targetemail;}

    public void setResult(String result){ this.result = result;}
    public String getResult(){ return this.result;}
}
