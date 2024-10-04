//
// Created by 杨自鉴 on 2019/3/4.
//
#include <jni.h>
#include "Constant.h"
#include "jniLoad.h"
#include "pascUtil.h"
#include "PassWord.h"
#include "sm3.h"


extern "C"
JNIEXPORT void JNICALL
nativeAddPassWord(JNIEnv *env, jobject instance, jstring pwd,jint mode) {
    jboolean  isCopy=0;
    const char *nativeString = env->GetStringUTFChars(pwd, &isCopy);
    addPassWord(env, instance, nativeString,mode);
    env->ReleaseStringUTFChars(pwd, nativeString);

}
extern "C"
JNIEXPORT void JNICALL
nativeRemovePassWord(JNIEnv *env, jobject instance) {
    removePassWord();
}

extern "C"
JNIEXPORT void JNICALL
nativeClearPassWord(JNIEnv *env, jobject instance) {
    clearPassWord();
}

 int currentIndex(){
     return currentLength();
 }
string *sm3StringTmp(const char *data) {
    int len = strlen(data);
    unsigned char output[SM3_DIGEST_LENGTH];
    sm3( (const unsigned char *)data, len, output);
    string *sm3Str = byteToHexStr(output, SM3_DIGEST_LENGTH);
    return sm3Str;
}
extern "C"
JNIEXPORT jstring JNICALL
 sm3Data(JNIEnv *env, jobject instance,jstring data){
    jboolean  isCopy=0;
    const char *nativeString = env->GetStringUTFChars(data, &isCopy);
    string * dd=sm3StringTmp(nativeString);
    jstring  sssss=env->NewStringUTF(dd->c_str());
    delete dd;
    dd=NULL;
    env->ReleaseStringUTFChars(data, nativeString);
    return sssss;

}
jstring nativeGetPassword(JNIEnv *env, jobject instance,jint mode) {
    return getNativePassword(env, instance,mode);
}


jint RegisterPwdNativeMethods(JNIEnv *env) {
    const int count = 5;
    const JNINativeMethod methods[count] =
            {
                    {"nativeAddPassWord",    "(Ljava/lang/String;I)V", (void *) nativeAddPassWord},
                    {"nativeRemovePassWord", "()V",                   (void *) nativeRemovePassWord},
                    {"nativeClearPassWord",  "()V",                   (void *) nativeClearPassWord},
                    {"currentLength",    "()I", (void *) currentIndex},
                    {"nativeGetPassword",    "(I)Ljava/lang/String;", (void *) nativeGetPassword}

            };
    int ret = RegisterNativeMethods(env, CLASS_NAME_NATIVE,
                                    methods, count);
    LOGI("RegisterNativeMethods ret : %d", ret);
    return 0;
}

