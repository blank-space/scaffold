package com.dawn.sample.pkg.feature.view.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author : LeeZhaoXing
 * @date : 2021/9/1
 * @desc :
 */
public class NoReflectTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (OutBean.class.equals(type.getRawType())) {
            return new NoReflectTypeAdapter(gson, type.getRawType());
        }
        if (OutBean.InnerBean.class.equals(type.getRawType())) {
            return new NoReflectTypeAdapter(gson, type.getRawType());
        }
        return null;
    }
}



