package com.jcy.capacity.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jcy.capacity.R;
import com.jcy.capacity.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_forget_password;
    private EditText et_email;

    private Button btn_update_password;

    private EditText et_now;
    private EditText et_new;
    private EditText et_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }
    private void initView(){
        btn_forget_password= (Button) findViewById(R.id.btn_forget_password);
        et_email= (EditText) findViewById(R.id.et_email);

        btn_forget_password.setOnClickListener(this);

        et_now= (EditText) findViewById(R.id.et_now);
        et_new= (EditText) findViewById(R.id.et_new);
        et_new_password= (EditText) findViewById(R.id.et_new_password);

        btn_update_password= (Button) findViewById(R.id.btn_update_password);

        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:
                final String email=et_email.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    MyUser.requestEmailVerify(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
                                Toast.makeText(ForgetActivity.this,
                                        "邮箱已发送至"+email, Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ForgetActivity.this,
                                        "邮箱发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update_password:
                String nowPwd=et_now.getText().toString().trim();
                String newPwd=et_new.getText().toString().trim();
                String newPassword=et_new_password.getText().toString().trim();
                if(TextUtils.isEmpty(nowPwd)&&TextUtils.isEmpty(newPwd)&&TextUtils.isEmpty(newPassword)){
                    if(newPwd.equals(newPassword)){
                        MyUser.updateCurrentUserPassword(nowPwd, newPwd, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(ForgetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ForgetActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this, "两次输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
