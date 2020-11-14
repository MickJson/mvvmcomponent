package com.czl.lib_base.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.czl.lib_base.BuildConfig
import com.czl.lib_base.R
import com.czl.lib_base.crash.CaocConfig
import com.czl.lib_base.di.allModule
import com.czl.lib_base.util.ToastHelper
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import es.dmoral.toasty.Toasty
import io.reactivex.plugins.RxJavaPlugins
import me.jessyan.autosize.AutoSizeConfig
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.litepal.LitePal


/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        setApplication(this)
        LitePal.initialize(this)
        MMKV.initialize(this)
        // 初始化Fragmentation
        Fragmentation.builder()
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(BuildConfig.DEBUG)
            .install()
        // 屏幕适配
        AutoSizeConfig.getInstance().setCustomFragment(true).setBaseOnWidth(false)
            .setExcludeFontScale(true).designHeightInDp = 720
        //是否开启日志打印
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG).setConsoleSwitch(BuildConfig.DEBUG)
        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
            .enabled(true) //是否启动全局异常捕获
            .showErrorDetails(BuildConfig.DEBUG) //是否显示错误详细信息
            .showRestartButton(true) //是否显示重启按钮
            .trackActivities(true) //是否跟踪Activity
            .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
//            .errorDrawable(R.mipmap.ic_launcher) //错误图标
//            .restartActivity(LoginActivity::class.java) //重新启动后的activity
            //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
            //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
            .apply()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(allModule)
        }
        RxJavaPlugins.setErrorHandler {
            ToastHelper.showErrorToast("系统错误")
            it.printStackTrace()
        }
        Toasty.Config.getInstance().allowQueue(false).apply()
        // 根据活动时间动态更换资源图标（如淘宝双11）
//        LauncherIconManager.register(this)
    }


    companion object {
        init {
            ClassicsFooter.REFRESH_FOOTER_FINISH = ""
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.md_theme_red, R.color.white)
                MaterialHeader(context).setColorSchemeColors(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_red
                    )
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context).setFinishDuration(0)
            }
        }
    }

    private fun setApplication(application: Application) {
        //初始化工具类
        Utils.init(application)
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                AppManager.instance.addActivity(activity)
                if ("leakcanary.internal.activity.LeakActivity" == activity.javaClass.name) {
                    return
                }
                ImmersionBar.with(activity).statusBarDarkFont(true).init()
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle?
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.instance.removeActivity(activity)
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

}