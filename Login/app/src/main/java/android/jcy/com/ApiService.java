package android.jcy.com;

import entity.User;
import model.ResultModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface ApiService {


    String BASE_URL = "http://192.168.100.51:8080/Server/";


    @POST("api/LoginServlet")
    @FormUrlEncoded
    Call<ResultModel<User>> login(@Field("name") String name, @Field("pwd") String pwd);

}
