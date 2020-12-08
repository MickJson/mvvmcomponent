package com.czl.module_search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.data.bean.SearchDataBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_search.R
import com.czl.module_search.databinding.SearchItemBinding
import com.czl.module_search.ui.fragment.SearchFragment

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
class SearchAdapter(val mFragment:SearchFragment) :BaseQuickAdapter<SearchDataBean.Data,BaseDataBindingHolder<SearchItemBinding>>(
    R.layout.search_item) {
    val tvShare = " 分享 "
    val tvAuthor = " 作者 "
    override fun convert(
        holder: BaseDataBindingHolder<SearchItemBinding>,
        item: SearchDataBean.Data
    ) {
        holder.dataBinding?.apply { 
            data = item
            adapter = this@SearchAdapter
            executePendingBindings()
        }
    }

    val onCollectClick:BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SearchDataBean.Data){
            if (!it.collect) {
                mFragment.viewModel.collectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                mFragment.showSuccessToast("收藏成功")
                                it.collect = true

                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            } else {
                mFragment.viewModel.unCollectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                it.collect = false
                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            }
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<SearchDataBean.Data>() {
        override fun areItemsTheSame(
            oldItem: SearchDataBean.Data,
            newItem: SearchDataBean.Data
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: SearchDataBean.Data,
            newItem: SearchDataBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }
}