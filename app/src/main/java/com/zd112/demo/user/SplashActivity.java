package com.zd112.demo.user

import android.os.Bundle

import com.zd112.demo.MainActivity
import com.zd112.demo.R
import com.zd112.demo.data.TransferredData
import com.zd112.framework.BaseActivity
import com.zd112.framework.net.callback.Callback
import com.zd112.framework.net.callback.ProgressCallback
import com.zd112.framework.net.helper.NetInfo
import com.zd112.framework.utils.IntentUtils
import com.zd112.framework.utils.LogUtils
import com.zd112.framework.view.UpdateView

import java.io.IOException

class SplashActivity : BaseActivity(), UpdateView.OnUpdateListener {
    override fun initView(savedInstanceState: Bundle) {
        setView(R.layout.activity_splash, this)
    }

    override fun processLogic(savedInstanceState: Bundle) {
        UpdateView(this).init().setListener(this)
    }

    override fun onResult(code: Int) {
        IntentUtils().setClass(MainActivity::class.java).start()
    }
}
