package com.dawn.sample.pkg.feature.view.gson;

import com.dawn.base.log.L;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LeeZhaoXing
 * @date : 2021/9/1
 * @desc :
 */
public class GsonDemo {

    public static void test() {
        List<String> tmp13 = new ArrayList<>();
        tmp13.add("1");
        tmp13.add("3");

        List<String> tmp1 = new ArrayList<>();
        tmp1.add("asd");
        tmp1.add("eds");

        List<OutBean> data = new ArrayList<>();
        data.add(new OutBean("xujiafeng", 12, new OutBean.InnerBean("innerXujiafeng"), tmp13));
        data.add(new OutBean("lihua", 24, new OutBean.InnerBean("innerLihua"), tmp1));

        Gson gson = new Gson();
        String json = gson.toJson(data);

        Gson goodGson = new GsonBuilder()
                .registerTypeAdapterFactory(new NoReflectTypeAdapterFactory())
                .create();

        List<OutBean> list = goodGson.fromJson(json, (new TypeToken<List<OutBean>>() {
        }).getType());

        println(list.toString());

        //效率测试
        List<String> tmp = new ArrayList<>();
        tmp.add("1");
        tmp.add("2");
        List<OutBean> mutableList = new ArrayList<>();
        for (int i = 0; i >= 0 && i < 1000000; i++) {

            mutableList.add(new OutBean("xujiafeng" + i, i, new OutBean.InnerBean("innerxujiafeng" + i), tmp));
        }

        String bigJson = gson.toJson(mutableList);

        {
            long startTime = System.currentTimeMillis();
            List<OutBean> goodResult = goodGson.fromJson(bigJson, (new
                                                                           TypeToken<List<OutBean>>() {
                                                                           }).getType());

            long endTime = System.currentTimeMillis();
            println("take time good " + (endTime - startTime));
        }


        Gson badGson = new Gson();
        {
            long startTime = System.currentTimeMillis();
            List<OutBean> result = badGson.fromJson(bigJson, (new TypeToken<List<OutBean>>() {
            }).getType());
            long endTime = System.currentTimeMillis();
            println("take time " + (endTime - startTime));

        }


    }

    private static void println(String s) {
        L.INSTANCE.d(">>>>  " + s);
    }
}