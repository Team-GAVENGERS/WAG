package gavengers.wag;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class FriendRequestAdapter extends BaseAdapter {
    String uid;
    FirebaseFirestore db;
    String cur_pos_uid;
    public FriendRequestAdapter(String uid, FirebaseFirestore newdb){
        this.uid = uid;
        this.db = newdb;
    }
    private ArrayList<FriendRequestItem> friendRequestItems = new ArrayList<>();
    @Override
    public int getCount(){
        return friendRequestItems.size();
    }
    @Override
    public FriendRequestItem getItem(int position){
        return friendRequestItems.get(position);
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_request_custom_listview,parent,false);
        }

        TextView nickname = convertView.findViewById(R.id.nick_explain);
        TextView uid = convertView.findViewById(R.id.uid_text);
        ImageButton accept_btn = convertView.findViewById(R.id.accept_btn);
        ImageButton refuse_btn = convertView.findViewById(R.id.refuse_btn);
        final FriendRequestItem myItem = friendRequestItems.get(position);

        nickname.setText(myItem.getNickname()+" 님께서 친구요청 하셨습니다.");
        accept_btn.setImageDrawable(myItem.getAccept());
        refuse_btn.setImageDrawable(myItem.getRefuse());

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String, Object> user = new HashMap<>();
//                user.put("accept", "true");
//                user.put("nickname", myItem.getNickname());
//                db.collection("UserData").document(uid).collection("friends").document(cur_pos_uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("성공","설정 성공");
//                    }
//                });
            }

        });
        refuse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
    public void addItem(String nick,Drawable a,Drawable r){
        FriendRequestItem items = new FriendRequestItem();
        items.setNickname(nick);
        items.setAccept(a);
        items.setRefuse(r);
        friendRequestItems.add(items);

    }
    public void setItem(int pos,String nick,Drawable a,Drawable r){
        FriendRequestItem item = new FriendRequestItem();
        item.setNickname(nick);
        item.setAccept(a);
        item.setRefuse(r);
        friendRequestItems.set(pos,item);
    }
    public void removeItem(int pos){
        friendRequestItems.remove(pos);
    }
    public void clearItem(){
        friendRequestItems.clear();
    }
}
