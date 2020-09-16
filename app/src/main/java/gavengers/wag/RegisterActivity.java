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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            firebaseUser = mAuth.getCurrentUser();

                            String uid = firebaseUser.getUid();

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

        // [END create_user_with_email]
    }
}
