package com.jcy.capacity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jcy.capacity.R;
import com.jcy.capacity.adapter.ChatListAdapter;
import com.jcy.capacity.entity.ChatListData;
import com.jcy.capacity.utils.L;
import com.jcy.capacity.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {
    private ListView mChatListView;
    private List<ChatListData> mList = new ArrayList<>();
    private ChatListAdapter adapter;
    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {
        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        et_text = (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        //设置适配器
        adapter = new ChatListAdapter(getActivity(), mList);
        mChatListView.setAdapter(adapter);

        addLeftItem("你好，我是小优");
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                /**
                 * 逻辑
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.判断长度不能大于30
                 * 4.清空当前的输入框
                 * 5.添加你输入的内容到right item
                 * 6.发送给机器人请求返回内容
                 * 7.拿到机器人的返回值之后添加在left item
                 */

                //1.获取输入框的内容
                String text = et_text.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(text)) {
                    //3.判断长度不能大于30
                    if (text.length() > 30) {
                        Toast.makeText(getActivity(), "输入长度超过限制", Toast.LENGTH_SHORT).show();
                    } else {
                        //4.清空当前的输入框
                        et_text.setText("");
                        //5.添加你输入的内容到right item
                        addRightItem(text);
                        //6.发送给机器人请求返回内容
                        String url = "http://op.juhe.cn/robot/index?info=" + text
                                + "&key=" + StaticClass.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                Toast.makeText(getActivity(), "Json:" + t, Toast.LENGTH_SHORT).show();
                                L.i("Json" + t);
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObhect = new JSONObject(t);
            JSONObject jsonresult = jsonObhect.getJSONObject("result");
            //拿到返回值
             //String text=jsonresult.getString("text");
            //7.拿到机器人的返回值之后添加在left item
            addLeftItem("5211314.");
            addLeftItem(jsonresult.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {
        ChatListData date = new ChatListData();
        date.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text) {

        ChatListData date = new ChatListData();
        date.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
}


