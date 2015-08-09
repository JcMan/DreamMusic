package dream.app.com.dreammusic.entry;

import android.content.Context;

import com.app.tool.logger.Logger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;

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

    public static List<NovelEntry> getNovelList(Document doc){
        List<NovelEntry> _List = new ArrayList<NovelEntry>();
        if(doc!=null){
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
        }
        return _List;
    }
}
