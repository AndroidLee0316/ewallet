package com.pasc.lib.netpay.converter;

import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.ByteString;

/**
 * @author yangzijian
 * @date 2019/3/19
 * @des
 * @modify
 **/
public class PascRequestBody extends RequestBody {
    private MediaType contentType;
    private ByteString content;
    public String jsonValue;

    public PascRequestBody(MediaType contentType, ByteString content, String jsonValue) {
        this.contentType = contentType;
        this.content = content;
        this.jsonValue = jsonValue;
    }

    @Override
    public
    MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return content.size ();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write (content);
    }


}
