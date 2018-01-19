package Biz;

import entity.User;
import model.ResultModel;

/**
 * Created by Administrator on 2017/5/23.
 */
public interface LogBiz {
    //登录的方法
    void login(String name, String pwd, LogBiz.OnLoginListener onLoginListener);
    interface OnLoginListener {
        //服务器正常相应
        void onSuccess(ResultModel<User> model);

        //服务器挂了
        void onFailed(String e);
    }
}
