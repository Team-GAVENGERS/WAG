package gavengers.wag;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("usernameExists")
    Call<Model__nickname__CheckAlready> postOverlapCheck(@Body Model__nickname__CheckAlready modelCheckAlready); //  (*데이터를 보낼때)

    //@FormUrlEncoded
    //@POST("/auth/overlapChecker")
    //Call<Model__nickname__CheckAlready> postOverlapCheck(@Field("phone") String phoneNum, @Field("message") String message); //이건 요청시 사용하는거 (*데이터를 보낼때)
}