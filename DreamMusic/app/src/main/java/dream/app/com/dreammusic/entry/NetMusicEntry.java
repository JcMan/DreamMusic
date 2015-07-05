package dream.app.com.dreammusic.entry;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 */
public class NetMusicEntry {
    public static final String ARTIST_ID = "artist_id";
    public static final String AUTHOR = "author";
    public static final String ALBUM_ID = "album_id";
    public static final String ALBUM_TITLE = "album_title";
    public static final String SONG_ID = "song_id";
    public static final String PIC_SMALL = "pic_small";
    public static final String PIC_BIG = "pic_big";
    public static final String TITLE = "title";
    public static final String SONG_LIST = "song_list";
    public static final String CHANNELLIST = "channellist";
    public static final String NAME = "name";
    public static final String CH_NAME = "ch_name";
    public static final String THUMB = "thumb";
    public static final String TING_UID = "ting_uid";
    public static final String AVATAR_MIDDLE = "avatar_middle";
    public static final String FILE_LINK = "file_link";




    private String channellist;
    private String name;
    private String ch_name;
    private String thumb;
    private String author;
    private String album_id;
    private String album_title;
    private String song_id;
    private String pic_small;
    private String pic_big;
    private String title;
    private String artist_id;
    private String ting_uid;
    private String avatar_middle;
    private String file_link;

    public String getFile_link() {
        return file_link;
    }
    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }
    public String getTing_uid() {
        return ting_uid;
    }

    public void setTing_uid(String ting_uid) {
        this.ting_uid = ting_uid;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    public void setAvatar_middle(String avatar_middle) {
        this.avatar_middle = avatar_middle;
    }




    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }
    public String getAuthor() {
        return author;
    }
    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannellist() {
        return channellist;
    }

    public void setChannellist(String channellist) {
        this.channellist = channellist;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getString(String type){
        String s = "";
        if(type.equals(ARTIST_ID))
            s = getArtist_id();
        else if(type.equals(AUTHOR))
            s = getAuthor();
        else if(type.equals(ALBUM_ID))
            s = getAlbum_id();
        else if(type.equals(ALBUM_TITLE))
            s = getAlbum_title();
        else if(type.equals(SONG_ID))
            s = getSong_id();
        else if(type.equals(PIC_SMALL))
            s = getPic_small();
        else if(type.equals(PIC_BIG))
            s = getPic_big();
        else if(type.equals(TITLE))
            s = getTitle();
        else if(type.equals(NAME))
            s = getName();
        else if(type.equals(THUMB))
            s = getThumb();
        else if(type.equals(CH_NAME))
            s = getCh_name() ;
        else if(type.equals(TING_UID))
            s = getTing_uid();
        else if(type.equals(AVATAR_MIDDLE))
            s = getAvatar_middle();
        else if(type.equals(FILE_LINK))
            s = getFile_link();
        return s;
    }
    public static void setNetMusicEntryList(ResponseInfo<String> stringResponseInfo,List<NetMusicEntry> mList) {
        try {
            JSONObject object = new JSONObject(stringResponseInfo.result);
            JSONArray array = object.getJSONArray(NetMusicEntry.SONG_LIST);
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                try {
                    entry.setAuthor(obj.getString(NetMusicEntry.AUTHOR));
                }catch (Exception ee){}
                try {
                    entry.setTitle(obj.getString(NetMusicEntry.TITLE));
                }catch (Exception e){}
                try {
                    entry.setPic_small(obj.getString(NetMusicEntry.PIC_SMALL));
                }catch (Exception e){}
                try {
                    entry.setPic_big(obj.getString(NetMusicEntry.PIC_BIG));
                }catch (Exception e){}
                try {
                    entry.setSong_id(obj.getString(NetMusicEntry.SONG_ID));
                }catch (Exception e){}
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setNetChannelList(ResponseInfo<String> info,List<NetMusicEntry> mList){
        try {
            JSONObject object = new JSONObject(info.result);
            JSONArray array1 = object.getJSONArray("result");
            JSONObject object1 = array1.getJSONObject(0);
            JSONArray array = object1.getJSONArray(NetMusicEntry.CHANNELLIST);
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                try {
                    entry.setName(obj.getString(NetMusicEntry.NAME));
                }catch (Exception e){}
                try {
                    entry.setThumb(obj.getString(NetMusicEntry.THUMB));
                }catch (Exception e){}
                try {
                    entry.setCh_name(obj.getString(NetMusicEntry.CH_NAME));
                }catch (Exception e){}
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setNetSingerList(ResponseInfo<String> info,List<NetMusicEntry> mList){
        try {
            JSONObject object = new JSONObject(info.result);
            JSONArray array = object.getJSONArray("artist");
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                try {
                    entry.setName(obj.getString(NetMusicEntry.NAME));
                }catch (Exception e){}
                try {
                    entry.setTing_uid(obj.getString(NetMusicEntry.TING_UID));
                }catch (Exception e){}
                try {
                    entry.setAvatar_middle(obj.getString(NetMusicEntry.AVATAR_MIDDLE));
                }catch (Exception e){}
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setNetHitoMusicList(ResponseInfo<String> info,List<NetMusicEntry> mList){
        try {
            JSONObject object = new JSONObject(info.result);
            JSONArray array = object.getJSONArray(NetMusicEntry.SONG_LIST);
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                try {
                    entry.setAuthor(obj.getString(NetMusicEntry.AUTHOR));
                }catch (Exception e){}
                try {
                    entry.setTitle(obj.getString(NetMusicEntry.TITLE));
                }catch (Exception e){}
                try {
                    entry.setSong_id(obj.getString(NetMusicEntry.SONG_ID));
                }catch (Exception e){}
                try {
                    entry.setPic_small(obj.getString(NetMusicEntry.PIC_SMALL));
                }catch (Exception e){}
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getFileLink(String json){
        String file_url = "";
        try {
            JSONObject object = new JSONObject(json);
            JSONObject object1 = object.getJSONObject("songurl");
            JSONArray array = object1.getJSONArray("url");
            if(array.length()>1)
                file_url = array.getJSONObject(0).getString(NetMusicEntry.FILE_LINK);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return file_url;
    }
}
