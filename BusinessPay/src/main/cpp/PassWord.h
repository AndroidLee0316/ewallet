//
// Created by 杨自鉴 on 2019/3/4.
//

#ifndef NDKTEST_PASSWORD_H
#define NDKTEST_PASSWORD_H

#include <jni.h>
#include <iostream>
void clearPassWord();
void removePassWord();
const char *getPassword();
void addPassWord(JNIEnv *env, jobject instance,const  char *pwd,jint mode);
int currentLength();
jstring getNativePassword(JNIEnv *env, jobject instance,jint mode);
#endif //NDKTEST_PASSWORD_H
