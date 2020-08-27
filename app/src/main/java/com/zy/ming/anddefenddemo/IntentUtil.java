package com.zy.ming.anddefenddemo;

import android.content.Intent;
import android.os.Parcelable;

/**
 * description ：intent工具类 替换系统的getIntExtra等方法
 *              数据来源可控，防恶意攻击 可自由添加相应方法
 * author : create by qiaoming on 2020/7/20
 * version :
 */
public class IntentUtil {

    public static int getIntExtra(Intent intent,String key,int defaultValue){
        try {
            return intent.getIntExtra(key,defaultValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static String getStringExtra(Intent intent,String key){
        try {
            return intent.getStringExtra(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static boolean getBooleanExtra(Intent intent,String key,boolean defaultValue){
        try {
            return intent.getBooleanExtra(key,defaultValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static <T extends Parcelable> T getParcelableExtra(Intent intent, String key){
        try {
            return (T) intent.getParcelableExtra(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
