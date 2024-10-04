package com.pasc.lib.netpay.upload;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/9/5
 * @des
 * @modify
 **/
public interface UploadListener {
    void progress(float progress, long byteWrited, long contentLength, boolean done);
}
