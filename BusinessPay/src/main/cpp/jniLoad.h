//
// Created by 杨自鉴 on 2018/11/14.
// 用于 JNI_OnLoad 初始化注册一些本地方法
//

#ifndef LIBMATCH_JNILOAD_H
#define LIBMATCH_JNILOAD_H

#include <jni.h>

jint RegisterPwdNativeMethods(JNIEnv *env);
jint UnregisterNativeMethods(JNIEnv *env, const char *className);
jint RegisterNativeMethods(JNIEnv* env, const char* className, const JNINativeMethod* methods, int numMethods);

#endif //LIBMATCH_JNILOAD_H
