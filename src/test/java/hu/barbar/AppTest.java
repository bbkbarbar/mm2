package hu.barbar;

import static org.junit.Assert.assertTrue;

import hu.barbar.util.FileHandler;
import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Ignore
    @Test
    public void altTabTest(){

        Robot r = null;
        try {
            r = new Robot();
            r.keyPress(KeyEvent.VK_ALT);
            Thread.sleep(100);
            r.keyPress(KeyEvent.VK_TAB);
            Thread.sleep(100);
            r.keyRelease(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_ALT);

            //r.keyPress(KeyEvent.VK_TAB);
        }catch (Exception e){

        }

    }

    @Test
    public void readConfigJson(){
        JSONObject conf = FileHandler.readJSON("config.json");
        if(conf != null) {
            System.out.println("Conf: " + conf);
            long val = (long) conf.getOrDefault("range in px", 30l);
            System.out.println("int value: " + val + "\n");

            JSONObject waitInMsJSON = (JSONObject) conf.get("wait in ms");
            long minWaitParam = 10000l;
            long maxWaitParam = 120000l;
            if(waitInMsJSON != null){
                minWaitParam = (long) waitInMsJSON.getOrDefault("min", 5000l);
                maxWaitParam = (long) waitInMsJSON.getOrDefault("max", 12000l);
            }else{
                //TODO
                System.out.println("Can not read wait params..");
            }
            System.out.println("minWaitParam: " + minWaitParam);
            System.out.println("maxWaitParam: " + maxWaitParam);





        }else{
            System.out.println("Config file (\"config.json\") does not exists.");
        }
    }

    @Ignore
    @Test
    public void createConfigFileTest(){
        App.createDefaultConfigFile();
    }


}
