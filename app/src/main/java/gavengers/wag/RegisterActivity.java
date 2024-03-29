package gavengers.wag;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private Button do_checkNick;
    private Button do_register;
    boolean nicknameCheck = false;
    String checkedNick = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reg_input_email);
        nickName = findViewById(R.id.reg_input_nickname);
        do_checkNick = findViewById(R.id.do_checkNick);
        do_checkNick.setEnabled(false);
        nickName.addTextChangedListener(textWatcher);
        password = findViewById(R.id.reg_input_pw);
        password.setEnabled(false);
        passwordChk = findViewById(R.id.reg_input_pw_again);
        passwordChk.setEnabled(false);
        passwordChk.addTextChangedListener(textWatcher2);

        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        do_register = findViewById(R.id.do_register);
        do_register.setEnabled(false);
        do_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp(email.getText().toString(),password.getText().toString(),nickName.getText().toString(),passwordChk.getText().toString());
            }
        });
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() > 2){
                do_checkNick.setEnabled(true);
            }
            else{
                do_checkNick.setEnabled(false);
            }
        }
    };
    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() >= 8){
                do_register.setEnabled(true);
            }
            else{
                do_register.setEnabled(false);
            }
        }
    };
    //Button Callback Method
    public void checkNickname(View v){
        final String nick = nickName.getText().toString();
        //Retrofit nickname check
        final Model__nickname__CheckAlready modelCheckAlready = new Model__nickname__CheckAlready(nick);

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
                    Toast.makeText(getApplicationContext(), "사용 가능한 닉네임입니다.",Toast.LENGTH_SHORT).show();
                    nickName.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                    nickName.setTextColor(Color.GREEN);
                    password.setEnabled(true);
                    passwordChk.setEnabled(true);
                    checkedNick = nick;
                    nicknameCheck = true;
                } else {
                    Log.d("중복검사: ", "중복된 닉네임입니다.");
                    Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다.",Toast.LENGTH_SHORT).show();
                    nickName.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    nickName.setTextColor(Color.RED);
                    nicknameCheck = false;
                    checkedNick = null;
                }
            }
            @Override
            public void onFailure(Call<Model__nickname__CheckAlready> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
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

        if(!checkedNick.equals(nickname) || !nicknameCheck){
            Toast.makeText(getApplicationContext(), "닉네임을 확인해주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // 로그인 성공
                            firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid(); //UID값
                            Map<String, Object> user = new HashMap<>();
                            user.put("uid", uid);
                            user.put("nickname", nickname);
                                db.collection("user").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // success
                                        Log.d("user/UID", "user successfully written!");
                                        Toast.makeText(getApplicationContext(), "회원가입 성공",
                                                Toast.LENGTH_SHORT).show();

                                        firebaseUser.getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            String idToken = task.getResult().getToken();
                                                            Log.d("user/Token", idToken);
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
                                                Log.w("user/UID", "userv written failed", e);
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
