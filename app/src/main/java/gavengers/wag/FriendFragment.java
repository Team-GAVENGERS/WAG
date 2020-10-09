package gavengers.wag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private boolean isOpen = false;
    private FriendRequestAdapter friendRequestAdapter;
    ArrayAdapter<String> list_adapter;
    ArrayAdapter<String> request_adapter;
    ListView f_list;
    ListView request_list;
    Button add_btn;
    EditText input_friend_email;
    Button friend_list;
    String own_uid;
    String results ="";
    TextView list_text;
    private ArrayList<String> uid_list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview =(ViewGroup) inflater.inflate(R.layout.friend_layout, container, false);
        own_uid = ((MenuActivity)MenuActivity.context).uid;
        context = container.getContext();
        f_list = rootview.findViewById(R.id.friend_list);
        request_list = rootview.findViewById(R.id.request_list);
        f_list.setVisibility(View.GONE);
        list_text = rootview.findViewById(R.id.list_text);
        list_text.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        friendRequestAdapter = new FriendRequestAdapter(own_uid,db);
        uid_list = new ArrayList<>();
        list_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1);
        request_adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
         final Handler handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){
                uid_list.clear();
                db.collection("UserData").document(own_uid).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("요청받은 목록 연결 실패","Error");
                            return;
                        }
                        ArrayAdapter<String> tmp = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getData().get("accept").toString().equals("false")){
                                uid_list.add(document.getId());
                                tmp.add(document.getData().get("nickname").toString()+" 님께서 친구요청 하셨습니다.");
                                //request_adapter.add(document.getData().get("nickname").
                                 //      toString()+" 님께서 친구요청 하셨습니다.");
                            }
                        }
                        if(request_adapter.getCount() != tmp.getCount()){
                            request_list.setAdapter(tmp);
                            request_adapter = tmp;
                        }
                        //request_list.setAdapter(request_adapter);
                    }
                });
            }
        };
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
            @Override
            public boolean cancel(){
                return super.cancel();
            }
        };
        timer.schedule(timerTask,1000,3000);
//        db.collection("UserData").document(own_uid).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(!task.isSuccessful()){
//                    Log.e("요청받은 목록 연결 실패","Error");
//                    return;
//                }
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    if(document.getData().get("accept").toString().equals("false")){
//                        uid_list.add(document.getId());
//                        request_adapter.add(document.getData().get("nickname").
//                                toString()+" 님께서 친구요청 하셨습니다.");
//                    }
//                }
//                request_list.setAdapter(request_adapter);
//            }
//        });
        request_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final String nicks = adapterView.getItemAtPosition(i).toString().split(" ")[0];
                final int index = i;
                new AlertDialog.Builder(context).setMessage("친구 요청")
                        .setPositiveButton("수락하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> user_data = new HashMap<>();
                                user_data.put("accept", "true");
                                user_data.put("nickname", nicks);
                                db.collection("UserData").document(own_uid).collection("friends").document(uid_list.get(index))
                                        .set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("성공","accept : true로 변경 성공");
                                        request_adapter.remove(adapterView.getItemAtPosition(index).toString());
                                        request_adapter.notifyDataSetChanged();
                                        request_list.setAdapter(request_adapter);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("거절하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
        add_btn  =rootview.findViewById(R.id.addFriend_btn);
        friend_list = rootview.findViewById(R.id.friend_list_btn);
        friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("UserData").document(own_uid).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("연결 실패","Error");
                            return;
                        }

                        if(!isOpen) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("accept").toString().equals("true")) {
                                    Log.d("Friend UID",document.getId());
                                    list_adapter.add(document.getData().get("nickname").toString());
                                }
                                //Log.d("List",document.getData().get("nickname").toString());
                            }

                            f_list.setAdapter(list_adapter);
                            f_list.setVisibility(View.VISIBLE);
                            list_text.setVisibility(View.VISIBLE);
                            isOpen = true;
                        }
                        else{
                            isOpen = false;
                            list_adapter.clear();
                            f_list.setVisibility(View.GONE);
                            list_text.setVisibility(View.GONE);
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

