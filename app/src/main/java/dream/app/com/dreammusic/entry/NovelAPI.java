package dream.app.com.dreammusic.entry;

import android.content.Context;

import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static String getKeyWordsUrl(Context context){
        return context.getResources().getString(R.string.novel_keywords_url);
    }

    public static List<String> getKeyWordsList(Document doc){
        List<String> _List = new ArrayList<String>();
        try {
            Element e1 = doc.getElementsByAttributeValue("class","toplistcon").first();
            Elements es = e1.getElementsByAttributeValue("class","tit");
            for(Element e:es){
                String text = e.text();
               _List.add(text);
            }
        }catch (Exception e){}
        return _List;
    }

    public static String getSearchUrl(String searchkey){
        String url = "http://www.16kxsw.com/modules/article/search.php?searchtype=2&searchkey=";
        try {
            url+= URLEncoder.encode(searchkey, "gbk");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return url;
    }

    public static List<NovelEntry> getSearchNovelList(String type,String htmlContent){
        List<NovelEntry> _List = new ArrayList<NovelEntry>();
        if(type.equals("tbody")){
            _List = getSearchNovelByBody(htmlContent);
        }else if(type.equals("list")){
            _List = getSearchNovelByList(htmlContent);
        }
        return _List;
    }

    private static List<NovelEntry> getSearchNovelByBody(String htmlContent){
        List<NovelEntry> _List = new ArrayList<NovelEntry>();
        try {
            Document doc = Jsoup.parse(htmlContent);
            Elements es_tr = doc.getElementsByTag("tbody").first().getElementsByTag("tr");
            for (int i=1;i<es_tr.size();i++){
                Elements e_a = es_tr.get(i).getElementsByTag("a");
                Element e_1 = e_a.get(0);
                String bookname = e_1.text();
                String bookurl = e_1.attr("href");
                String state = e_a.get(1).text();
                String author = es_tr.get(i).getElementsByAttributeValue("class","odd").get(1).text();
                NovelEntry entry = new NovelEntry();
                entry.setmBookName(bookname);
                entry.setmBookUrl(bookurl);
                entry.setmIntroduction(state);
                entry.setmAuthor(author);
                _List.add(entry);
            }
        }catch (Exception e){}
        return _List;
    }

    private static List<NovelEntry> getSearchNovelByList(String htmlContent){
        List<NovelEntry> _List = new ArrayList<NovelEntry>();
        try {
            Document doc = Jsoup.parse(htmlContent);
            Element e1 = doc.getElementsByAttributeValue("class", "novel").first();
            String state = e1.getElementsByAttributeValue("class","hotTag").first().text();
            Element e2 = e1.getElementsByAttributeValue("class", "title").first();
            String bookname = e2.getElementsByTag("h2").first().text();
            String author = e2.getElementsByTag("span").first().text().replace("作者：", "");
            author = author.replace("作者: ","");
            Element e_img = e1.getElementsByTag("img").first();
            String img_url = e_img.attr("src");
            String baseurl = "http://www.16kxsw.com/16k/";
            String s[] = img_url.split("/");
            String s1 = s[s.length-3];
            String s2 = s[s.length-2];
            String bookurl =baseurl+s1+"/"+s2+"/";
            NovelEntry entry = new NovelEntry();
            entry.setmBookName(bookname);
            entry.setmBookUrl(bookurl);
            entry.setmIntroduction(state);
            entry.setmAuthor(author);
            _List.add(entry);
        }catch (Exception e){}
        return _List;
    }

    public static String getImgUrl(Document doc){
        String imgurl = "";
        Element e = doc.getElementsByAttributeValue("class","novel").first();
        imgurl = e.getElementsByTag("img").first().attr("src");
        return imgurl;
    }
}
