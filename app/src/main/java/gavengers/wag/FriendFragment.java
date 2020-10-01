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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview =(ViewGroup) inflater.inflate(R.layout.friend_layout, container, false);
        context = container.getContext();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        add_btn  =rootview.findViewById(R.id.addFriend_btn);
        //add_btn.setEnabled(false);
        input_friend_email = rootview.findViewById(R.id.input_friend_email);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mytoken = ((MainActivity)MainActivity.context_main).token;
                Log.e("토큰값",mytoken);
                Model__friend__addFreind modelAddfriend = new Model__friend__addFreind(mytoken,input_friend_email.getText().toString());

                Call<Model__friend__addFreind> call = RetrofitClient.getApiService().addFriends(modelAddfriend);

                call.enqueue(new Callback<Model__friend__addFreind>() {
                    @Override
                    public void onResponse(Call<Model__friend__addFreind> call, Response<Model__friend__addFreind> response) {
                        if(!response.isSuccessful()){
                            Log.e("연결 비정상적","Error: "+response.message()+" "+response.code());
                            return;
                        }
                        assert response.body() != null;
                        Log.d("연결 성공적",response.body().toString());
                        Toast.makeText(context, input_friend_email.getText().toString()+" 친구추가 요청 되었습니다.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Model__friend__addFreind> call, Throwable t) {
                        Log.e("연결 실패",t.getMessage());
                    }
                });
            }
        });
        return rootview;
    }

}
