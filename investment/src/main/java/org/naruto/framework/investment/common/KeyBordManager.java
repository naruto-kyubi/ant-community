package org.naruto.framework.investment.common;

import com.google.common.collect.Lists;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Point;

import java.util.HashMap;
import java.util.Map;

public class KeyBordManager {
    public static Map<String, Map<String, Point>> keyBordMap = new HashMap<String, Map<String, Point>>();

    public static void initKeyBord(){
        //华为青春20数字键盘
        Map<String, Point> lra_number = new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(540,2261));
                put("keyboard_no_1",new Point(180,1697));
                put("keyboard_no_2",new Point(540,1697));
                put("keyboard_no_3",new Point(720,1697));
                put("keyboard_no_4",new Point(180,1842));
                put("keyboard_no_5",new Point(540,1842));
                put("keyboard_no_6",new Point(720,1842));
                put("keyboard_no_7",new Point(180,2117));
                put("keyboard_no_8",new Point(540,2117));
                put("keyboard_no_9",new Point(720,2117));
            }

        };
        //华为青春20全键盘
        Map<String, Point> lra_full = new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(1026,1634));
                put("keyboard_no_1",new Point(54,1634));
                put("keyboard_no_2",new Point(162,1634));
                put("keyboard_no_3",new Point(270,1634));
                put("keyboard_no_4",new Point(378,1634));
                put("keyboard_no_5",new Point(486,1634));
                put("keyboard_no_6",new Point(594,1634));
                put("keyboard_no_7",new Point(702,1634));
                put("keyboard_no_8",new Point(810,1634));
                put("keyboard_no_9",new Point(918,1634));
                put("keyboard_no_q",new Point(54,1794));
                put("keyboard_no_a",new Point(115,2000));
                put("keyboard_no_shift",new Point(93,2150));
            }

        };

        Map<String, Point> lra_huatai = new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(540,2320));
                put("keyboard_no_1",new Point(180,1927));
                put("keyboard_no_2",new Point(540,1927));
                put("keyboard_no_3",new Point(900,1927));
                put("keyboard_no_4",new Point(180,2062));
                put("keyboard_no_5",new Point(540,2062));
                put("keyboard_no_6",new Point(900,2062));
                put("keyboard_no_7",new Point(180,2194));
                put("keyboard_no_8",new Point(540,2194));
                put("keyboard_no_9",new Point(900,2194));
            }

        };

        //红米note8数字键盘
        Map<String, Point> redmi_note_8_number =new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(540,2099));
                put("keyboard_no_1",new Point(180,1653));
                put("keyboard_no_2",new Point(540,1653));
                put("keyboard_no_3",new Point(900,1653));
                put("keyboard_no_4",new Point(180,1780));
                put("keyboard_no_5",new Point(540,1780));
                put("keyboard_no_6",new Point(900,1780));
                put("keyboard_no_7",new Point(180,1982));
                put("keyboard_no_8",new Point(540,1982));
                put("keyboard_no_9",new Point(900,1982));
            }
        };

        //红米note8全键盘
        Map<String, Point> redmi_note_8_full = new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(1026,1601));
                put("keyboard_no_1",new Point(54,1601));
                put("keyboard_no_2",new Point(162,1601));
                put("keyboard_no_3",new Point(270,1601));
                put("keyboard_no_4",new Point(378,1601));
                put("keyboard_no_5",new Point(486,1601));
                put("keyboard_no_6",new Point(594,1601));
                put("keyboard_no_7",new Point(702,1601));
                put("keyboard_no_8",new Point(810,1601));
                put("keyboard_no_9",new Point(918,1601));
                put("keyboard_no_q",new Point(54,1676));
                put("keyboard_no_a",new Point(155,1878));
                put("keyboard_no_shift",new Point(85,2000));
            }

        };

        //红米note8，华泰键盘（非全屏模式）
        Map<String, Point> redmi_note_8_huatai = new HashMap<String,Point>(){
            {
                put("keyboard_no_0",new Point(540,2127));
                put("keyboard_no_1",new Point(180,1761));
                put("keyboard_no_2",new Point(540,1761));
                put("keyboard_no_3",new Point(900,1761));
                put("keyboard_no_4",new Point(180,1883));
                put("keyboard_no_5",new Point(540,1883));
                put("keyboard_no_6",new Point(900,1883));
                put("keyboard_no_7",new Point(180,2000));
                put("keyboard_no_8",new Point(540,2000));
                put("keyboard_no_9",new Point(900,2000));
            }
        };

        //小米键盘
        keyBordMap.put("LRA-AL00_number",lra_number);
        keyBordMap.put("LRA-AL00_full",lra_full);
        keyBordMap.put("LRA-AL00_huatai",lra_huatai);

        //红米note8键盘
        keyBordMap.put("Redmi Note 8_number",redmi_note_8_number);
        keyBordMap.put("Redmi Note 8_full",redmi_note_8_full);
        keyBordMap.put("Redmi Note 8_huatai",redmi_note_8_huatai);
    }

    public static Map<String,Point> getKeyBord(AndroidDriver<MobileElement> driver,String keybordType){
        if (keyBordMap.size() <1) {
            initKeyBord();
        }
        String model = "";

        Map<String, Object> args = new HashMap<>();
        args.put("command", "getprop");
        args.put("args", Lists.newArrayList("ro.product.model"));
        model = ((String)driver.executeScript("mobile: shell", args)).replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", "");;
        return keyBordMap.get(model.concat("_").concat(keybordType));
    }

    public static void tap(AndroidDriver<MobileElement> driver,Map<String,Point> keybord,String pwd){
        for(int i =0; i<pwd.length();i++) {
            char num = pwd.charAt(i);
            String key = "keyboard_no_".concat(String.valueOf(num).toLowerCase());
            Point point = keybord.get(key);
            System.out.println("click the point ".concat(point.toString()));
            AndroidTouchAction action = new AndroidTouchAction(driver);
            if(Character.isUpperCase(num)){
                  // 大小写转换键
                Point shift = keybord.get("keyboard_no_shift");
                action.press(PointOption.point(shift)).release().perform();
            }
            action.press(PointOption.point(point)).release().perform();
        }
    }
}
