package com.jiehun.component.rxpermission;

import android.Manifest;
import android.app.Activity;

import com.jiehun.component.utils.AbStringUtils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/** 提供权限申请
 * Created by zhouyao on 16-10-13.
 */

public class AbRxPermission {

    public static void checkPermissions(final Activity aty, final RxCallBack callBack, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(aty);
        rxPermissions
                .requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted){
                            callBack.onOk();
                        }else if (permission.shouldShowRequestPermissionRationale){
                            //已拒绝权限请求
                            callBack.onCancel();
                        }else {
                            //拒绝权限请求并不在询问
                            callBack.onNeverAsk(aty,getPermissionCnStr(permission.name));
                        }
                    }
                });
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测相机权限
     */
    public static void checkCameraPermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测录音权限
     */
    public static void checkRecordPermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测拨打电话权限
     */
    public static void checkPhonePermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.CALL_PHONE);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测定位权限
     */
    public static void checkLocationPermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测通讯录权限
     */
    public static void checkContactPermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.WRITE_CONTACTS);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测文件权限
     */
    public static void checkWriteStoragePermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /**
     *
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测读取文件权限
     */
    public static void checkReadStoragePermission(final Activity aty,final RxCallBack callBack){
        checkPermissions(aty,callBack,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测短信权限
     */
    public static void checkSmsPermission(final Activity aty, final RxCallBack callBack) {
        checkPermissions(aty, callBack, Manifest.permission.SEND_SMS);
    }

    private static String getPermissionCnStr(String permission){
        if (AbStringUtils.isNull(permission)){
            return "";
        }
        if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return "读写";
        }
        if (permission.equals(Manifest.permission.CAMERA)){
            return "打开摄像头";
        }
        if (permission.equals(Manifest.permission.RECORD_AUDIO)){
            return "使用话筒录音/通话录音";
        }
        if (permission.equals(Manifest.permission.CALL_PHONE)){
            return "拨打电话";
        }
        if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)){
            return "获取位置信息";
        }
        if (permission.equals(Manifest.permission.WRITE_CONTACTS)){
            return "写入/删除联系人信息";
        }
        if (permission.equals(Manifest.permission.SEND_SMS)){
            return "发送短信";
        }
        return "";
    }

}
