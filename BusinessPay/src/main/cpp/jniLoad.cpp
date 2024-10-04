//
// Created by 杨自鉴 on 2018/11/14.
//
#include "jniLoad.h"
#include "pascUtil.h"
#include "Constant.h"
__attribute__((visibility ("default")))
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void * /*reserved*/) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        RegisterPwdNativeMethods(env);
    }
    // release 版本反调试
#ifdef RELEASE
    be_attached_check();
#endif
    return JNI_VERSION_1_6;
}


__attribute__((visibility ("default")))
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void * /*reserved*/) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        UnregisterNativeMethods(env, CLASS_NAME_NATIVE);

    }

}

jint RegisterNativeMethods(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                           int numMethods) {
    jint result = JNI_ERR;
    if (jclass clazz = env->FindClass(className)) {
        result = env->RegisterNatives(clazz, methods, numMethods);
        env->DeleteLocalRef(clazz);
    }
    return result;
}

jint UnregisterNativeMethods(JNIEnv *env, const char *className) {
    jint result = JNI_ERR;
    if (jclass clazz = env->FindClass(className)) {
        result = env->UnregisterNatives(clazz);
        env->DeleteLocalRef(clazz);
    }
    return result;
}