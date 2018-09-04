package com.zd112.demo.home.data

import com.google.gson.annotations.SerializedName
import com.zd112.framework.data.BaseData

import java.io.Serializable

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/4.
 * @Emal:18311271399@163.com
 */
class HomeData : BaseData() {
    @SerializedName("body")
    var res: Res? = null

    inner class Res : Serializable {

        @SerializedName("picList")
        var picLists: List<PicList>? = null
        @SerializedName("ggList")
        var ggLists: List<GgList>? = null
        @SerializedName("itemList")
        var itemList: Array<String>? = null
        @SerializedName("profit")
        var profit: Float = 0.toFloat()
        @SerializedName("totalDealMoney")
        var totalDealMoney: Double = 0.toDouble()
        @SerializedName("totalUser")
        var totalUser: Int = 0
        @SerializedName("customerServiceTelnum")
        var customerServiceTelnum: String? = null
        @SerializedName("iteList")
        var iteLists: List<IteList>? = null

        inner class PicList : Serializable {
            @SerializedName("ppctPicPkMdl")
            var ppctPicPkMdl: String? = null
            @SerializedName("createTime")
            var createTime: Long = 0
            @SerializedName("manageId")
            var manageId: Int = 0
            @SerializedName("picGoUrl")
            var picGoUrl: String? = null
            @SerializedName("picId")
            var picId: Int = 0
            @SerializedName("picPixel")
            var picPixel: String? = null
            @SerializedName("picSize")
            var picSize: Int = 0
            @SerializedName("picTitle")
            var picTitle: String? = null
            @SerializedName("picUrl")
            var picUrl: String? = null
        }

        inner class GgList : Serializable {
            @SerializedName("pptArticlePkMdl")
            var pptArticlePkMdl: String? = null
            @SerializedName("artBrowseCount")
            var artBrowseCount: String? = null
            @SerializedName("artContent")
            var artContent: String? = null
            @SerializedName("artCreatePerson")
            var artCreatePerson: String? = null
            @SerializedName("artCreateTime")
            var artCreateTime: String? = null
            @SerializedName("artId")
            var artId: Int = 0
            @SerializedName("artLevelTwo")
            var artLevelTwo: String? = null
            @SerializedName("artLevelTwoName")
            var artLevelTwoName: String? = null
            @SerializedName("artLevelTwoFdName")
            var artLevelTwoFdName: String? = null
            @SerializedName("artLock")
            var artLock: String? = null
            @SerializedName("artModifyPerson")
            var artModifyPerson: String? = null
            @SerializedName("artModifyTime")
            var artModifyTime: String? = null
            @SerializedName("artSort")
            var artSort: String? = null
            @SerializedName("artSource")
            var artSource: String? = null
            @SerializedName("artTitle")
            var artTitle: String? = null
            @SerializedName("coluId")
            var coluId: Int = 0
            @SerializedName("coluIdName")
            var coluIdName: String? = null
            @SerializedName("artUrl")
            var artUrl: String? = null
            @SerializedName("artImgUrl")
            var artImgUrl: String? = null
        }

        inner class IteList : Serializable {
            @SerializedName("iteId")
            var iteId: Int = 0
            @SerializedName("iteType")
            var iteType: Int = 0
            @SerializedName("iteTypeName")
            var iteTypeName: String? = null
            @SerializedName("title")
            var title: String? = null
            @SerializedName("yearRate")
            var yearRate: Double = 0.toDouble()
            @SerializedName("repayDate")
            var repayDate: Int = 0
            @SerializedName("repayInterval")
            var repayInterval: String? = null
            @SerializedName("repayIntervalName")
            var repayIntervalName: String? = null
            @SerializedName("repayType")
            var repayType: Int = 0
            @SerializedName("repayTypeName")
            var repayTypeName: String? = null
            @SerializedName("remainMoney")
            var remainMoney: Long = 0
            @SerializedName("remainMoneyYuan")
            var remainMoneyYuan: String? = null
            @SerializedName("progress")
            var progress: Float = 0.toFloat()
            @SerializedName("isNewItem")
            var isNewItem: Int = 0
            @SerializedName("state")
            var state: String? = null
            @SerializedName("stateName")
            var stateName: String? = null
            @SerializedName("amount")
            var amount: Long = 0
            @SerializedName("amountYuan")
            var amountYuan: String? = null
            @SerializedName("safeguardType")
            var safeguardType: Int = 0
            @SerializedName("safeguardTypeName")
            var safeguardTypeName: String? = null
            @SerializedName("borrowLevelName")
            var borrowLevelName: String? = null
            @SerializedName("safeguardList")
            var safeguardList: List<SafeguardList>? = null

            inner class SafeguardList : Serializable {
                @SerializedName("iteId")
                var iteId: Int = 0
                @SerializedName("type")
                var type: Int = 0
                @SerializedName("typeName")
                var typeName: String? = null
            }
        }
    }
}
