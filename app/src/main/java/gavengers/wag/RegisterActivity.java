package gavengers.wag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private EditText email;
    private EditText nickName;
    private EditText password;
    private EditText passwordChk;
    private Button go_back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reg_input_email);
        nickName = findViewById(R.id.reg_input_nickname);
        password = findViewById(R.id.reg_input_pw);
        passwordChk = findViewById(R.id.reg_input_pw_again);
        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        Button do_register = findViewById(R.id.do_register);
        do_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp(email.getText().toString(),password.getText().toString(),nickName.getText().toString(),passwordChk.getText().toString());
            }
        });
    }
    private void SignUp(String email, final String password, final String nickname,final String pwChk) {
        // [START create_user_with_email]
        if(!password.equals(pwChk)){
            Toast.makeText(getApplicationContext(), "비밀번호가 같지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            passwordChk.setText("");
            return;
        }

        //Retrofit nickname check
        final Model__nickname__CheckAlready modelCheckAlready = new Model__nickname__CheckAlready(nickname);
        Call<Model__nickname__CheckAlready> call = RetrofitClient.getApiService().nicknameCheck(modelCheckAlready);
        call.enqueue(new Callback<Model__nickname__CheckAlready>() {
            @Override
            public void onResponse(Call<Model__nickname__CheckAlready> call, Response<Model__nickname__CheckAlready> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.message());
                    return;
                }
                Model__nickname__CheckAlready checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                if(checkAlready.getResult().equals("False")){
                    Log.d("중복검사: ", "중복된 닉네임이 아닙니다");
                }
            }
            @Override
            public void onFailure(Call<Model__nickname__CheckAlready> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // 로그인 성공
                            firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid(); //UID값

                            Map<String, Object> user = new HashMap<>();
                            user.put("UID", uid);
                            user.put("Nickname", nickname);
                                db.collection("UserData").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // success
                                        Log.d("UserData/UID", "UserData successfully written!");
                                        Toast.makeText(getApplicationContext(), "회원가입 성공",
                                                Toast.LENGTH_SHORT).show();
                                        firebaseUser.getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            String idToken = task.getResult().getToken();
                                                            Log.d("UserData/Token", idToken);
                                                        } else {
                                                            // Handle error -> task.getException();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //fail
                                                Log.w("UserData/UID", "UserData written failed", e);
                                            }
                                        });

                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mAuth.signOut();

        // [END create_user_with_email]
    }



}
