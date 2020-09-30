package gavengers.wag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    Button add_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview =(ViewGroup) inflater.inflate(R.layout.friend_layout, container, false);
        add_btn  =rootview.findViewById(R.id.addFriend_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model__friend__addFreind modelAddfriend = new Model__friend__addFreind();
                Call<Model__friend__addFreind> call = RetrofitClient.getApiService().addFreinds(modelAddfriend);
                call.enqueue(new Callback<Model__friend__addFreind>() {
                    @Override
                    public void onResponse(Call<Model__friend__addFreind> call, Response<Model__friend__addFreind> response) {

                    }

                    @Override
                    public void onFailure(Call<Model__friend__addFreind> call, Throwable t) {

                    }
                });
            }
        });
        return rootview;
    }

}
