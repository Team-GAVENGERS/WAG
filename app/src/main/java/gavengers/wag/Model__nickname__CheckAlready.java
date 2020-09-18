package gavengers.wag;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Model__nickname__CheckAlready {
    private boolean isRight = false;
    private String nickname;
    private String result;

    @Override
    public String toString() {
        return "Model__nickname__CheckAlready{" +
                "isRight=" + isRight +
                ", nickname='" + nickname + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public Model__nickname__CheckAlready(String nickname) {
        this.nickname = nickname;
    }

}