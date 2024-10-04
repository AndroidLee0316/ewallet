//
// Created by 杨自鉴 on 2019/3/4.
//
#include <memory.h>
#include <stdlib.h>
#include "pascUtil.h"

void be_attached_check() {
    try {
        const int bufSize = 1024;
        char filename[bufSize];
        char line[bufSize];
        int pid = getpid();
        sprintf(filename, "/proc/%d/status", pid);
        FILE *fd = fopen(filename, "r");
        if (fd != NULL) {
            while (fgets(line, bufSize, fd)) {
                if (strncmp(line, "TracerPid", 9) == 0) {
                    int statue = atoi(&line[10]);
                    LOGI("%s", line);
                    if (statue != 0) {
                        LOGI("be attached !! kill %d", pid);
                        fclose(fd);
                        int ret = kill(pid, SIGKILL);
                    }
                    break;
                }
            }
            fclose(fd);
        } else {
            LOGI("open %s fail...", filename);
        }
    } catch (...) {

    }
}

string *byteToHexStr(unsigned char byte_arr[], int arr_len) {
    string *hexStr = new string();
    for (int i = 0; i < arr_len; i++) {
        char hex1;
        char hex2;
        int value = byte_arr[i];
        int S = value / 16;
        int Y = value % 16;
        if (S >= 0 && S <= 9) {
            // 数字
            hex1 = (char) (48 + S);
        } else {
            // 小写 加 87 ，大小加 55
            hex1 = (char) (55 + S);
        }
        if (Y >= 0 && Y <= 9) {
            hex2 = (char) (48 + Y);
        } else {
            hex2 = (char) (55 + Y);
        }
        //字符串拼接
        *hexStr = *hexStr + hex1 + hex2;
    }
    return hexStr;
}



