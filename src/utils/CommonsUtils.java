package utils;

import java.util.UUID;

public class CommonsUtils {
    //生成uid
    public static  String getUid(){
        return UUID.randomUUID().toString();
    }
    //生成telephone
    public static String getTelephone(){
        int num = (int) (Math.random() * 900000) + 100000;
        return Integer.toString(num);
    }
}
