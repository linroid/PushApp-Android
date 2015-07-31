package com.linroid.pushapp.service;


import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linroid.pushapp.App;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import timber.log.Timber;


/**
 * Created by linroid on 7/27/15.<br/>
 * 免ROOT自动安装Apk
 * 部分代码是通过反编译酷市场得到的-.-
 *
 * @{http://www.infoq.com/cn/articles/android-accessibility-installing}
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ApkAutoInstallService extends AccessibilityService {
    public static final int AVALIABLE_API = 16;
    private static final String CLASS_NAME_APP_ALERT_DIALOG = "android.app.AlertDialog";
    private static final String CLASS_NAME_LENOVO_SAFECENTER = "com.lenovo.safecenter";
    private static final String CLASS_NAME_PACKAGE_INSTALLER = "com.android.packageinstaller";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_ACTIVITY = "com.android.packageinstaller.PackageInstallerActivity";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_PERMSEDITOR = "com.android.packageinstaller.PackageInstallerPermsEditor";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_PROGRESS = "com.android.packageinstaller.InstallAppProgress";
    private static final String CLASS_NAME_PACKAGE_UNINSTALLER_ACTIVITY = "com.android.packageinstaller.UninstallerActivity";
    private static final String CLASS_NAME_PACKAGE_UNINSTALLER_PROGRESS = "com.android.packageinstaller.UninstallAppProgress";
    private static final String CLASS_NAME_WIDGET_BUTTON = "android.widget.Button";
    private static final String CLASS_NAME_WIDGET_LISTVIEW = "android.widget.ListView";
    private static final String CLASS_NAME_WIDGET_TEXTVIEW = "android.widget.TextView";

    private static boolean enable = false;
    //TODO 使用软引用
    private static List<Pack> sInstallList = new ArrayList<>();
    private static List<Pack> sUninstallList = new ArrayList<>();

    @Inject
    public SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        App.from(this).component().inject(this);
    }

    @DebugLog
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    /**
     * 安装应用
     *
     * @param pack
     */
    public static void installPackage(Pack pack) {
        enable = true;

        if (pack != null && !sInstallList.contains(pack)) {
            sInstallList.add(pack);
        }
    }

    /**
     * 卸载应用
     *
     * @param pack
     */
    public static void uninstallApplication(Pack pack) {
        enable = true;
        if (pack != null && !sUninstallList.contains(pack)) {
            sUninstallList.add(pack);
        }
    }

    public static void reset() {
        enable = false;
        if (sInstallList != null) {
            sInstallList.clear();
        }
        if (sUninstallList != null) {
            sUninstallList.clear();
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (enable && avaliable()) {
            try {
                onProcessAccessibilityEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onInterrupt() {
    }

    protected boolean onKeyEvent(KeyEvent event) {
        return true;
    }


    private void onProcessAccessibilityEvent(AccessibilityEvent event) {
        if (event.getSource() == null) {
            return;
        }
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        String sourceText = event.getSource().getText() == null ? BuildConfig.VERSION_NAME : event.getSource().getText().toString().trim();
        if (packageName.equals(CLASS_NAME_PACKAGE_INSTALLER)) {
            if (isApplicationInstallEvent(event, className, sourceText)) {
                //准备安装
                onApplicationInstall(event);
            }
            if (className.equalsIgnoreCase(CLASS_NAME_APP_ALERT_DIALOG)) {
                //弹窗，如授权操作
                processAlertDialogEvent(event, className, sourceText);
            } else if (isApplicationUninstalledEvent(event, className, sourceText)) {
                //卸载完成
                onApplicationUninstalled(event);
            } else if (isApplicationInstalledEvent(event, className, sourceText)) {
                //安装完成
                onApplicationInstalled(event);
            } else if (isApplicationUninstallEvent(event, className, sourceText)) {
                //准备卸载
                onApplicationUninstall(event);
            } else if (isApplicationInstallEvent(event, className, sourceText)) {
                //准备安装
                onApplicationInstall(event);
            } else if (hasAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_install_blocked))) {
                //准备安装
                onApplicationInstall(event, false);
            }
        } else if (packageName.equals(CLASS_NAME_LENOVO_SAFECENTER)) {
            processLenovoEvent(event, className, sourceText);
        }
    }

    /**
     * 是否是准备安装事件
     *
     * @param event
     * @param className
     * @param sourceText
     * @return
     */
    private boolean isApplicationInstallEvent(AccessibilityEvent event, String className, String sourceText) {
        return className.equalsIgnoreCase(CLASS_NAME_PACKAGE_INSTALLER_ACTIVITY)
                || sourceText.contains(getString(R.string.btn_accessibility_install));
    }


    /**
     * 是否是安装完成事件
     *
     * @param event
     * @param className
     * @param sourceText
     * @return
     */
    private boolean isApplicationInstalledEvent(AccessibilityEvent event, String className, String sourceText) {
        return sourceText.equalsIgnoreCase(getString(R.string.btn_accessibility_open))
                || sourceText.equalsIgnoreCase(getString(R.string.btn_accessibility_run))
                || sourceText.contains(getString(R.string.str_accessibility_installed))
                || sourceText.contains(getString(R.string.str_accessibility_installed2))
                || sourceText.contains(getString(R.string.str_accessibility_installed3))

                || hasAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_installed))
                || hasAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_installed2))
                || hasAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_installed3));
    }

    /**
     * 是否是卸载事件
     *
     * @param event
     * @param className
     * @param sourceText
     * @return
     */
    private boolean isApplicationUninstallEvent(AccessibilityEvent event, String className, String sourceText) {
        return className.equalsIgnoreCase(CLASS_NAME_PACKAGE_UNINSTALLER_ACTIVITY)
                || sourceText.contains(getString(R.string.str_accessibility_uninstall));
    }

    /**
     * 是否是卸载完成事件
     *
     * @param event
     * @param className
     * @param sourceText
     * @return
     */
    private boolean isApplicationUninstalledEvent(AccessibilityEvent event, String className, String sourceText) {


        return hasAccessibilityNodeInfoByText(event, R.string.str_accessibility_uninstalled)
                || hasAccessibilityNodeInfoByText(event, R.string.str_accessibility_uninstalled2)
                || hasAccessibilityNodeInfoByText(event, R.string.str_accessibility_uninstalled3);
    }

    /**
     * 弹窗，如安装失败、卸载等
     *
     * @param event
     * @param className
     * @param sourceText
     */
    private void processAlertDialogEvent(AccessibilityEvent event, String className, String sourceText) {
        Timber.d("出现弹窗");
        String eventText = event.getText().toString();

        if (eventText.contains(getString(R.string.str_accessibility_error))) {
            onInstallFail(event);
        } else if (!eventText.contains(getString(R.string.str_accessibility_uninstall))) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, CLASS_NAME_WIDGET_BUTTON, getString(R.string.btn_accessibility_ok));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
            }
        }
    }

    private void processLenovoEvent(AccessibilityEvent event, String className, String sourceText) {
        Timber.d("联想");
        if (sourceText.contains(getString(R.string.str_accessibility_installed3))) {
            onApplicationInstalled(event);
        } else if (sourceText.contains(getString(R.string.str_accessibility_uninstalled3))) {
            onApplicationUninstalled(event);
        } else {
            onApplicationInstall(event, CLASS_NAME_WIDGET_TEXTVIEW);
        }
    }


    /**
     * 安装失败
     *
     * @param event
     */
    private void onInstallFail(AccessibilityEvent event) {
        Timber.e("安装失败");
    }

    private void onApplicationInstall(AccessibilityEvent event) {
        onApplicationInstall(event, DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON, true);
    }

    private void onApplicationInstall(AccessibilityEvent event, boolean maybeValidate) {
        onApplicationInstall(event, DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON, maybeValidate);
    }

    private void onApplicationInstall(AccessibilityEvent event, String nodeClassName) {
        onApplicationInstall(event, nodeClassName, true);
    }

    /**
     * 准备安装
     *
     * @param event
     */
    private void onApplicationInstall(AccessibilityEvent event, String nodeClassName, boolean maybeValidate) {
        Timber.d("准备安装");
        if (!maybeValidate || isValidPackageEvent(event, sInstallList)) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, nodeClassName, getString(R.string.btn_accessibility_install));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, nodeClassName, getString(R.string.btn_accessibility_allow_once));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, nodeClassName, getString(R.string.btn_accessibility_next));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                onApplicationInstall(event);
                nodeInfo.recycle();
            }
        }
    }

    /**
     * 安装完成
     *
     * @param event
     */
    private void onApplicationInstalled(AccessibilityEvent event) {
        Timber.d("安装完成");
        AccessibilityNodeInfo validInfo = getValidAccessibilityNodeInfo(event, sInstallList);
        if (validInfo != null && processApplicationInstalled(event) && sInstallList != null && validInfo.getText() != null) {
            if (preferences.getBoolean(Constants.SP_AUTO_OPEN, true)) {
                AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event,
                        DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON,
                        getString(R.string.btn_accessibility_open));
                if (nodeInfo != null) {
                    performClick(nodeInfo);
                    nodeInfo.recycle();
                    return;
                }
            }
            String label = validInfo.getText().toString();
            removePackFromList(sInstallList, label);
            validInfo.recycle();
        }
    }

    private void removePackFromList(List<Pack> list, String label) {
        for (Pack pack : list) {
            if (pack.getAppName().equals(label)) {
                sInstallList.remove(pack);
            }
        }
    }

    /**
     * 安装完成之后的处理操作
     *
     * @param event
     * @return
     */
    private boolean processApplicationInstalled(AccessibilityEvent event) {
        AccessibilityNodeInfo eventInfo = null;
        if (event != null && event.getSource() != null) {
            eventInfo = event.getSource();
        } else if (VERSION.SDK_INT >= 16) {
            eventInfo = getRootInActiveWindow();
        }
        boolean success = false;
        if (eventInfo != null) {
            eventInfo.recycle();
            success = performEventAction(eventInfo, null, getString(R.string.btn_accessibility_ok), false)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_done), false)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_complete), false)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_know), false)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_run), true)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_open), true);
        }
        return success;
    }

    /**
     * 准备卸载
     *
     * @param event
     */
    private void onApplicationUninstall(AccessibilityEvent event) {
        onApplicationUninstall(event, DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON);
    }

    private void onApplicationUninstall(AccessibilityEvent event, String nodeClassName) {
        Timber.d("准备卸载");
        if (isValidPackageEvent(event, sUninstallList)) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, nodeClassName, getString(R.string.btn_accessibility_uninstall));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, nodeClassName, getString(R.string.btn_accessibility_ok));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
            }
        }
    }

    /**
     * 卸载完成
     *
     * @param event
     */
    private void onApplicationUninstalled(AccessibilityEvent event) {
        Timber.d("卸载完成");
        AccessibilityNodeInfo validInfo = getValidAccessibilityNodeInfo(event, sUninstallList);
        if (validInfo != null && processApplicationUninstalled(event)
                && sUninstallList != null
                && validInfo.getText() != null) {
            String label = validInfo.getText().toString();
            removePackFromList(sUninstallList, label);
            validInfo.recycle();
        }
    }

    /**
     * 处理卸载应用
     *
     * @param event
     * @return
     */
    private boolean processApplicationUninstalled(AccessibilityEvent event) {
        AccessibilityNodeInfo eventInfo = null;
        if (event != null && event.getSource() != null) {
            eventInfo = event.getSource();
        } else {
            eventInfo = getRootInActiveWindow();
        }
        boolean success = false;
        if (eventInfo != null) {
            success = performEventAction(eventInfo, null, getString(R.string.btn_accessibility_ok), false)
                    || performEventAction(eventInfo, null, getString(R.string.btn_accessibility_know), false);
            eventInfo.recycle();
        }
        return success;
    }

    private boolean hasAccessibilityNodeInfoByText(AccessibilityEvent event, int resId) {
        return hasAccessibilityNodeInfoByText(event, getString(resId));
    }

    /**
     * 通过文本判断是否存在AccessibilityNodeInfo
     *
     * @param event
     * @param text
     * @return
     */
    private boolean hasAccessibilityNodeInfoByText(AccessibilityEvent event, String text) {
        List<AccessibilityNodeInfo> nodes = null;
        if (event != null && event.getSource() != null) {
            nodes = event.getSource().findAccessibilityNodeInfosByText(text);
        } else if (VERSION.SDK_INT >= 16) {
            AccessibilityNodeInfo info = getRootInActiveWindow();
            if (info != null) {
                nodes = info.findAccessibilityNodeInfosByText(text);
            }
        }
        return !(nodes == null || nodes.size() <= 0);
    }

    /**
     * 判断是否是指定包的事件
     *
     * @param event
     * @param validPackageList
     * @return
     */
    private boolean isValidPackageEvent(AccessibilityEvent event, List<Pack> validPackageList) {
        return getValidAccessibilityNodeInfo(event, validPackageList) != null;
    }

    /**
     * 获取有效的的AccessibilityNodeInfo
     *
     * @param event
     * @param validPackageList
     * @return
     */
    private AccessibilityNodeInfo getValidAccessibilityNodeInfo(AccessibilityEvent event, List<Pack> validPackageList) {
        if (validPackageList != null && validPackageList.size() > 0) {
            for (Pack pack : validPackageList) {
                AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, CLASS_NAME_WIDGET_TEXTVIEW, pack.getAppName());
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    /**
     * 通过文本获取AccessibilityNodeInfo 对象
     *
     * @param event
     * @param className
     * @param text
     * @return
     */
    private AccessibilityNodeInfo getAccessibilityNodeInfoByText(AccessibilityEvent event, String className, String text) {
        List<AccessibilityNodeInfo> nodes = null;
        if (event != null && event.getSource() != null) {
            nodes = event.getSource().findAccessibilityNodeInfosByText(text);
        } else {
            AccessibilityNodeInfo info = getRootInActiveWindow();
            if (info != null) {
                nodes = info.findAccessibilityNodeInfosByText(text);
            }
        }
        if (nodes != null && nodes.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                String nodeText = nodeInfo.getText() == null ? BuildConfig.VERSION_NAME : nodeInfo.getText().toString();
                if (nodeInfo.getClassName().equals(className) && nodeText.equalsIgnoreCase(text)) {
                    return nodeInfo;
                }
                nodeInfo.recycle();
            }
        }
        return null;
    }

    /**
     * 进行动作事件操作
     *
     * @param info
     * @param className
     * @param text
     * @param isGlobalAction 是否是全局动作
     * @return 是否执行成功
     */
    private boolean performEventAction(AccessibilityNodeInfo info, String className, String text, boolean isGlobalAction) {
        if (info == null) {
            return false;
        }
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByText(text);
        if (nodes != null && nodes.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                String nodeText = nodeInfo.getText() == null ? null : nodeInfo.getText().toString();
                boolean isValidClassName = true;
                if (className != null) {
                    isValidClassName = className.equalsIgnoreCase(nodeInfo.getClassName() == null ? null : nodeInfo.getClassName().toString());
                }
                if (isValidClassName && text.equalsIgnoreCase(nodeText)) {
                    if (isGlobalAction) {
                        //返回
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    } else {
                        performClick(nodeInfo);
                    }
                    return true;
                }
                nodeInfo.recycle();
            }
        }
        return false;
    }

    /**
     * 进行点击操作
     *
     * @param node
     * @return 是否点击成功
     */
    private boolean performClick(AccessibilityNodeInfo node) {
        return node != null
                && node.isEnabled()
                && node.isClickable()
                && node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public static boolean avaliable() {
        return VERSION.SDK_INT >= AVALIABLE_API;
    }
}