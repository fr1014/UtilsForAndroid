package com.fr.konwledge.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fr.konwledge.application.BaseApplication;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间：2019/7/31
 * 作者：范瑞
 * 博客：https://www.jianshu.com/u/408f3c1b46a9
 */
public class SPUtils {

    private static SharedPreferences.Editor getEditor() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext()).edit();
    }

    private static SharedPreferences getSharedPre() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
    }

    /**
     * 存储boolean类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    public static void save(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    /**
     * 存储float类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    public static void save(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    /**
     * 存储int类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    public static void save(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    /**
     * 存储long类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    public static void save(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    /**
     * 存储String类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    public static void save(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    /**
     * 存储bean类的键值对到SharedPreferences文件当中。
     * <p>
     * 由于SharedPreferences不能直接存储此类型，所以转化为json字符串
     *
     * @param key    存储的键
     * @param object 存储的值
     */
    public static void save(String key, Object object) {
        Observable.create(emitter -> {
            String json = new Gson().toJson(object);
            getEditor().putString(key, json).apply();
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的bean类的值。
     * 
     * @param key       读取的键
     * @param clazz     bean类
     * @param <T>       泛型
     * @return
     */
    public static <T> T getClassFromSp(String key, Class<T> clazz) {
        try {
            String json = getSharedPre().getString(key, "");
            return new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的boolean类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return boolean类型的值，如果读取不到，则返回默认值
     */
    public static boolean read(String key, boolean defValue) {
        return getSharedPre().getBoolean(key, defValue);
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的float类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return float类型的值，如果读取不到，则返回默认值
     */
    public static float read(String key, float defValue) {
        return getSharedPre().getFloat(key, defValue);
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的int类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return int类型的值，如果读取不到，则返回默认值
     */
    public static int read(String key, int defValue) {
        return getSharedPre().getInt(key, defValue);
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的long类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return long类型的值，如果读取不到，则返回默认值
     */
    public static long read(String key, long defValue) {
        return getSharedPre().getLong(key, defValue);
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的String类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return String类型的值，如果读取不到，则返回默认值
     */
    public static String read(String key, String defValue) {
        return getSharedPre().getString(key, defValue);
    }

    /**
     * 判断SharedPreferences文件当中是否包含指定的键值。
     *
     * @param key 判断键是否存在
     * @return 键已存在返回true，否则返回false。
     */
    public static boolean contains(String key) {
        return getSharedPre().contains(key);
    }

    /**
     * 清理SharedPreferences文件当中传入键所对应的值。
     *
     * @param key 想要清除的键
     */
    public static void clear(String key) {
        getEditor().remove(key).apply();
    }

    /**
     * 将SharedPreferences文件中存储的所有值清除。
     */
    public static void clearAll() {
        getEditor().clear().apply();
    }
}
