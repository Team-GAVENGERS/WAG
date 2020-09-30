package gavengers.wag;

public class Model__friend__addFreind {
    private String token;
    private String targetEmail;
    private String result;
    @Override
    public String toString(){
        return "Model__friend__addFriend{"+
                "token='"+token+
                "\', targetEmail='"+targetEmail+
                "\', result='"+result+"\'}";
    }
    public void setToken(String token){this.token = token;}
    public String getToken(){ return this.token;}

    public void setTargetEmail(String targetEmail){ this.targetEmail = targetEmail;}
    public String getTargetEmail(){ return this.targetEmail;}

    public void setResult(String result){ this.result = result;}
    public String getResult(){ return this.result;}
}
