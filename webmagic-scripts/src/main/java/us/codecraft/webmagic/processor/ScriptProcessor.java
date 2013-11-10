package us.codecraft.webmagic.processor;

import org.apache.commons.io.IOUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author code4crafter@gmail.com
 */
public class ScriptProcessor implements PageProcessor{

    private ScriptEngine rubyEngine;

    private String defines;

    ScriptProcessor(){
        ScriptEngineManager manager = new ScriptEngineManager();
        rubyEngine = manager.getEngineByName("jruby");
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ruby/defines.rb");
        try {
            defines = IOUtils.toString(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(Page page) {
        ScriptContext context = rubyEngine.getContext();
        context.setAttribute("page", page, ScriptContext.ENGINE_SCOPE);
        String script;
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("ruby/oschina.rb");
            try {
                script = IOUtils.toString(resourceAsStream);
                rubyEngine.eval(defines+script, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }

    public static void main(String[] args) {
        Spider.create(new ScriptProcessor()).addUrl("http://my.oschina.net/flashsword/blog").run();
    }
}