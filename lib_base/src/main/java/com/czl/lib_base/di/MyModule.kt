package com.czl.lib_base.di

import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.data.net.RetrofitClient
import com.czl.lib_base.base.AppViewModelFactory
import com.czl.lib_base.base.DataRepository
import com.czl.lib_base.base.api.ApiService
import com.czl.lib_base.base.source.HttpDataSource
import com.czl.lib_base.base.source.LocalDataSource
import com.czl.lib_base.base.source.impl.HttpDataImpl
import com.czl.lib_base.base.source.impl.LocalDataImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * @author Alwyn
 * @Date 2020/8/5
 * @Description 注入的module
 */
val appModule = module {
    // single->单例式  factory->每次都创建不同实例  viewModel->VM注入
    // androidApplication()->获取当前Application , androidContext() -> 获取context
    // 1 . 获取api实例
    single { RetrofitClient.getInstance().create(ApiService::class.java) }
    // 2. 创建实例前若构造方法内有参数 则需先注入构造中的参数实例
    single<HttpDataSource> { HttpDataImpl(get()) }
    // 3. 获取本地数据调用的实例
    single<LocalDataSource> { LocalDataImpl() }
    // 4 .综合以上本地+网络两个数据来源 得到数据仓库
    single { DataRepository(get(), get()) }
    // bind 将指定的实例绑定到对应的class  single { AppViewModelFactory(androidApplication(), get()) } bind TestActivity::class
    single { AppViewModelFactory(androidApplication() as MyApplication, get()) }

}

val viewModelModule = module {
//    viewModel { TestViewModel(androidApplication(), get()) }
//    viewModel { MainViewModel(androidApplication(), get()) }
}

//val factoryModule = module {
//    // 带参数注入
//    factory { (view: View) -> TestDataImpl(view) }
//}

//val customModule = module {
//    // 绑定与TestActivity生命周期的作用域 通过lifecycleScope.inject<TestScopeDataImpl>()注入
//    scope<TestActivity> {
//        scoped { TestScopeDataImpl() }
//    }
//}

val allModule = appModule + viewModelModule