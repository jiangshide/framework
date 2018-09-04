package com.zd112.demo.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView

import com.zd112.demo.R
import com.zd112.demo.home.data.HomeData
import com.zd112.demo.home.fragment.CommentFragment
import com.zd112.demo.home.fragment.ShareFragment
import com.zd112.framework.BaseFragment
import com.zd112.framework.anim.AnimUtils
import com.zd112.framework.annotation.Transformer
import com.zd112.framework.apdater.CusFragmentPagerAdapter
import com.zd112.framework.net.helper.NetInfo
import com.zd112.framework.utils.LogUtils
import com.zd112.framework.utils.ViewUtils
import com.zd112.framework.view.CusViewPager

import java.util.ArrayList

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
class HomeFragment : BaseFragment() {

    @ViewUtils.ViewInject(R.id.testClick)
    private val testClick: Button? = null
    @ViewUtils.ViewInject(R.id.navigationTitle)
    private val navigationTitle: TabLayout? = null
    @ViewUtils.ViewInject(R.id.tabViewPager)
    private val tabViewPager: CusViewPager? = null
    @ViewUtils.ViewInject(R.id.textTxt)
    private val textTxt: TextView? = null

    private var isTrue: Boolean = false

    override fun initView(savedInstanceState: Bundle) {
        setView(R.layout.home, this, true)
        request("more/ind", HomeData::class.java, true)
    }

    override fun onSuccess(info: NetInfo) {
        super.onSuccess(info)
        val homeData = info.getResponseObj<HomeData>()
        val bannerList = ArrayList<String>()
        for (picList in homeData.res.picLists) {
            bannerList.add(picList.picUrl)
        }

    }

    override fun processLogic(savedInstanceState: Bundle) {
        testClick!!.setOnClickListener {
            if (isTrue) {
                tabViewPager!!.setMode(isTrue, Transformer.VERTICAL)
                isTrue = false
            } else {
                tabViewPager!!.setMode(isTrue, Transformer.ACCORDION)
                isTrue = true
            }
            AnimUtils.anim2(textTxt)
        }
        tabViewPager!!.adapter = CusFragmentPagerAdapter(childFragmentManager, CHANNELS, CommentFragment(), ShareFragment(), CommentFragment())
        navigationTitle!!.setupWithViewPager(tabViewPager)
        navigationTitle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //                CusToast.fixTxt(getActivity(), "tab:" + tab);
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {

        private val CHANNELS = arrayOf("CUPCAKE", "DONUT", "Test")
    }
}
