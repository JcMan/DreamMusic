package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import dream.app.com.dreammusic.*;

import java.util.List;

import dream.app.com.dreammusic.entry.MessageEntry;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by JcMan on 2015/7/7.
 */
public class MessageAdapter extends BaseAdapter {

    private List<MessageEntry> list;
    private Context mContext;
    private RelativeLayout layout;

    public MessageAdapter(Context context,List<MessageEntry> list){
        mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        MessageEntry data = (MessageEntry)list.get(position);
        //自己发送的
        if(data.getType()==MessageEntry.TYPE_ME){
            layout = (RelativeLayout) inflater.inflate(R.layout.rightitem,null);
            ImageView view = (ImageView) layout.findViewById(R.id.iv_right);
            FinalBitmap finalBitmap = FinalBitmap.create(mContext);
            String headimageurl = SharedPreferencesUtil.getHeadImageUrl();
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boy);
            finalBitmap.display(view,headimageurl,bitmap,bitmap);
        }
        //机器人发送的
        else{
            layout = (RelativeLayout) inflater.inflate(R.layout.leftitem,null);
        }
        TextView tv = (TextView) layout.findViewById(R.id.tv);
        tv.setText(data.getContent());
        return layout;
    }
}
