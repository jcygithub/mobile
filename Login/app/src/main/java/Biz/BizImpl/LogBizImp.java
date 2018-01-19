package Biz.BizImpl;

import android.jcy.com.ApiService;
import android.util.Log;

import Biz.LogBiz;
import entity.User;
import model.ResultModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/23.
 */
public class LogBizImp implements LogBiz {
    private static final String TAG = "LogBizImp";
    private ApiService mService;

    public LogBizImp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(ApiService.class);

    }

    @Override
    public void login(String name, String pwd, final OnLoginListener onLoginListener) {
        Call<ResultModel<User>> call=mService.login(name,pwd);
        call.enqueue(new Callback<ResultModel<User>>() {
                @Override
                public void onResponse(Call<ResultModel<User>> call, Response<ResultModel<User>> response) {
                    if (response.isSuccessful()){
                        ResultModel<User> model=response.body();
                        onLoginListener.onSuccess(model);
                    }else {
                        Log.e(TAG, "onResponse: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResultModel<User>> call, Throwable throwable) {
                    onLoginListener.onFailed(throwable.getMessage());
                }
            });
    }
}
