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

    @Test
    public void altTabTest(){
        App.doAltTab();
        App.doAltTab();
    }

    @Test
    public void readConfigJson(){
        JSONObject conf = FileHandler.readJSON("mm_config.json");
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

    @Test
    public void createConfigFileTest(){
        App.createDefaultConfigFile("testConfig.json");
    }


}
