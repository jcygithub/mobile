package com.jcy.capacity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jcy.capacity.MainActivity;
import com.jcy.capacity.R;
import com.jcy.capacity.entity.MyUser;
import com.jcy.capacity.utils.ShareUtils;
import com.jcy.capacity.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //注册
    private TextView user_create;
    //登录
    private EditText user_name;
    private EditText user_pwd;
    private Button  user_log;
    private CheckBox keep_password;
    private CustomDialog mDialog;

    private TextView forget_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initView();
    }

    private void initView() {
        user_create= (TextView) findViewById(R.id.user_create);
        user_create.setOnClickListener(this);
        user_name= (EditText) findViewById(R.id.user_name);
        user_pwd= (EditText) findViewById(R.id.user_pwd);
        user_log= (Button) findViewById(R.id.user_log);
        user_log.setOnClickListener(this);

        keep_password= (CheckBox) findViewById(R.id.keep_pwd);

        forget_pwd= (TextView) findViewById(R.id.forget_pwd);
        forget_pwd.setOnClickListener(this);

        //记住密码
        boolean isKeep = ShareUtils.getBoolean(this, "keeppass", false);
        keep_password.setChecked(isKeep);

        mDialog = new CustomDialog(this, 100, 100, R.layout.layout_dialog, R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        mDialog.setCancelable(false);

        if(isKeep){
            //设置密码
            String name = ShareUtils.getString(this, "name", "");
            String password = ShareUtils.getString(this, "password", "");
            user_name.setText(name);
            user_pwd.setText(password);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_create:
                startActivity(new Intent(this,RegisteredActivity.class));
            break;

            case R.id.forget_pwd:
                startActivity(new Intent(this,ForgetActivity.class));
                break;

            case R.id.user_log:
                String username=user_name.getText().toString().trim();
                String userpwd=user_pwd.getText().toString().trim();
                if(!TextUtils.isEmpty(username)&& !TextUtils.isEmpty(userpwd)){
                    mDialog.show();
                    MyUser myUser=new MyUser();
                    myUser.setUsername(username);
                    myUser.setPassword(userpwd);
                    myUser.login(new SaveListener<MyUser>() {

                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e==null){
                                mDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "keeppass", keep_password.isChecked());

        //是否记住密码
        if (keep_password.isChecked()) {
            //记住用户名和密码
            ShareUtils.putString(this, "name", user_name.getText().toString().trim());
            ShareUtils.putString(this, "password", user_pwd.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }
}
