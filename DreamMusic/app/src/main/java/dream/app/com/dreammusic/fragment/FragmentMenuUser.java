package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import net.tsz.afinal.FinalBitmap;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.ui.view.CircleView;
import dream.app.com.dreammusic.util.Util;

/**
 * Created by Administrator on 2015/6/27.
 */
public class FragmentMenuUser extends Fragment {
    @Nullable
    private String name;
    private String headimageurl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login_user,container,false);
        TextView nameView = (TextView) view.findViewById(R.id.tv_username);
        CircleView circleView = (CircleView) view.findViewById(R.id.circle_headimage);
        nameView.setText(name);
//        FinalBitmap finalBitmap = FinalBitmap.create(getActivity());

//        finalBitmap.display(circleView,headimageurl);
        BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
        bitmapUtils.display(circleView,headimageurl, Util.getConfig(getActivity(),R.drawable.boy));
        return view;
    }
    public  FragmentMenuUser(String name,String headimageurl){
        this.name = name;
        this.headimageurl = headimageurl;
    }
}
