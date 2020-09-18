package gavengers.wag;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {
    public boolean callNicknamecheck(String nickname){
        boolean isRight = false;

        //Retrofit 호출
        final Model__nickname__CheckAlready modelCheckAlready = new Model__nickname__CheckAlready(nickname);
        Call<Model__nickname__CheckAlready> call = RetrofitClient.getApiService().postOverlapCheck(modelCheckAlready);
        call.enqueue(new Callback<Model__nickname__CheckAlready>() {
            @Override
            public void onResponse(Call<Model__nickname__CheckAlready> call, Response<Model__nickname__CheckAlready> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                Model__nickname__CheckAlready checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                if(modelCheckAlready.getResult() == "False"){
                    Log.d("중복검사: ", "중복된 닉네임이 아닙니다");
                    modelCheckAlready.setRight(true);
                }
            }
            @Override
            public void onFailure(Call<Model__nickname__CheckAlready> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        return modelCheckAlready.isRight();
    }
}