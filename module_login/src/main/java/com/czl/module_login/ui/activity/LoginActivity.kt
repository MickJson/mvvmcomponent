package com.czl.module_login.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginActivityLoginBinding
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.config.AppConstants
import com.czl.module_login.viewmodel.LoginViewModel

@Route(path = AppConstants.Router.Login.A_LOGIN)
class LoginActivity : BaseActivity<LoginActivityLoginBinding, LoginViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(): Int {
        return R.layout.login_activity_login
    }

    override fun initParam() {
        super.initParam()

    }

    override fun initData() {
        super.initData()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

}