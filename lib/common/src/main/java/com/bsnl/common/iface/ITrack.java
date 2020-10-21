package com.bsnl.common.iface;

import java.util.HashMap;

/**
 * @author : LeeZhaoXing
 * @date : 2020/8/7
 * @desc : 数据埋点
 */
public interface ITrack {

    default void track(String event, String extraKey, String extraPropertyKey, String extraPropertyValue) {

        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put(extraPropertyKey, extraPropertyValue);

        track(event, extraKey, extraProperties);

    }

    default void track(String event, String extraKey, HashMap<String, String> extraProperties) {

//        BxSensorsData.Builder builder = BxSensorsData.getBuilder()
//                .setEventKey(event);
//
//        if (!TextUtils.isEmpty(extraKey)) {
//            builder.appendExtraKey(extraKey);
//        }
//
//        if (!CollectionUtil.isEmptyOrNull(extraProperties)) {
//            for (String key : extraProperties.keySet()) {
//                String value = extraProperties.get(key);
//                builder.appendExtraProperties(key, value);
//            }
//        }
//        builder.track();
    }
}
