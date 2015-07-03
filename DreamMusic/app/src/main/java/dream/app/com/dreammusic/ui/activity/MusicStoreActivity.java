package dream.app.com.dreammusic.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.fragment.FragmentNetHotMusic;
import dream.app.com.dreammusic.fragment.FragmentNetKtvMusic;
import dream.app.com.dreammusic.fragment.FragmentNetNewMusic;
import dream.app.com.dreammusic.fragment.FragmentNetRadioList;
import dream.app.com.dreammusic.fragment.FragmentNetSingerList;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;

/**
 * Created by JcMan on 2015/7/2.
 */
public class MusicStoreActivity extends BaseActivity {

    private DrawerLayout mLayout;
    private View mMenuView;
    private View view_newmusic,view_hotmusic,view_ktv,view_singerlist,view_radiolist;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicstore);
        initView();
        initListener();
        setTitle(gettitleFromIntent());
        getFragmentManager().beginTransaction().add(R.id.fr_music_store,new FragmentNetNewMusic()).commit();
    }

    @Override
    public void initView() {
        super.initView();
        mLayout = (DrawerLayout) findViewById(R.id.drawer_layout_music_store);
        mMenuView = findViewById(R.id.menu_music_store);
        view_newmusic = findViewById(R.id.view_menuitem_newmusic);
        view_hotmusic = findViewById(R.id.view_menuitem_hotmusic);
        view_ktv = findViewById(R.id.view_menuitem_ktvmusic);
        view_singerlist = findViewById(R.id.view_menuitem_singerlist);
        view_radiolist = findViewById(R.id.view_menuitem_radiolist);
        setTopRightToggleVisible();
    }

    @Override
    protected void initListener() {
        super.initListener();
        view_newmusic.setOnClickListener(this);
        view_hotmusic.setOnClickListener(this);
        view_ktv.setOnClickListener(this);
        view_singerlist.setOnClickListener(this);
        view_radiolist.setOnClickListener(this);

    }

    private String gettitleFromIntent() {
        String title = "";
        Intent intent = getIntent();
        title+=intent.getExtras().getString(ActivityUtil.TITLE);
        return title;
    }

    @Override
    protected void clickOnToggle() {
        super.clickOnToggle();
        toggleMenu();
    }

    private void toggleMenu() {
        boolean isOpen = mLayout.isDrawerOpen(mMenuView);
        if (!isOpen)
            mLayout.openDrawer(mMenuView);
        else
            mLayout.closeDrawer(mMenuView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        FragmentManager manager = getFragmentManager();
        switch (v.getId()){
            case R.id.view_menuitem_newmusic:
                manager.beginTransaction().add(R.id.fr_music_store,new FragmentNetNewMusic()).commit();
                break;
            case R.id.view_menuitem_hotmusic:
                manager.beginTransaction().add(R.id.fr_music_store,new FragmentNetHotMusic()).commit();
                break;
            case R.id.view_menuitem_ktvmusic:
                manager.beginTransaction().add(R.id.fr_music_store, new FragmentNetKtvMusic()).commit();
                break;
            case R.id.view_menuitem_singerlist:
                manager.beginTransaction().add(R.id.fr_music_store,new FragmentNetSingerList()).commit();
                break;
            case R.id.view_menuitem_radiolist:
                manager.beginTransaction().add(R.id.fr_music_store, new FragmentNetRadioList()).commit();
                break;
        }
        toggleMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }
}
