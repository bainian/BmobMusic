LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := util
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\AndroidStudioProjects\BmobMusic\app\src\main\jni\Android.mk \
	D:\AndroidStudioProjects\BmobMusic\app\src\main\jni\Application.mk \
	D:\AndroidStudioProjects\BmobMusic\app\src\main\jni\jni.c \
	D:\AndroidStudioProjects\BmobMusic\app\src\main\jni\util.c \

LOCAL_C_INCLUDES += D:\AndroidStudioProjects\BmobMusic\app\src\main\jni
LOCAL_C_INCLUDES += D:\AndroidStudioProjects\BmobMusic\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
