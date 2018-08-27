package com.zd112.demo.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.zd112.framework.data.BaseData;

import java.io.Serializable;
import java.util.List;

public class TransferredData extends BaseData {

    @SerializedName("body")
    public Res res;

    public class Res implements Serializable {
//        @SerializedName("totalRecordNum")
//        public int totalRecordNum;

        @SerializedName("list")
        public List<ListItem> lists;

        public class ListItem implements Serializable {
            @SerializedName("iteId")
            public int iteId;
            @SerializedName("iteRepayInterval")
            public String iteRepayInterval;
            @SerializedName("iteRepayIntervalName")
            public String iteRepayIntervalName;//个月
            @SerializedName("iteTitle")
            public String iteTitle;
            @SerializedName("iteYearRate")
            public double iteYearRate;//利率
            @SerializedName("surplusTotalNo")
            public int surplusTotalNo;
            @SerializedName("surplusPrincipal")
            public double surplusPrincipal;
            @SerializedName("borrowLevelName")
            public String borrowLevelName;
            @SerializedName("claTransSumYuan")
            public double claTransSumYuan;//转让价格
            @SerializedName("claId")
            public int claId;
            @SerializedName("userId")
            public String userId;
            @SerializedName("type")
            public String type;
            @SerializedName("claTransClaimSumYuan")
            public double claTransClaimSumYuan;//剩余本息
            public double rate;
        }
    }
}
