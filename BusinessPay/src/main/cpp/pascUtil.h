//
// Created by 杨自鉴 on 2019/3/4.
//
#ifndef ANDROIDSAFEDEMO_PASCUTIL_H
#define ANDROIDSAFEDEMO_PASCUTIL_H

#include <android/log.h>
#include <jni.h>
#include <string>
#include <unistd.h>
#define  LOG_TAG    "yzj"
#ifdef LOG_FLAG
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#else
#define  LOGI(...)
#endif
using namespace std;
void be_attached_check();
string *byteToHexStr(unsigned char byte_arr[], int arr_len);

#endif //ANDROIDSAFEDEMO_PASCUTIL_H
