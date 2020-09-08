package gavengers.wag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToast("상연이는 음탕해~");
        showToast("싼쌈도 음탕해");
        TextView text = findViewById(R.id.text1);
        text.setText("팤태현!팤태현!팤태현!팤태현!팤태현!팤태현!팤태현!");
    }
    public void showToast(String str){
        Toast.makeText(this.getApplicationContext(),str, Toast.LENGTH_SHORT).show();
    }
}
