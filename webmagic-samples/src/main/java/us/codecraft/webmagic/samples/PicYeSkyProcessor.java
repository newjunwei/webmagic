package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.pipeline.OneFilePipeline;
import us.codecraft.webmagic.samples.pipeline.UrlPreOneFilePipeline;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.soap.Node;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
public class PicYeSkyProcessor implements PageProcessor {
    //http://pic.yesky.com/c/6_20771.shtml
    public static final String URL_LIST = "http://pic\\.yesky\\.com/c/[\\d_]+\\.shtml";
    //http://pic.yesky.com/290/40162290.shtml
    public static final String URL_PIC = "http://pic\\.yesky\\.com/\\d+/[\\d_]+\\.shtml";

    public static final String URI_LIST="/c/[\\d_]+\\.shtml";
    private Site site = Site
            .me()
            .setDomain("pic.yesky.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
            List<Selectable> imageBoxes = page.getHtml().xpath("//div[@class=\"mode_box\"]/dl").nodes();
            int i = 0;
//            page.putField("pageUrl",page.getUrl().get());
            for (Selectable box : imageBoxes) {
                i++;
                String firstPicUrl = box.xpath("//dt/a/@href").regex(URL_PIC).get();
//                page.putField("url" + i, firstPicUrl);
//                page.putField("titlePicSrc" + i, box.xpath("//dt/a/img/@src"));
//                page.putField("title" + i, box.xpath("//dd/a/@title"));
//                page.putField("number" + i, box.xpath("//dd/span/text()").regex("\\((.*)\\)"));
                page.addTargetRequest(firstPicUrl);
            }
//            List<String> pages = page.getHtml().xpath("//div[@class=\"flym\"]").links().regex(URI_LIST).all();
//            page.addTargetRequests(pages);
            //图集页
        } else {
            Selectable imageBlock = page.getHtml().xpath("//div[@id=\"l_effect_img\"]//div[@class=\"l_effect_img_mid\"]");
            String nextPicUrl = imageBlock.links().regex(URL_PIC).get();
            page.putField("url", page.getUrl().get());
            String picSrc = imageBlock.xpath("//a/img/@src").get();
            page.putField("picSrc", picSrc);
            String title = imageBlock.xpath("//a/img/@alt").get();
//            page.putField("picDes", title);
            page.addTargetRequest(nextPicUrl);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Spider.create(new PicYeSkyProcessor()).addUrl("http://pic.yesky.com/c/6_61113.shtml").addPipeline(new UrlPreOneFilePipeline("/Users/junwei/temp/webmagic"))
                .thread(50).run();
    }
}
