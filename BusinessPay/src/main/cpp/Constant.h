
#ifndef ANDROIDSAFEDEMO_CONSTANT_H
#define ANDROIDSAFEDEMO_CONSTANT_H

const char *const CLASS_NAME_NATIVE = "com/pasc/lib/enc/Epwd";


const char *const messageDigestClassName = "java/security/MessageDigest";
const char *const getInstanceMethod = "getInstance";
const char *const getInstanceMethodSig = "(Ljava/lang/String;)Ljava/security/MessageDigest;";
const char *const updateMethod = "update";
const char *const updateMethodSig = "([B)V";

const char *const digestMethod = "digest";
const char *const digestMethodSig = "()[B";

const char *const stringClass = "java/lang/String";

const char *const getBytesMethod = "getBytes";
const char *const getBytesMethodSig = "()[B";

const char *const bytesToHexMethod = "bytesToHex";
const char *const bytesToHexMethodSig = "([B)Ljava/lang/String;";

const char * const onFinishPwdMethod="onFinishPwd";
const char * const onFinishPwdMethodSig="(Ljava/lang/String;Z)V";


const static  int sm3ModeTag=0;
const static  int sha256ModeTag=1;
const static  int simpleModeTag=2;


#endif //ANDROIDSAFEDEMO_CONSTANT_H
