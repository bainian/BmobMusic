LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := util
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\StudioProjects\BmobMusic\app\src\main\jni\Android.mk \
	D:\StudioProjects\BmobMusic\app\src\main\jni\Application.mk \
	D:\StudioProjects\BmobMusic\app\src\main\jni\jni.c \
	D:\StudioProjects\BmobMusic\app\src\main\jni\util.c \

LOCAL_C_INCLUDES += D:\StudioProjects\BmobMusic\app\src\main\jni
LOCAL_C_INCLUDES += D:\StudioProjects\BmobMusic\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
