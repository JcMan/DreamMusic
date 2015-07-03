package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dream.app.com.dreammusic.R;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetSingerList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_singer_list,container,false);
        return view;
    }
}
