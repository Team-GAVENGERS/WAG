package gavengers.wag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String mytoken ="base";
    private Context context;
    Button add_btn;
    EditText input_friend_email;
    Button friend_list;
    String own_uid;
    String results ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview =(ViewGroup) inflater.inflate(R.layout.friend_layout, container, false);
        own_uid = ((MenuActivity)MenuActivity.context).uid;
        context = container.getContext();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
        add_btn  =rootview.findViewById(R.id.addFriend_btn);
        friend_list = rootview.findViewById(R.id.friend_list_btn);
        friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이거 땜에 firebase cloud firestore rule 수정함
                db.collection("UserData").document(own_uid).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("연결 실패","Error");
                            return;
                        }

                        for(QueryDocumentSnapshot document: task.getResult()){
                            db.collection("UserData").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                    if(!task2.isSuccessful()){
                                        Log.e("친구 연결 실패",task2.getException().getMessage());
                                        return;
                                    }
                                    DocumentSnapshot doc = task2.getResult();
                                    if(doc.exists()){
                                        // getData()는 Map<String,Object> 리턴함 필드 여러개면 get(필드명)으로 하는게 나을듯
                                        Log.d("Friend Data(Nickname)",doc.getData().get("Nickname").toString());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        //add_btn.setEnabled(false);
        input_friend_email = rootview.findViewById(R.id.input_friend_email);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mytoken = ((MainActivity)MainActivity.context_main).token;
                Log.d("토큰값",mytoken);
                Log.d("닉네임",input_friend_email.getText().toString());
                Model__friend__addFriend modelAddFriend = new Model__friend__addFriend(mytoken,input_friend_email.getText().toString());

                Call<Model__friend__addFriend> call = RetrofitClient.getApiService().addFriends(modelAddFriend);

                call.enqueue(new Callback<Model__friend__addFriend>() {
                    @Override
                    public void onResponse(Call<Model__friend__addFriend> call, Response<Model__friend__addFriend> response) {
                        if(!response.isSuccessful()){
                            Log.e("연결 비정상적","Error: "+response.message()+" "+response.code());
                            return;
                        }
                        assert response.body() != null;
                        Log.d("연결 성공적",response.body().toString());

                        Toast.makeText(context, input_friend_email.getText().toString()+" 친구추가 요청 되었습니다.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Model__friend__addFriend> call, Throwable t) {
                        Log.e("연결 실패",t.getMessage());
                    }
                });

            }
        });
        return rootview;
    }
}
