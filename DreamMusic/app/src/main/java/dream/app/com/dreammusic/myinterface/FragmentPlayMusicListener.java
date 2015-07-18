package dream.app.com.dreammusic.myinterface;

import java.util.List;

import dream.app.com.dreammusic.model.Music;

/**
 * Created by Administrator on 2015/7/18.
 */
public interface FragmentPlayMusicListener {

    public void onPlay(int position);

    public void onUpdateMusicList(List<Music> list,int type);
}
