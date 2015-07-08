package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MessageAdapter;
import dream.app.com.dreammusic.entry.MessageEntry;
import dream.app.com.dreammusic.entry.TuLingApiEntry;
import dream.app.com.dreammusic.util.MyHttpUtil;

/**
 * Created by Administrator on 2015/7/8.
 */
public class MessageActivity extends BaseActivity{

    private ListView mListView;
    private EditText mEdit;
    private Button mBtn;
    private List<MessageEntry> mList;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVariable();
        initView();
        initListener();
        initData();
        setTitle("小梦");
    }

    private void initVariable() {
        mList = new ArrayList<MessageEntry>();
    }

    private void initData() {
        MessageEntry entry = new MessageEntry();
        entry.setType(MessageEntry.TYPE_MENG);
        entry.setContent("你好，我是小梦，有空和我聊聊吧,^_^!");
        mList.add(entry);
        adapter = new MessageAdapter(this,mList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.listView_activity_message);
        mEdit = (EditText) findViewById(R.id.edit_activity_message);
        mBtn = (Button) findViewById(R.id.btn_send_activity_message);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_send_activity_message:
                String content = mEdit.getText().toString().replaceAll(" ", "");
                send(content);
                addMessage(content);
                break;
        }
    }

    private void addMessage(String content) {
        MessageEntry entry = new MessageEntry(MessageEntry.TYPE_ME,content);
        mList.add(entry);
        adapter.notifyDataSetChanged();
        if(mListView.getCount()-1>0)
            mListView.setSelection(mListView.getCount()-1);
        mEdit.setText("");
    }

    private void send(String info) {
        MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET, TuLingApiEntry.getTuLingUrl(info), new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo){
                String content = getContent(stringResponseInfo.result);
                if(content!=""){
                    MessageEntry entry = new MessageEntry(MessageEntry.TYPE_MENG,content);
                    mList.add(entry);
                    adapter.notifyDataSetChanged();
                    if(mListView.getCount()-1>0)
                        mListView.setSelection(mListView.getCount()-1);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private String getContent(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String text = jsonObject.getString("text");
            return text;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
