package com.pasc.lib.netpay.upload;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class CountingRequestBody extends RequestBody {
    private static final Handler HANDLER=new Handler(Looper.getMainLooper()) ;
    protected RequestBody requestBody;
    private UploadListener listener;
    private BufferedSink bufferedSink;

    public CountingRequestBody(RequestBody requestBody, UploadListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            // Log Interceptor
            requestBody.writeTo(sink);
            return;
        }
//        BufferedSink bufferedSink = Okio.buffer(sink(sink));
        if (bufferedSink == null)
            bufferedSink = Okio.buffer(sink(sink));
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调bubufferedSink.flush();不然最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long byteWritten = 0L;//当前写入字节数
            long contentLength = 0L;//总字节长度，避免多次调用contentLength()方法

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0)
                    contentLength = contentLength();//获取总字节长度，避免多次调用
                byteWritten += byteCount;//累计当前写入字节数
                if (listener != null) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.progress(byteWritten * 1.0f / contentLength,byteWritten, contentLength, byteWritten == contentLength);
                        }
                    });

                }
            }
        };
    }


}

