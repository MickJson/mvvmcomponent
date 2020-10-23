package com.czl.lib_base.base.source

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.mvvm.entity.*
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface HttpDataSource {
    fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>>
    fun getMainArticle(page: String = "0"): Observable<BaseBean<ArticleBean>>
    fun getCollectArticle(page: String = "0"): Observable<BaseBean<CollectArticle>>

}