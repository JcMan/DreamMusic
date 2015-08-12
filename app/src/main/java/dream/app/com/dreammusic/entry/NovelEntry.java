package dream.app.com.dreammusic.entry;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelEntry {

    private String mBookName;
    private String mAuthor;
    private String mBookUrl;
    private String mImgUrl;
    private String mIntroduction;

    private String mMainPageUrl;
    private String mBaseUrl;
    private int mLastChapter;

    public String getmBaseUrl() {
        return mBaseUrl;
    }

    public void setmBaseUrl(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }



    public String getmMainPageUrl() {
        return mMainPageUrl;
    }

    public void setmMainPageUrl(String mMainPageUrl) {
        this.mMainPageUrl = mMainPageUrl;
    }

    public int getmLastChapter() {
        return mLastChapter;
    }

    public void setmLastChapter(int mLastChapter) {
        this.mLastChapter = mLastChapter;
    }



    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmBookUrl() {
        return mBookUrl;
    }

    public void setmBookUrl(String mBookUrl) {
        this.mBookUrl = mBookUrl;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public String getmIntroduction() {
        return mIntroduction;
    }

    public void setmIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
    }

    @Override
    public String toString(){
        return "NovelEntry{" +
                "mBookName='" + mBookName + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mBookUrl='" + mBookUrl + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                ", mIntroduction='" + mIntroduction + '\'' +
                ", mMainPageUrl='" + mMainPageUrl + '\'' +
                ", mBaseUrl='" + mBaseUrl + '\'' +
                ", mLastChapter=" + mLastChapter +
                '}';
    }
}
