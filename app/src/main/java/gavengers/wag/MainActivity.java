package gavengers.wag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainActivity extends AppCompatActivity {
    private Button register_btn;
    private Button do_login;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    public String token;
    public static Context context_main;
    private CheckBox auto_login;
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        context_main = this;
        mAuth = FirebaseAuth.getInstance();
        do_login = findViewById(R.id.do_login);
        email = findViewById(R.id.input_id);
        password = findViewById(R.id.input_pw);
        auto_login = findViewById(R.id.auto_login_chkbox);
        setting = getSharedPreferences("setting",0);
        if(setting.getBoolean("Enabled",false)){
            email.setText(setting.getString("email",""));
            password.setText(setting.getString("pw",""));
            auto_login.setChecked(true);
        }
        editor = setting.edit();
        do_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn(email.getText().toString(),password.getText().toString());
            }
        });
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    String email_ = email.getText().toString();
                    String password_ = password.getText().toString();
                    editor.putString("email",email_);
                    editor.putString("pw",password_);
                    editor.putBoolean("Enabled",true);
                    editor.commit();
                }
                else{
                    editor.remove("email");
                    editor.remove("pw");
                    editor.remove("Enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });
        register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    public void changeToTest(View v){
       Intent testIntent =new Intent(getApplicationContext(),PlanActivity.class);
        startActivity(testIntent);
    }*/

    public void showToast(String str){
        Toast.makeText(this.getApplicationContext(),str, Toast.LENGTH_SHORT).show();
    }
    private void SignIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 성공
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseUser = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Success",
                                    Toast.LENGTH_SHORT).show();
                            firebaseUser.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if(task.isSuccessful()){
                                                token = task.getResult().getToken();
                                                Log.d("Token",token);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                            Intent intent = new Intent(getApplicationContext(),MenuActivity.class); // 액티비티 만들면 그 class로 연결 -> 완료
                            startActivity(intent);

                        } else {
                            // 실패
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
