package hu.barbar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import hu.barbar.util.FileHandler;
import org.json.simple.JSONObject;

/**
 * Hello world!
 */
public class App {

    public static final String CONFIG_FILE_NAME = "mm_config.json";
    public static final int DEFAULT_MOVE_RANGE = 30;
    public static final boolean DEFAULT_WASD = false;
    public static final boolean DEFAULT_MOUSE_MOVEMENT = true;
    public static final double DEFAULT_WAIT_MIN_IN_MS = 3000;
    public static final double DEFAULT_WAIT_MAX_IN_MS = 120000;

    double minWaitParam = DEFAULT_WAIT_MIN_IN_MS;
    double maxWaitParam = DEFAULT_WAIT_MAX_IN_MS;
    long moveRange = DEFAULT_MOVE_RANGE;

    boolean moveWASD = false;
    boolean mouseMovement = true;
    boolean needAltTab = false;

    int counter = 0;


    public static void main(String[] args) {
        new App();
    }

    private void doAltTab(){
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

    public App(){
        System.out.println("\n");
        readConfigs();

        System.out.println("\n\nYou can stop me with \"CTRL + C\"");
        System.out.println("I wish you a good rest! :)\n");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        Robot robot = null;
        try {
            robot = new Robot();
            Random random = new Random();

            while (true) {
                if(counter < 3){
                    counter++;
                }

                random = new Random();
                int mx = (int) (random.nextInt((int) moveRange) - (moveRange / 2));
                random = new Random();
                int my = (int) (random.nextInt((int) moveRange) - (moveRange / 2));
                //System.out.println("\t" + mx + "\t" + my);

                if(mouseMovement) {
                    PointerInfo mi = MouseInfo.getPointerInfo();
                    Point lp = mi.getLocation();
                    int x = (int) lp.getX();
                    int y = (int) lp.getY();
                    //System.out.println("X: " + lp.getX() + "\nY: " + lp.getY());

                    Point modded = new Point((int) lp.getX(), (int) lp.getY());
                    robot.mouseMove((int) modded.getX() + mx, (int) modded.getY() + my);
                }

                // WASD if needed
                if(moveWASD && counter > 2){
                    int pressLength = 500;
                    int waitBetween = 200;

                    keyPressAndRelease(robot, pressLength, waitBetween,  'W');
                    keyPressAndRelease(robot, pressLength, waitBetween,  'A');
                    keyPressAndRelease(robot, pressLength, waitBetween,  'S');
                    keyPressAndRelease(robot, pressLength, waitBetween,  'D');
                    
                }


                if(needAltTab) {
                    doAltTab();
                    Thread.sleep(100);
                    doAltTab();
                }
                now = new Date();

                int waitRange = (int) (maxWaitParam - minWaitParam);
                double wait_in_ms = random.nextInt(waitRange) + minWaitParam;
                System.out.println(sdf.format(now) + "\t" + ((wait_in_ms/1000)<10f?" ":"") + (int)(wait_in_ms/1000) + " s" + "\t[" + mx + "; " + my + "]\t");

                Thread.sleep((long) (wait_in_ms));
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void keyPressAndRelease(Robot robot, int pressLength, int waitBetween, int keyCode) throws InterruptedException {
        robot.keyPress(keyCode);
        Thread.sleep(pressLength);
        robot.keyRelease(keyCode);
        Thread.sleep(waitBetween);
    }

    private void readConfigs() {
        JSONObject conf = FileHandler.readJSON(CONFIG_FILE_NAME);
        if (conf != null) {
            //System.out.println("Conf: " + conf);
            moveRange = (long) conf.getOrDefault("range in px", 30l);

            moveWASD = (boolean) conf.getOrDefault("wasd", false);
            mouseMovement = (boolean) conf.getOrDefault("mm", true);
            needAltTab = (boolean) conf.getOrDefault("need_alt_tab", false);


            System.out.println("Move WASD: " + moveWASD);

            JSONObject waitInMsJSON = (JSONObject) conf.get("wait in ms");
            if (waitInMsJSON != null) {
                minWaitParam = (long) waitInMsJSON.getOrDefault("min", 5000l);
                maxWaitParam = (long) waitInMsJSON.getOrDefault("max", 12000l);
            } else {
                //TODO
                System.out.println("Can not read wait params..");
            }
            System.out.println("min wait: " + minWaitParam/1000 + " s");
            System.out.println("max wait: " + maxWaitParam/1000 + " s");
            System.out.println("movement range in px: " + moveRange + " px");
        }else{
            System.out.println("Config file (\""+ CONFIG_FILE_NAME + "\") can not be found..\nTry to create it with default parameters:");
            System.out.println("\tMin wait in ms: " + DEFAULT_WAIT_MIN_IN_MS);
            System.out.println("\tMax wait in ms: " + DEFAULT_WAIT_MAX_IN_MS);
            System.out.println("\tMovement range in px: " + DEFAULT_MOVE_RANGE);
            createDefaultConfigFile();
        }
    }

    public static void createDefaultConfigFile(){
        createDefaultConfigFile(CONFIG_FILE_NAME);
    }

    public static void createDefaultConfigFile(String configFileName){
        ArrayList<String> linesOfDefaultConfigFile = new ArrayList<String>();
        linesOfDefaultConfigFile.add("{\n" +
                "\t\"range in px\": " + DEFAULT_MOVE_RANGE + ",\n" +
                "\t\"wasd\": " + DEFAULT_WASD + ",\n" +
                "\t\"mm\": " + DEFAULT_MOUSE_MOVEMENT + ",\n" +
                "\t\"wait in ms\":{\n" +
                "\t\t\"min\": " + (long)DEFAULT_WAIT_MIN_IN_MS + ",\n" +
                "\t\t\"max\": " + (long)DEFAULT_WAIT_MAX_IN_MS + "\n" +
                "\t}\n" +
                "}");
        FileHandler.writeToFile(configFileName, linesOfDefaultConfigFile);
    }

}
