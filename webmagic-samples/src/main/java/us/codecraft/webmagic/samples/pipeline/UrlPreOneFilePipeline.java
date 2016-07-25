package us.codecraft.webmagic.samples.pipeline;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.*;
import java.util.Map;

/**
 * Store results in files.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@ThreadSafe
public class UrlPreOneFilePipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * create a FilePipeline with default path"/data/webmagic/"
     */
    public UrlPreOneFilePipeline() {
        setPath("/data/webmagic/");
    }

    public UrlPreOneFilePipeline(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.get("pageUrl");
        if(url==null) {
            url=resultItems.get("url");
            int index = url.lastIndexOf("_");
            url = index == -1 ? url : url.substring(0, index).concat(".shtml");
        }
        url = url.replaceAll("\\W", "_");
        String path = this.path + PATH_SEPERATOR + task.getUUID()+"_life" + PATH_SEPERATOR + url + ".txt";
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(path),true));
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                String key = entry.getKey();
                if(key.equals("url")){
                    continue;
                }
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
//                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
//                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                    printWriter.println(entry.getValue());
                }
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }

    public static void main(String[] args) {
        String url="http://pic.yesky.com/c/6_61113.shtml";
        url=url.replaceAll("\\W","_");
        System.out.println(url);
    }
}
