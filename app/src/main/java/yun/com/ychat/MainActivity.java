package yun.com.ychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yun.com.ychat.adapter.ChatListAdapter;
import yun.com.ychat.entity.ChatData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_chat_list;
    private EditText ed_send;
    private Button btn_send;
    private List<ChatData> mList = new ArrayList<>();
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();

    }

    private void initView() {
        lv_chat_list = (ListView) findViewById(R.id.lv_chat_list);
        ed_send = (EditText) findViewById(R.id.ed_send);
        btn_send = (Button) findViewById(R.id.btn_send);
        lv_chat_list.setDivider(null);

        //设置适配器
        adapter = new ChatListAdapter(this,mList);
        lv_chat_list.setAdapter(adapter);

        //设置发送按钮监听
        btn_send.setOnClickListener(this);

        //设置欢迎语
        //addlefttext("你好呀！");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                //获取输入框内容
                String message = ed_send.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(message)){
                    //清空当前输入框
                    ed_send.setText("");
                    //添加输入的内容到right item
                    addrighttext(message);
                    //定义URL
                    //图灵机器人接口地址：http://www.tuling123.com/openapi/api
                    //key=后接在图灵官网申请到的apikey
                    //info后接输入的内容
                    if( message.equals("@") ){
                        message = "你好";
                    };

                    String url ="http://www.tuling123.com/openapi/api?"+
                            "key="+"3e4f8d6a4a484a46b34330e8693f7f9b"+"&info="+message;
                    //请求RxVolley将信息发出（添加RxVolley依赖，
                    // 在app的build.gradle的ependencies中添加compile 'com.kymjs.rxvolley:rxvolley:1.1.4'）
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //解析返回的JSON数据
                            pasingJson(t);
                        }
                    });

                }else{
                    return;
                }
                break;
        }
    }
    //解析返回的JSON数据
    private void pasingJson(String message){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            //通过key（text）转换成Json获取返回值
            String text = jsonObject.getString("text");
            //添加在左边
            addlefttext(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加右侧消息
    private void addrighttext(String message) {
        ChatData data = new ChatData();
        data.setType(ChatListAdapter.chat_right);
        data.setText(message);
        mList.add(data);
        //通知adapter刷新页面
        adapter.notifyDataSetChanged();
        //滚动到底部
        lv_chat_list.setSelection(lv_chat_list.getBottom());

    }

    //添加左侧消息
    private void addlefttext(String message) {
        ChatData data = new ChatData();
        data.setType(ChatListAdapter.chat_left);
        data.setText(message);
        mList.add(data);
        adapter.notifyDataSetChanged();
        lv_chat_list.setSelection(lv_chat_list.getBottom());

    }
}
