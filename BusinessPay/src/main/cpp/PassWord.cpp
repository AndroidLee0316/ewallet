//
// Created by 杨自鉴 on 2019/3/4.
//
#include <memory.h>
#include "PassWord.h"
#include "Constant.h"
#include "sha256.h"
#include "pascUtil.h"
#include <string>
#include <vector>
#include "sm3.h"

using namespace std;
vector<string> pVec;
bool isFinish = false;
int currentIndex = 0;
/**buffer 需要放到全局，局部会被回收的***/
std::string buffer;
const int invalidatePwdSize = 20;
const char *sm3Arr[invalidatePwdSize] = {
        "CDCD1FFCC4080D60AB9972747825166EE09AA3C4FC5F2C384609C8139E934BAA",
        "6E53D0E8B428D5C4044074C6A2AF589F207E755D13260878CDAE8AE97EA2BC4C",
        "9C6A3B4E5C0024395E138B7ED02BF1ED73A032DA080362A1B1A281F3C7100496",
        "CDF614FE04D1226DDEAD5F79ACAE178A0221543FFDB2F68001EE1FF751B36001",
        "AF4D04F71A86CB820D8FD60CDFC5EF2956E4688548E58592B7B75FA9A55E63CC",
        "22E35AB27BC0EBF21A9DAF7808D4856E9B625982BAB6E0E516FF76202ED72D24",
        "EC5339965E60FBA7C6C62C4F7C56DD8D94C1FF4E77D825A131C875E23C7939B7",
        "949D4524F87F71958715662572A36ABC0454E8AB5A054FFE492F417D34435C15",
        "3526849C11453AD7372EB40B5E6253D53EE1B171006C5770C1A4147A57CAAEAE",
        "BAA74BB9B354C210531E6FC480CCECAE4A160BCBDC7581E937564F3BB8B0B5D2",
        "21FC189B3F8E16A472164CDDC4E031C640DDED197BB54BB502103F9930850AD7",
        "36D07CDBDFA5A5CBD35CD02447E58E4439C19948A60834B51915EAD9A755F666",
        "1B4D522C895C43BB8D328F45B1526B80DF1E8186A4D0C5067923B25FC2C4BF6B",
        "5CC3C8839FE289670EEDC4A4B38AC6DAD77A10C635A8BE8DF3A1D569A0E2BD4B",
        "87983CA861FC4EF0EC7CFE06245EB1DF53E240F04A78A11E996870747D0AD97C",
        "ED799388422FDCBD6AD36ACD6520354E2356B13761AB4518B5D885E67F57CFF0",
        "243414AB3026B26DAD5FAEE780A3AEEA18134A75BD80DA1B2B90603E1040ACE6",
        "EA4B8D98D7A64D350839BC5F25E64CA6D7ECFDC529139465DFE2907A3C971ECC",
        "90457A7786C202FDBC7AD946A6E8F1BAD88ACCC2341AA6F4D4DAE8EAD59D210F",
        "657FA16B093F6A5B962A4B5F7C8C9014F6F5351316DD7CAE414EF5C5E28239FF"
};

const char *simpleArr[invalidatePwdSize] = {
        "000000",
        "111111",
        "222222",
        "333333",
        "444444",
        "555555",
        "666666",
        "777777",
        "888888",
        "999999",
        "123456",
        "234567",
        "345678",
        "456789",
        "012345",
        "987654",
        "876543",
        "765432",
        "654321",
        "543210"
};

/****String to byte[]****/
static jbyteArray jString2jByteArray(JNIEnv *env, jstring str) {
    jclass jcl = env->FindClass(stringClass);
    jmethodID instance = env->GetMethodID(jcl, getBytesMethod, getBytesMethodSig);
    return static_cast<jbyteArray>(env->CallObjectMethod(str, instance));
}


/****调用android 自带的hash256****/
static jbyteArray sha256Android(JNIEnv *env, jstring pwd_) {
    jbyteArray array = jString2jByteArray(env, pwd_);
    jclass jcl = env->FindClass(messageDigestClassName);
    jmethodID instance = env->GetStaticMethodID(jcl, getInstanceMethod, getInstanceMethodSig);
    jobject obj = env->CallStaticObjectMethod(jcl, instance, env->NewStringUTF("SHA-256"));

    jmethodID update = env->GetMethodID(jcl, updateMethod, updateMethodSig);
    env->CallVoidMethod(obj, update, array);
    jmethodID digest = env->GetMethodID(jcl, digestMethod, digestMethodSig);
    jbyteArray byteData = jbyteArray(env->CallObjectMethod(obj, digest));
    return byteData;
}

static jstring JNICALL securityPwd(JNIEnv *env, jclass javaCls, jstring pwd_) {
    jbyteArray byteData = sha256Android(env, pwd_);
    jmethodID instance = env->GetStaticMethodID(javaCls, bytesToHexMethod, bytesToHexMethodSig);
    return (jstring) env->CallStaticObjectMethod(javaCls, instance, byteData);
}


string *sha256String(const char *pwd) {
    BYTE buf[SHA256_BLOCK_SIZE];
    SHA256_CTX ctx;
    int len = strlen(pwd);
    sha256_init(&ctx);
    sha256_update(&ctx, pwd, len);
    sha256_final(&ctx, buf);
    string *hexStr = byteToHexStr(buf, SHA256_BLOCK_SIZE);
    return hexStr;
}

