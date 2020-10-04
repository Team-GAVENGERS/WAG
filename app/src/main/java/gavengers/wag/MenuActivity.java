package gavengers.wag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private PlanFragment planFragment;
    private MapFragment mapFragment;
    private FriendFragment friendFragment;
    private SettingFragment settingFragment;
    private FragmentTransaction transaction;
    public String uid;
    public String email;
    public static Context context;
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        uid = getIntent().getStringExtra("UID");
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        fragmentManager = getSupportFragmentManager();
        planFragment = new PlanFragment();
        mapFragment = new MapFragment();
        friendFragment = new FriendFragment();
        settingFragment = new SettingFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, mapFragment).commitAllowingStateLoss();

    }
    @Override
    public void onBackPressed(){
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
    public void clickMenu (View view){

        transaction = fragmentManager.beginTransaction();

        switch(view.getId()){
            case R.id.planBtn:
                transaction.replace(R.id.frameLayout, planFragment).commitAllowingStateLoss();
                break;
            case R.id.mapBtn:
                transaction.replace(R.id.frameLayout, mapFragment).commitAllowingStateLoss();
                break;
            case R.id.friendBtn:
                Log.d("메뉴 UID",uid);
                transaction.replace(R.id.frameLayout, friendFragment).commitAllowingStateLoss();
                break;
            case R.id.settingBtn:
                transaction.replace(R.id.frameLayout, settingFragment).commitAllowingStateLoss();
                break;
        }
    }
}
