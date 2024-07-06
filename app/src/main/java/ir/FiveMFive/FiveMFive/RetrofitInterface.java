package ir.FiveMFive.FiveMFive;

import java.util.List;

import ir.FiveMFive.FiveMFive.Java.Group;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("user_credit.php")
    Call<ResponseBody> login(@Query("username") String user, @Query("password") String pass);
    @GET("send_sms.php")
    Call<ResponseBody> sendSingleMessage(@Query("username") String user,
                             @Query("password") String pass,
                             @Query("sender_number") String senderNumber,
                             @Query("receiver_number") String receiverNumber,
                             @Query("note") String message);
    @GET("sms_group_list.php")
    Call<List<Group>> getGroupsList(@Query("username") String user,
                                    @Query("password") String pass);
    @GET("sms_send_group.php")
    Call<ResponseBody> sendGroupMessage(@Query("username") String user,
                                        @Query("password") String pass,
                                        @Query("sender_number") String senderNumber,
                                        @Query("note") String message,
                                        @Query("categories[]") List<String> receivers,
                                        @Query("blacklist") boolean shouldSendToBlackList);
}
