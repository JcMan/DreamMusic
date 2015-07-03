package dream.app.com.dreammusic.entry;

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


    private String author;
    private String album_id;
    private String album_title;
    private String song_id;
    private String pic_small;
    private String pic_big;
    private String title;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    private String artist_id;

    public String getAuthor() {
        return author;
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
        return s;
    }






}
