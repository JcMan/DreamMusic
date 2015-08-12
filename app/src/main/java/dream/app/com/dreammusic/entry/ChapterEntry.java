package dream.app.com.dreammusic.entry;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ChapterEntry {

    private String mChapterName;
    private String mChapterUrl;

    public ChapterEntry(String mChapterName, String mChapterUrl) {
        this.mChapterName = mChapterName;
        this.mChapterUrl = mChapterUrl;
    }

    @Override
    public String toString() {
        return "ChapterEntry{" +
                "mChapterName='" + mChapterName + '\'' +
                ", mChapterUrl='" + mChapterUrl + '\'' +
                '}';
    }

    public String getmChapterName() {
        return mChapterName;
    }

    public void setmChapterName(String mChapterName) {
        this.mChapterName = mChapterName;
    }

    public String getmChapterUrl() {
        return mChapterUrl;
    }

    public void setmChapterUrl(String mChapterUrl) {
        this.mChapterUrl = mChapterUrl;
    }
}
