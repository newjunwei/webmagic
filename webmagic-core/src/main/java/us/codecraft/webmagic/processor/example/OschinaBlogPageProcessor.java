package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
public class OschinaBlogPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("http://pic.yesky.com/");

    @Override
    public void process(Page page) {
//        http://pic.yesky.com/226/98071726.shtml

        List<String> links = page.getHtml().links().regex("http://pic\\.yesky\\.com/\\d+/\\d+.shtml").all();
        page.addTargetRequests(links);
        //http://dynamic-image.yesky.com/220x165/uploadImages/2016/195/05/QKQ656377330.jpg
        List<String> titles = page.getHtml().css("div.mode_box").regex("<img src=[^>]+").all();//.regex("http://dynamic-image.yesky.com/[^.]+\\.jpg").toString();
        page.putField("titles", titles);
        if (titles.size()==0) {
            String pic = page.getHtml().css("div.ll_img").regex("<img src=[^>]+").get();//.regex("http://dynamic-image.yesky.com/[^.]+\\.jpg").toString();
            page.putField("pic",pic);
        }
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new OschinaBlogPageProcessor()).addUrl("http://pic.yesky.com/tag/meinv/1/%E4%BA%BA%E4%BD%93%E8%89%BA%E6%9C%AF/").addPipeline(new ConsolePipeline()).run();
    }
}
