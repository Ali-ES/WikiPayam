package ir.FiveMFive.FiveMFive;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("user_credit.php")
    Call<ResponseBody> login(@Query("username") String user, @Query("password") String pass);

}
