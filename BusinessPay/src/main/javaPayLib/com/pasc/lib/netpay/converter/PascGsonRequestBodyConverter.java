package com.pasc.lib.netpay.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.pasc.business.ewallet.config.Constants;
import com.pasc.lib.netpay.param.BaseV2Param;
import com.pasc.lib.sm.SM3Digest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * @author yangzijian
 * @date 2019/3/19
 * @des
 * @modify
 **/
public class PascGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse ("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName ("UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    PascGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        PascRequestBody body = null;
        if (value instanceof BaseV2Param) {
            BaseV2Param baseV2Param = (BaseV2Param) value;
            String sign = null;
            if (baseV2Param.message instanceof String) {
                sign = SM3Digest.d (baseV2Param.message + Constants.SECRET_KEY);
            } else if ((baseV2Param.message instanceof List) || (baseV2Param.message instanceof Set)) {
                String json = gson.toJson (baseV2Param.message);
                sign = SM3Digest.d (json + Constants.SECRET_KEY);
            } else {
                String json = gson.toJson (baseV2Param.message);
                try {
                    JSONObject jsonObject = new JSONObject (json);
                    Iterator<String> iterator = jsonObject.keys ();
                    StringBuilder builder = new StringBuilder ();
                    String and = "&";
                    Map<String, String> map = new TreeMap<> ();
                    while (iterator.hasNext ()) {
                        String key = iterator.next ();
                        map.put (key, jsonObject.getString (key));
                    }
                    int i = 0;
                    int size =map.size ();
                    for (Map.Entry<String,String> entry:map.entrySet ()){
                        i++;
                        builder.append (entry.getKey () + "=" + entry.getValue () + ((i < size) ? and : ""));
                    }
                    sign = SM3Digest.d (builder.toString () + Constants.SECRET_KEY);
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
            baseV2Param.sign = sign;
            baseV2Param.appId = Constants.APP_ID;

            Buffer buffer = new Buffer ();
            Writer writer = new OutputStreamWriter (buffer.outputStream (), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter (writer);
            adapter.write (jsonWriter, value);
            jsonWriter.close ();
            String jsonValue = gson.toJson (value);
            body = new PascRequestBody (MEDIA_TYPE, buffer.readByteString (), jsonValue);
        } else {
            Buffer buffer = new Buffer ();
            Writer writer = new OutputStreamWriter (buffer.outputStream (), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter (writer);
            adapter.write (jsonWriter, value);
            jsonWriter.close ();
            String jsonValue = "";
            if (value instanceof String) {
                jsonValue = (String) value;
            } else {
                jsonValue = gson.toJson (value);
            }
            body = new PascRequestBody (MEDIA_TYPE, buffer.readByteString (), jsonValue);
        }
        return body;
    }
}
