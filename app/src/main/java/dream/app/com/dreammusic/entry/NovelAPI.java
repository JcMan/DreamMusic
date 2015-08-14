package dream.app.com.dreammusic.entry;

import android.content.Context;

import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.SocketHandler;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.util.StringUtil;

/**
 * Created by Administrator on 2015/8/9.
 */
public class NovelAPI {

    public static String getClassificationUrl(Context context,String label){
        String[] kind_url = context.getResources().getStringArray(R.array.novel_kind_url);
        String[] kind_name = context.getResources().getStringArray(R.array.novel_kind_name);
        for (int i = 0; i <kind_name.length ; i++) {
            if(kind_name[i].equals(label))
                return kind_url[i];
        }
        return "";
    }

    public static List<HashMap<String, String>> getClassificationNameAndUrl(Context context){
        String[] kind_url = context.getResources().getStringArray(R.array.novel_kind_url);
        String[] kind_name = context.getResources().getStringArray(R.array.novel_kind_name);
        List<HashMap<String,String>> _List = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i <kind_name.length ; i++) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("name",kind_name[i]);
            map.put("url",kind_url[i]);
            _List.add(map);
        }
        return _List;
    }

    public static String[] getClassificationName(Context context){
        String[] kind_name = context.getResources().getStringArray(R.array.novel_kind_name);
        return kind_name;
    }

    public static List<NovelEntry> getNovelList(Document doc){
        List<NovelEntry> _List = new ArrayList<NovelEntry>();
        if(doc!=null){
            try{
                Element root = doc.getElementsByAttributeValue("class", "con").first();
                Elements es = root.getElementsByTag("dl");
                for(Element e:es){
                    Element dd = e.getElementsByTag("dd").first();
                    Element h4= dd.getElementsByTag("h4").first();
                    Element h4_a = h4.getElementsByTag("a").first();
                    Element h4_span = h4.getElementsByTag("span").first();
                    Element p = dd.getElementsByTag("p").first();
                    Element p_a = p.getElementsByTag("a").first();
                    Element dt_img = e.getElementsByTag("img").first();
                    String imgurl = dt_img.attr("src");
                    String introduction = p_a.text();
                    String href = h4_a.attr("href");
                    String bookname = h4_a.text();
                    String author = h4_span.text();

                    NovelEntry entry = new NovelEntry();
                    entry.setmAuthor(author);
                    entry.setmBookName(bookname);
                    entry.setmBookUrl(href);
                    entry.setmImgUrl(imgurl);
                    entry.setmIntroduction(introduction);
                    _List.add(entry);
                }
            }catch(Exception e){}
        }
        return _List;
    }

    public static List<NovelEntry> getNovelList(String html){
        Document doc = Jsoup.parse(html);
        return getNovelList(doc);
    }

    public static List<NovelEntry> getNextPageNovelList(Document doc){
        return getNovelList(doc);
    }

    public static int getPageCount(Document doc){
        int count = 0;
        try{
            Element e = doc.getElementById("pagelink");
            Element e_a  = e.getElementsByTag("a").last();
            count = Integer.parseInt(e_a.text());
        }catch (Exception e){}
        return count;
    }

    public static String getNovelState(Document doc){
        String state = "";
        try {
            Element e = doc.getElementsByAttributeValue("class","hotTag").first();
            Element a = e.getElementsByTag("a").first();
            state = a.text();
        }catch (Exception e){}
        return state;
    }

    public static String getNovelIntro(Document doc){
        String intro = "";
        try {
            Element e = doc.getElementsByAttributeValue("class", "introCon").first();
            intro = e.text();
        }catch (Exception e){}
        return intro;
    }

    public static List<ChapterEntry> getNetNovelChapters(Document doc,String baseurl){
        List<ChapterEntry> _List = new ArrayList<ChapterEntry>();
        Elements es = doc.getElementsByAttributeValue("class","ocon").first().getElementsByTag("a");
        for(Element e:es){
            String link = e.attr("href");
            link = baseurl+link;
            String name = e.text();
            ChapterEntry entry = new ChapterEntry(name,link);
            _List.add(entry);
        }
        return _List;

    }

    public static String getChapterContent(Document doc){
        String content = "";
        try{
            Element e = doc.getElementById("htmlContent");
            content  = e.text();
        }catch (Exception e){}
        return content;
    }

    public static String getChapterName(Document doc){
        String name = "";
        try {
            Element e = doc.getElementsByAttributeValue("class","nr_title").first().getElementsByTag("h3").first();
            name  = e.text();
        }catch (Exception e){}
        return name;
    }

    public static int getChapter(String url){
        int chapter;
        chapter = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1,
                url.length()).replace(".html", ""));
        return chapter;
    }
}
