#include "com_example_shannon_bmobmusic_MyApplication.h"
JNIEXPORT jstring JNICALL Java_com_example_shannon_bmobmusic_MyApplication_getBmobKeyFromC
  (JNIEnv *env, jobject obj){

        return  (*(*env)).NewStringUTF(env,"8b5beeb648f06a02c0ce693ba7dc40b5");


  }

