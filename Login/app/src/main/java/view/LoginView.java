package view;

import entity.User;
import model.ResultModel;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface LoginView {
    String getName();

    String getPwd();

    void showProgress();

    void hideProgress();

    void clearPwd();

    void onSuccess(ResultModel<User> model);

    void onFailed(String e);
}
