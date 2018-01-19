package presenter;


import Biz.BizImpl.LogBizImp;
import Biz.LogBiz;
import entity.User;
import model.ResultModel;
import view.LoginView;

/**
 * Created by Administrator on 2017/5/23.
 */

public class LoginPresenter {
    private LogBiz mLogBiz;
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginview) {
        this.mLoginView=loginview;
        mLogBiz=new LogBizImp();
    }
    public void login(){
        mLoginView.showProgress();
        mLogBiz.login(mLoginView.getName(), mLoginView.getPwd(), new LogBiz.OnLoginListener() {
            @Override
            public void onSuccess(ResultModel<User> model) {
                mLoginView.hideProgress();
                mLoginView.onSuccess(model);
                mLoginView.clearPwd();
            }
            @Override
            public void onFailed(String e) {
                mLoginView.hideProgress();
                mLoginView.onFailed(e);
                mLoginView.clearPwd();
            }
        });
    }
}
