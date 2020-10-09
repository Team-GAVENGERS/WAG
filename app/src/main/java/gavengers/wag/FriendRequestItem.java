package gavengers.wag;

import android.graphics.drawable.Drawable;

public class FriendRequestItem {
    String nickname;
    String uid;
    Drawable accept;
    Drawable refuse;
    public String getNickname(){
        return this.nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getUid(){
        return this.uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
    public Drawable getAccept(){
        return this.accept;
    }
    public void setAccept(Drawable accept){
        this.accept = accept;
    }
    public Drawable getRefuse(){
        return this.refuse;
    }
    public void setRefuse(Drawable refuse){
        this.refuse = refuse;
    }
}
