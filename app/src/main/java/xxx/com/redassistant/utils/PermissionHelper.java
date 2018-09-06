package xxx.com.redassistant.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/1 0001 09:55
 * 适配安卓6.0的权限管理
 */
public class PermissionHelper {

    /**
     * 请求权限
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param permissions 权限字符串数组
     */
    public static void requestPermission(Activity activity, int requestCode, String... permissions) {
        PermissionGen.with(activity)
                .addRequestCode(requestCode)
                .permissions(permissions)
                .request();
    }

    /**
     * 请求权限
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param permissions 权限字符串数组
     */
    public static void requestPermission(Fragment fragment, int requestCode, String... permissions) {
        PermissionGen.with(fragment)
                .addRequestCode(requestCode)
                .permissions(permissions)
                .request();
    }
}
