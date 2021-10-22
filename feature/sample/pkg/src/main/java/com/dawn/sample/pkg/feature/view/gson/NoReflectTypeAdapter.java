package com.dawn.sample.pkg.feature.view.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : LeeZhaoXing
 * @date : 2021/9/1
 * @desc :
 */
class NoReflectTypeAdapter<T> extends TypeAdapter<T> {
    private Gson gson;
    private Class<T> rawType;

    public NoReflectTypeAdapter(Gson gson, Class<T> rawType) {
        this.gson = gson;
        this.rawType = rawType;
    }

    private TypeToken<List<String>> obj = new TypeToken<List<String>>() {
    };

    private TypeAdapter<List<String>> adapter = gson.getAdapter(obj);


    @Override
    public void write(JsonWriter out, T value) throws IOException {

    }

    @Override
    public T read(JsonReader input) throws IOException {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull();
            return null;
        }

        try {
            if (OutBean.class.equals(rawType)) {
                String outName = "";
                int age = 0;
                OutBean.InnerBean bean = null;
                ArrayList list = null;
                input.beginObject();
                while (input.hasNext()) {
                    switch (input.nextName()) {
                        case "outName":
                            outName = input.nextString();
                            break;
                        case "age":
                            age = input.nextInt();
                            break;
                        case "bean":
                            bean = (OutBean.InnerBean) gson.getAdapter(OutBean.InnerBean.class).read(input);
                            break;
                        case "list": {
                            list = (ArrayList<String>) adapter.read(input);
                        }
                    }
                }
                return (T) new OutBean(outName, age, bean, list);
            } else if (OutBean.InnerBean.class.equals(rawType)) {
                String innerName = "";
                input.beginObject();
                while (input.hasNext()) {
                    if ("innerName".equals(input.nextName())) {
                        innerName = input.nextString();
                    }
                }
                return (T) new OutBean.InnerBean(innerName);
            }
        } catch (IllegalStateException e) {
            throw new JsonSyntaxException(e);
        } finally {
            input.endObject();
        }
        return null;
    }
}