string *sm3String(const char *data) {
    int len = strlen(data);
    unsigned char output[SM3_DIGEST_LENGTH];
    LOGI("%s, %d", data, len);
    LOGI("%s", (const unsigned char *) data);

    sm3((const unsigned char *) data, len, output);
    LOGI("%s", output);

    string *sm3Str = byteToHexStr(output, SM3_DIGEST_LENGTH);
    return sm3Str;
}

void clearPassWord() {
    buffer.clear();
    pVec.clear();
    isFinish = false;
    currentIndex = 0;
}

void removePassWord() {
    if (isFinish) {
        return;
    }
    if (currentIndex <= 0) {
        return;
    }
    currentIndex--;
    pVec.pop_back();
}

const char *getPassword() {
    buffer.clear();
    for (int i = 0; i < pVec.size(); ++i) {
        const char *str = pVec[i].c_str();
        if (str) {
            buffer.append(str);
            LOGI(str);
        }
    }
    return buffer.c_str();
}

void addPassWordSha256(JNIEnv *env, jobject instance, const char *originPwd
) {
    string *newHashPwdString = sha256String(originPwd);
    pVec.push_back(newHashPwdString->c_str());
    // newHashPwdString 是 byte2hex是new 出来的，需要手动释放
    if (newHashPwdString != NULL) {
        delete newHashPwdString;
        newHashPwdString = NULL;
    }
}

void
addPassWordSM3(JNIEnv *env, jobject instance, const char *originPwd) {
    string *newHashPwdString = sm3String(originPwd);
    pVec.push_back(newHashPwdString->c_str());
    // newHashPwdString 是 byte2hex是new 出来的，需要手动释放
    LOGI("%s = %s", originPwd, newHashPwdString->c_str());
    if (newHashPwdString != NULL) {
        delete newHashPwdString;
        newHashPwdString = NULL;
    }
}

void addPassWordSimple(JNIEnv *env, jobject instance, const char *originPwd){
    pVec.push_back(originPwd);
}

void pwdCall(JNIEnv *env, jobject instance, const char *pwd, jint mode) {
    if (mode == sm3ModeTag) {
        addPassWordSM3(env, instance, pwd);
    } else if (mode == sha256ModeTag) {
        addPassWordSha256(env, instance, pwd);
    } else {
        addPassWordSimple(env, instance, pwd);
    }
}


void
addPassWord(JNIEnv *env, jobject instance, const char *originPwd, jint mode) {
    if (isFinish) {
        return;
    }
    if (!originPwd) {
        return;
    }
    currentIndex++;
    std::string strData;

    if (mode == sm3ModeTag || mode==sha256ModeTag) {
        //0->fu 1->pt   2->jm    3->mp   4->ts   5->gl   6->io   7->sb  8->hx  9->wr
        if (strcmp("0", originPwd) == 0) {
            strData.append("fu");
        } else if (strcmp("1", originPwd) == 0) {
            strData.append("pt");
        } else if (strcmp("2", originPwd) == 0) {
            strData.append("jm");
        } else if (strcmp("3", originPwd) == 0) {
            strData.append("mp");
        } else if (strcmp("4", originPwd) == 0) {
            strData.append("ts");
        } else if (strcmp("5", originPwd) == 0) {
            strData.append("gl");
        } else if (strcmp("6", originPwd) == 0) {
            strData.append("io");
        } else if (strcmp("7", originPwd) == 0) {
            strData.append("sb");
        } else if (strcmp("8", originPwd) == 0) {
            strData.append("hx");
        } else if (strcmp("9", originPwd) == 0) {
            strData.append("wr");
        } else {
            strData.append(originPwd);
        }
    }else{
        strData.append(originPwd);

    }
    const char *pwd = strData.data();
    pwdCall(env, instance, pwd, mode);

}

jstring getNativePassword(JNIEnv *env, jobject instance,jint mode) {
    const char *totalPwd = getPassword();
    string *newTotalString =NULL;
    const char *tt =NULL;
    if (mode==sm3ModeTag){
        newTotalString = sm3String(totalPwd);
        tt = newTotalString->c_str();
    }else if (mode==sha256ModeTag){
        newTotalString = sha256String(totalPwd);
        tt = newTotalString->c_str();
    }else{
        tt=totalPwd;
    }
    bool isInvalidatePwd = false;
    if (mode==sm3ModeTag) {
        for (int i = 0; i < invalidatePwdSize; ++i) {
            const char *invalidatePwd = sm3Arr[i];
            if (strcmp(invalidatePwd, tt) == 0) {
                isInvalidatePwd = true;
                break;
            }
        }
    } else if (mode==simpleModeTag){
        for (int i = 0; i < invalidatePwdSize; ++i) {
            const char *invalidatePwd = simpleArr[i];
            if (strcmp(invalidatePwd, tt) == 0) {
                isInvalidatePwd = true;
                break;
            }
        }
    }

    string pwdBuffer;
    pwdBuffer.append(tt).append("#split#").append(isInvalidatePwd?"true":"false");
    jstring hashTotalPwdStr = env->NewStringUTF(pwdBuffer.c_str());
    // newTotalString 是 byte2hex是new 出来的，需要手动释放
    if (newTotalString != NULL) {
        delete newTotalString;
        newTotalString = NULL;
    }
    return hashTotalPwdStr;
};

int currentLength() {
    return currentIndex;
}
