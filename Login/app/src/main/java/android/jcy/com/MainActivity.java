package android.jcy.com;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import entity.User;
import model.ResultModel;
import presenter.LoginPresenter;
import view.LoginView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoginView {
    private static final String TAG = "MainActivity";
    private EditText mName;
    private EditText mPwd;
    private ProgressDialog mDialog;
    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDialog=new ProgressDialog(this);
        mDialog.setTitle("登录");
        mDialog.setMessage("请稍等...");

        mName = (EditText) findViewById(R.id.edit_name);
        mPwd = (EditText) findViewById(R.id.edit_pwd);

        findViewById(R.id.btn_login).setOnClickListener(this);

        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        mLoginPresenter.login();
    }

    @Override
    public String getName() {
        return mName.getText().toString();
    }

    @Override
    public String getPwd() {
        return mPwd.getText().toString();
    }

    @Override
    public void showProgress() {
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        mDialog.dismiss();
    }

    @Override
    public void clearPwd() {
        mPwd.getText().clear();
    }

    @Override
    public void onSuccess(ResultModel<User> model) {
        Log.e(TAG, "onSuccess: " + model.data );
        Toast.makeText(this, model.msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String e) {

    }
}
