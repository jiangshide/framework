package com.zd112.demo.home.data;

import com.google.gson.annotations.SerializedName;
import com.zd112.framework.data.BaseData;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/4.
 * @Emal:18311271399@163.com
 */
public class HomeData extends BaseData{

    @SerializedName("body")
    public Res res;

    public class Res implements Serializable {

        @SerializedName("picList")
        public List<PicList> picLists;
        @SerializedName("ggList")
        public List<GgList> ggLists;
        @SerializedName("itemList")
        public String[] itemList;
        @SerializedName("profit")
        public float profit;
        @SerializedName("totalDealMoney")
        public double totalDealMoney;
        @SerializedName("totalUser")
        public int totalUser;
        @SerializedName("customerServiceTelnum")
        public String customerServiceTelnum;
        @SerializedName("iteList")
        public List<IteList> iteLists;

        public class PicList implements Serializable {
            @SerializedName("ppctPicPkMdl")
            public String ppctPicPkMdl;
            @SerializedName("createTime")
            public long createTime;
            @SerializedName("manageId")
            public int manageId;
            @SerializedName("picGoUrl")
            public String picGoUrl;
            @SerializedName("picId")
            public int picId;
            @SerializedName("picPixel")
            public String picPixel;
            @SerializedName("picSize")
            public int picSize;
            @SerializedName("picTitle")
            public String picTitle;
            @SerializedName("picUrl")
            public String picUrl;
        }

        public class GgList implements Serializable {
            @SerializedName("pptArticlePkMdl")
            public String pptArticlePkMdl;
            @SerializedName("artBrowseCount")
            public String artBrowseCount;
            @SerializedName("artContent")
            public String artContent;
            @SerializedName("artCreatePerson")
            public String artCreatePerson;
            @SerializedName("artCreateTime")
            public String artCreateTime;
            @SerializedName("artId")
            public int artId;
            @SerializedName("artLevelTwo")
            public String artLevelTwo;
            @SerializedName("artLevelTwoName")
            public String artLevelTwoName;
            @SerializedName("artLevelTwoFdName")
            public String artLevelTwoFdName;
            @SerializedName("artLock")
            public String artLock;
            @SerializedName("artModifyPerson")
            public String artModifyPerson;
            @SerializedName("artModifyTime")
            public String artModifyTime;
            @SerializedName("artSort")
            public String artSort;
            @SerializedName("artSource")
            public String artSource;
            @SerializedName("artTitle")
            public String artTitle;
            @SerializedName("coluId")
            public int coluId;
            @SerializedName("coluIdName")
            public String coluIdName;
            @SerializedName("artUrl")
            public String artUrl;
            @SerializedName("artImgUrl")
            public String artImgUrl;
        }

        public class IteList implements Serializable {
            @SerializedName("iteId")
            public int iteId;
            @SerializedName("iteType")
            public int iteType;
            @SerializedName("iteTypeName")
            public String iteTypeName;
            @SerializedName("title")
            public String title;
            @SerializedName("yearRate")
            public double yearRate;
            @SerializedName("repayDate")
            public int repayDate;
            @SerializedName("repayInterval")
            public String repayInterval;
            @SerializedName("repayIntervalName")
            public String repayIntervalName;
            @SerializedName("repayType")
            public int repayType;
            @SerializedName("repayTypeName")
            public String repayTypeName;
            @SerializedName("remainMoney")
            public long remainMoney;
            @SerializedName("remainMoneyYuan")
            public String remainMoneyYuan;
            @SerializedName("progress")
            public float progress;
            @SerializedName("isNewItem")
            public int isNewItem;
            @SerializedName("state")
            public String state;
            @SerializedName("stateName")
            public String stateName;
            @SerializedName("amount")
            public long amount;
            @SerializedName("amountYuan")
            public String amountYuan;
            @SerializedName("safeguardType")
            public int safeguardType;
            @SerializedName("safeguardTypeName")
            public String safeguardTypeName;
            @SerializedName("borrowLevelName")
            public String borrowLevelName;
            @SerializedName("safeguardList")
            public List<SafeguardList> safeguardList;

            public class SafeguardList implements Serializable {
                @SerializedName("iteId")
                public int iteId;
                @SerializedName("type")
                public int type;
                @SerializedName("typeName")
                public String typeName;
            }
        }
    }


    public String name;
    public String url;
    public String thumb;
}
