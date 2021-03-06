/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.service;


import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linroid.pushapp.App;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.util.AndroidUtil;
import com.linroid.pushapp.util.BooleanPreference;
import com.linroid.pushapp.util.DeviceUtil;
import com.linroid.pushapp.util.IntentUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import hugo.weaving.DebugLog;
import timber.log.Timber;


/**
 * Created by linroid on 7/27/15.<br/>
 * 免ROOT自动安装Apk<br/>
 *
 * See <a href="http://www.infoq.com/cn/articles/android-accessibility-installing">使用Android Accessibility实现免Root自动批量安装功能</a>
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ApkAutoInstallService extends AccessibilityService {
    public static final int AVAILABLE_API = 16;

    public static final int INSTALL_TIMEOUT = 60000;
    public static final int UNINSTALL_TIMEOUT = 5000;
    public static final int INSTALL_RETRY_INTERVAL = 1000;

    private static final String CLASS_NAME_APP_ALERT_DIALOG = "android.app.AlertDialog";
    private static final String CLASS_NAME_LENOVO_SAFECENTER = "com.lenovo.safecenter";
    private static final String CLASS_NAME_PACKAGE_INSTALLER = "com.android.packageinstaller";
    private static final String CLASS_NAME_GOOGLE_PACKAGE_INSTALLER = "com.google.android.packageinstaller";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_ACTIVITY = "com.android.packageinstaller.PackageInstallerActivity";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_PERMSEDITOR = "com.android.packageinstaller.PackageInstallerPermsEditor";
    private static final String CLASS_NAME_PACKAGE_INSTALLER_PROGRESS = "com.android.packageinstaller.InstallAppProgress";
    private static final String CLASS_NAME_PACKAGE_UNINSTALLER_ACTIVITY = "com.android.packageinstaller.UninstallerActivity";
    private static final String CLASS_NAME_PACKAGE_UNINSTALLER_PROGRESS = "com.android.packageinstaller.UninstallAppProgress";
    private static final String CLASS_NAME_WIDGET_BUTTON = "android.widget.Button";
    private static final String CLASS_NAME_WIDGET_LISTVIEW = "android.widget.ListView";
    private static final String CLASS_NAME_WIDGET_TEXTVIEW = "android.widget.TextView";

    private static boolean enable = false;

    // 需要先卸载再安装的应用
    private static SparseArray<Pack> sPrepareList = new SparseArray<>();
    private static SparseArray<Pack> sInstallList = new SparseArray<>();
    private static SparseArray<Pack> sUninstallList = new SparseArray<>();

    @Inject
    @Named(Constants.SP_AUTO_OPEN)
    public BooleanPreference autoOpen;
    @Inject
    NotificationManager notificationManager;

    Handler handler;

    Runnable handleInstallTimeout = new Runnable() {
        @Override
        public void run() {
            AccessibilityNodeInfo validInfo = getRootInActiveWindow();
            if (validInfo != null) {
                if (autoOpen.getValue()) {
                    boolean openSuccess = openAfterInstalled(null);
                    Timber.d("成功打开？%s", openSuccess);
                }
                String label = validInfo.getText().toString();
                removePackFromListByAppName(sInstallList, label, true);
                validInfo.recycle();
            }
        }
    };

    Runnable handleUninstallTimeout = new Runnable() {
        @Override
        public void run() {
            // 卸载超时，先尝试点击完成（即卸载成功），然后再看有没有待装的应用（即先卸载再安装的）
            AccessibilityNodeInfo eventInfo = getRootInActiveWindow();
            if (eventInfo != null && sUninstallList != null
                    && eventInfo.getText() != null) {
                String label = eventInfo.getText().toString();
                removePackFromListByAppName(sUninstallList, label); // waring： 此处可能找不到
                boolean success = performEventAction(eventInfo, getString(R.string.btn_accessibility_ok), false)
                        || performEventAction(eventInfo, getString(R.string.btn_accessibility_know), false);
                eventInfo.recycle();
            }
            processPrepareInstall(null);
        }
    };


    /**
     * 安装应用
     *
     * @param pack
     */
    public static void addInstallPackage(Pack pack) {
        enable = true;

        if (pack != null) {
            sInstallList.put(pack.getId(), pack);
        }
    }

    /**
     * 卸载应用
     *
     * @param pack
     */
    public static void addUninstallApplication(Pack pack) {
        enable = true;
        if (pack != null) {
            sUninstallList.put(pack.getId(), pack);
        }
    }

    /**
     * 先卸载再安装的应用
     *
     * @param pack
     */
    public static void addPrepareInstallApplication(Pack pack) {
        enable = true;
        if (pack != null) {
            sUninstallList.put(pack.getId(), pack);
            sPrepareList.put(pack.getId(), pack);
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

    public static boolean available() {
        return VERSION.SDK_INT >= AVAILABLE_API;
    }


    /**
     * 处理卸载后的安装
     */
    private void processPrepareInstall(String label) {
        Pack pack = null;
        if (sPrepareList.size() == 0) {
            return;
        }

        if (label != null) {
            for (int i = 0; i < sPrepareList.size(); i++) {
                int key = sPrepareList.keyAt(i);
                pack = sPrepareList.get(key);
                sPrepareList.remove(key);
            }
        }

        if (pack == null) {
            pack = sPrepareList.get(sPrepareList.size() - 1);
            sPrepareList.removeAt(sPrepareList.size() - 1);
        }

        addInstallPackage(pack);
        startActivity(IntentUtil.installApk(pack.getPath()));
    }

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        App.from(this).component().inject(this);
        handler = new Handler();
    }

    @DebugLog
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (enable && available()) {
            try {
                onProcessAccessibilityEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @DebugLog
    public void onInterrupt() {
    }

    @DebugLog
    @Override
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
        if (packageName.equals(CLASS_NAME_PACKAGE_INSTALLER) || packageName.equals(CLASS_NAME_GOOGLE_PACKAGE_INSTALLER)) {
            if (isApplicationInstallEvent(event, className, sourceText)) {
                // 准备安装
                onApplicationInstall(event);
            } else if (hasAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_install_blocked))) {
                // 准备安装
                onApplicationInstall(event, false);
            } else if (isApplicationInstalledEvent(event, className, sourceText)) {
                // 安装完成
                onApplicationInstalled(event);
            } else if (isApplicationInstallFailedEvent(event, className, sourceText)) {
                // 安装失败
                onInstallFail(event);
            } else if (className.equalsIgnoreCase(CLASS_NAME_APP_ALERT_DIALOG)) {
                // 弹窗，如授权操作
                processAlertDialogEvent(event, className, sourceText);
            } else if (isApplicationUninstallEvent(event, className, sourceText)) {
                // 准备卸载
                onApplicationUninstall(event);
            } else if (isApplicationUninstalledEvent(event, className, sourceText)) {
                // 卸载完成
                onApplicationUninstalled(event);
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
     * 是否是安装失败界面
     *
     * @param event
     * @param className
     * @param sourceText
     * @return
     */
    private boolean isApplicationInstallFailedEvent(AccessibilityEvent event, String className, String sourceText) {
        return className.equalsIgnoreCase(CLASS_NAME_WIDGET_TEXTVIEW)
                && (sourceText.contains(getString(R.string.str_accessibility_installed4))
                || sourceText.contains(getString(R.string.str_accessibility_installed5)));
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
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_ok));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
            }
        } else if (eventText.contains(getString(R.string.str_accessibility_replace))
                || eventText.contains(getString(R.string.str_accessibility_replace1))) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_ok));
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
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        // 从 sInstallList 转移到 prepare 的流程（先卸载再安装
        if (sInstallList != null && sInstallList.size() > 0) {
            AccessibilityNodeInfo validInfo = getValidAccessibilityNodeInfo(event, sInstallList);
            if (validInfo != null && processApplicationUninstalled(event)
                    && validInfo.getText() != null) {
                String label = validInfo.getText().toString();
                removePackFromListByAppName(sInstallList, label);
                for (int i = 0; i < sInstallList.size(); i++) {
                    int key = sInstallList.keyAt(i);
                    Pack pack = sInstallList.get(key);
                    if (pack.getAppName().equals(label)) {
                        addPrepareInstallApplication(pack);
                        sInstallList.remove(key);
                        startActivity(IntentUtil.uninstallApp(pack.getPath()));
                        break;
                    }
                }
                validInfo.recycle();
            }
        }

    }

    private void onApplicationInstall(AccessibilityEvent event) {
        onApplicationInstall(event, DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON,
                true, false);
    }

    private void onApplicationInstall(AccessibilityEvent event, boolean maybeValidate) {
        onApplicationInstall(event, DeviceUtil.isFlyme() ? CLASS_NAME_WIDGET_TEXTVIEW : CLASS_NAME_WIDGET_BUTTON,
                maybeValidate, false);
    }

    private void onApplicationInstall(AccessibilityEvent event, String nodeClassName) {
        onApplicationInstall(event, nodeClassName, true, false);
    }

    /**
     * 准备安装
     *
     * @param event Accessibility 捕获到的事件
     * @param nodeClassName 需要遍历的节点是什么类型的
     * @param maybeValidate 是否需要校验
     * @param isRetry 是否是重试
     */
    private void onApplicationInstall(final AccessibilityEvent event, final String nodeClassName,
                                      final boolean maybeValidate, boolean isRetry) {
        Timber.d("准备安装");

        // 设置安装超时（即最后未捕获到安装失败的话）
        handler.postDelayed(handleInstallTimeout, INSTALL_TIMEOUT);

        if (!maybeValidate || isValidPackageEvent(event, sInstallList)) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_install));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_allow_once));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                nodeInfo.recycle();
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_next));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                onApplicationInstall(event);
                nodeInfo.recycle();
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.str_accessibility_replace_continue));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                onApplicationInstall(event);
                nodeInfo.recycle();
            }

            // 低端机型，页面还没加载出来，事件却已经先发到了，故加入延迟机制
            if (nodeInfo == null && !isRetry) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onApplicationInstall(event, nodeClassName,maybeValidate, true);
                    }
                }, INSTALL_RETRY_INTERVAL);
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

        // 取消安装超时的操作
        handler.removeCallbacks(handleInstallTimeout);

        AccessibilityNodeInfo validInfo = getValidAccessibilityNodeInfo(event, sInstallList);
        if (validInfo != null) {
            if (autoOpen.getValue()) {
                boolean openSuccess = openAfterInstalled(event);
                Timber.d("成功打开？%s", openSuccess);
            }
            String label = validInfo.getText().toString();
            removePackFromListByAppName(sInstallList, label, true);
            validInfo.recycle();
        }
    }

    private void removePackFromListByAppName(SparseArray<Pack> sInstallList, String label) {
        removePackFromListByAppName(sInstallList, label, false);
    }

    /**
     * 通过应用名称从列表中移除
     *
     * @param list             保存的列表
     * @param appName          应用名称
     * @param becauseInstalled 是因为安装完成了才移除的
     */
    private void removePackFromListByAppName(SparseArray<Pack> list, String appName, boolean becauseInstalled) {
        for (int i = 0; i < list.size(); i++) {
            int key = list.keyAt(i);
            Pack pack = list.get(key);
            if (pack.getAppName().equals(appName)) {
                sInstallList.remove(key);
                if (becauseInstalled) {
                    showInstalledNotification(pack);
                }
                break;
            }
        }
        if (sInstallList.size() == 0 && sUninstallList.size() == 0) {
            enable = false;
        }
    }

    /**
     * 显示安装完成的通知
     *
     * @param pack
     */
    private void showInstalledNotification(Pack pack) {
        PendingIntent intent = PendingIntent.getActivity(this, 0, AndroidUtil.getOpenAppIntent(this, pack.getPackageName()), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.msg_download_title, pack.getAppName(), pack.getVersionName()))
                .setSmallIcon(R.drawable.ic_stat_complete)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setContentText(getString(R.string.msg_install_complete));

        AndroidUtil.openApplication(this, pack.getPackageName());
        notificationManager.notify(pack.getId(), builder.build());
    }

    /**
     * 安装完成之后打开应用
     *
     * @param event
     * @return
     */
    private boolean openAfterInstalled(AccessibilityEvent event) {
        AccessibilityNodeInfo eventInfo;
        if (event != null && event.getSource() != null) {
            eventInfo = event.getSource();
        } else {
            eventInfo = getRootInActiveWindow();
        }
        boolean success = false;
        if (eventInfo != null) {
            success = performEventAction(eventInfo, getString(R.string.btn_accessibility_run), false)
                    || performEventAction(eventInfo, getString(R.string.btn_accessibility_open), false);
            eventInfo.recycle();
        }
        return success;
    }

    /**
     * 安装完成之后关闭安装窗口
     *
     * @param event
     * @return
     */
    private boolean closeAfterInstalled(AccessibilityEvent event) {
        performGlobalAction(GLOBAL_ACTION_BACK);
        return true;
        /*AccessibilityNodeInfo eventInfo;
        if (event != null && event.getSource() != null) {
            eventInfo = event.getSource();
        } else {
            eventInfo = getRootInActiveWindow();
        }
        boolean success = false;
        if (eventInfo != null) {
            success =
                    performEventAction(eventInfo, getString(R.string.btn_accessibility_ok), false)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_done), false)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_complete), false)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_know), false)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_know), false)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_run), true)
                            || performEventAction(eventInfo, getString(R.string.btn_accessibility_open), true);
            eventInfo.recycle();
        }
        return success;*/
    }

    /**
     * 准备卸载
     *
     * @param event
     */
    private void onApplicationUninstall(AccessibilityEvent event) {
        Timber.d("准备卸载");

        // 设置卸载超时（即最后未捕获到卸载失败的话）
        handler.postDelayed(handleUninstallTimeout, UNINSTALL_TIMEOUT);

        if (isValidPackageEvent(event, sUninstallList)) {
            AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_uninstall));
            if (nodeInfo != null) {
                performClick(nodeInfo);
                return;
            }
            nodeInfo = getAccessibilityNodeInfoByText(event, getString(R.string.btn_accessibility_ok));
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

        // 取消卸载超时的操作
        handler.removeCallbacks(handleUninstallTimeout);

        AccessibilityNodeInfo validInfo = getValidAccessibilityNodeInfo(event, sUninstallList);
        String label = null;
        if (validInfo != null && processApplicationUninstalled(event)
                && sUninstallList != null
                && validInfo.getText() != null) {
            label = validInfo.getText().toString();
            removePackFromListByAppName(sUninstallList, label);
            validInfo.recycle();
        }

        // 如果卸载完成是为了安装某个Apk，则执行安装
        processPrepareInstall(label);
    }

    /**
     * 处理卸载应用
     *
     * @param event
     * @return
     */
    private boolean processApplicationUninstalled(AccessibilityEvent event) {
        performGlobalAction(GLOBAL_ACTION_BACK);
        return true;
        /*AccessibilityNodeInfo eventInfo = null;
        if (event != null && event.getSource() != null) {
            eventInfo = event.getSource();
        } else {
            eventInfo = getRootInActiveWindow();
        }
        boolean success = false;
        if (eventInfo != null) {
            success = performEventAction(eventInfo, getString(R.string.btn_accessibility_ok), false)
                    || performEventAction(eventInfo, getString(R.string.btn_accessibility_know), false);
            eventInfo.recycle();
        }
        return success;*/
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
        } else {
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
    private boolean isValidPackageEvent(AccessibilityEvent event, SparseArray<Pack> validPackageList) {
        return getValidAccessibilityNodeInfo(event, validPackageList) != null;
    }

    /**
     * 获取有效的的AccessibilityNodeInfo
     *
     * @param event
     * @param validPackageList 包列表
     * @return
     */
    private AccessibilityNodeInfo getValidAccessibilityNodeInfo(AccessibilityEvent event, SparseArray<Pack> validPackageList) {
        if (validPackageList != null && validPackageList.size() > 0) {
            for (int i = 0; i < validPackageList.size(); i++) {
                int key = validPackageList.keyAt(i);
                Pack pack = validPackageList.get(key);
                AccessibilityNodeInfo nodeInfo = getAccessibilityNodeInfoByText(event, pack.getAppName());
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
     * @param text
     * @return
     */
    private AccessibilityNodeInfo getAccessibilityNodeInfoByText(AccessibilityEvent event, String text) {
        List<AccessibilityNodeInfo> nodes = null;
        // 加入try-catch防崩溃，因为安装时的延迟调用可能会导致 event.getSource 产生奇妙的 NullPointerException
        try {
            if (event != null && event.getSource() != null) {
                nodes = event.getSource().findAccessibilityNodeInfosByText(text);
            }
        } catch (Exception e) {

        }

        // 不用else，而是主动判断，提高识别的成功率
        if (nodes == null || nodes.size() == 0){
            AccessibilityNodeInfo info = getRootInActiveWindow();
            if (info != null) {
                nodes = info.findAccessibilityNodeInfosByText(text);
            }
        }
        if (nodes != null && nodes.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                String nodeText = nodeInfo.getText() == null ? BuildConfig.VERSION_NAME : nodeInfo.getText().toString();
                //nodeInfo.getClassName().equals(className) &&
                if (nodeText.equalsIgnoreCase(text)) {
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
     * @param text
     * @param isGlobalAction 是否是全局动作
     * @return 是否执行成功
     */
    private boolean performEventAction(AccessibilityNodeInfo info, String text, boolean isGlobalAction) {
        if (info == null) {
            return false;
        }
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByText(text);
        if (nodes != null && nodes.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                String nodeText = nodeInfo.getText() == null ? null : nodeInfo.getText().toString();
                if (text.equalsIgnoreCase(nodeText)) {
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
}