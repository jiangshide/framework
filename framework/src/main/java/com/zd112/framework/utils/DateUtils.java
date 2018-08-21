package com.zd112.framework.utils;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * 时间日期相关工具类
 */
public class DateUtils extends CountDownTimer {

    private TextView textView;
    private String tips;
    private int beforeOrAfter;
    private String beforeTips, afterTips;
    private String lastTips;
    private String normalText;
    private OnDateListener onDateListener;
    private Class _class;
    public static final String ymdhms = "yyyy-MM-dd HH:mm:ss";
    public static final String ymd = "yyyy-MM-dd";
    private static String WEEK[] = {"日", "一", "二", "三", "四", "五", "六"};
    private static String MONTH[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public DateUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public DateUtils setView(TextView textView) {
        this.textView = textView;
        this.textView.setVisibility(View.VISIBLE);
        return this;
    }

    public DateUtils setView(TextView textView, String tips) {
        this.textView = textView;
        this.tips = tips;
        this.textView.setVisibility(View.VISIBLE);
        return this;
    }

    public DateUtils setView(TextView textView, String tips, int beforeOrAfter) {
        this.textView = textView;
        this.tips = tips;
        this.beforeOrAfter = beforeOrAfter;
        this.textView.setVisibility(View.VISIBLE);
        return this;
    }

    public DateUtils setView(TextView textView, String beforeTips, String afterTips) {
        this.textView = textView;
        this.beforeTips = beforeTips;
        this.afterTips = afterTips;
        this.textView.setVisibility(View.VISIBLE);
        return this;
    }

    public DateUtils setNormalText(String text) {
        this.normalText = text;
        return this;
    }

    public DateUtils goActivity(final Class _class) {
        this._class = _class;
        if (null != textView && null != _class) {
            LogUtils.e("-------------_class:",_class);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                    LogUtils.e("----------cancel");
                    new IntentUtils().setClass(_class).start();
                }
            });
        }
        return this;
    }

    public DateUtils setListener(OnDateListener listener) {
        this.onDateListener = listener;
        return this;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        LogUtils.e("---------------millisUntilFinished:",millisUntilFinished);
        if (null != onDateListener) {
            onDateListener.onTick(millisUntilFinished);
        }
        if (null != textView) {
            long second = millisUntilFinished / 1000;
            if (!TextUtils.isEmpty(beforeTips) && !TextUtils.isEmpty(afterTips)) {
                lastTips = beforeTips + second + afterTips;
            } else if (!TextUtils.isEmpty(tips)) {
                lastTips = beforeOrAfter == 0 ? second + tips : tips + second;
            } else {
                lastTips = second + "s";
            }
            textView.setText(lastTips);
        }
    }

    @Override
    public void onFinish() {
        LogUtils.e("--------------finish");
        if (null != onDateListener) {
            onDateListener.onFinish();
        }
        if (null != textView) {
            textView.setText(!TextUtils.isEmpty(normalText) ? normalText : lastTips);
        }
        if (null != _class) {
            new IntentUtils().setClass(_class).start();
        }
    }

    public interface OnDateListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    /**
     * Date转String
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String date2String(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * String转Date
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * Long转Date
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date long2Date(long currentTime, String formatType) throws ParseException {
        Date date = new Date(currentTime);
        return date;
    }


    /**
     * Date转Long
     *
     * @param date
     * @return
     */
    public static long date2Long(Date date) {
        return date.getTime();
    }


    /**
     * Long转String
     *
     * @param currentTime
     * @return
     * @throws ParseException
     */
    public static String long2String(long currentTime) throws ParseException {
        Date date = long2Date(currentTime, ymdhms);
        String strTime = date2String(date, ymdhms);
        return strTime;
    }


    /**
     * Long转String
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String long2String(long currentTime, String formatType) throws ParseException {
        Date date = long2Date(currentTime, formatType);
        String strTime = date2String(date, formatType);
        return strTime;
    }


    /**
     * String转Long
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType) throws ParseException {
        Date date = string2Date(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date2Long(date); // date类型转成long类型
            return currentTime;
        }
    }


    //时区相关

    /**
     * 判断用户的设备时区是否为东八区（中国）
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08")) {
            defaultVaule = true;
        } else {
            defaultVaule = false;
        }
        return defaultVaule;
    }


    /**
     * 根据不同时区，转换时间
     */
    public static Date transformTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime()) - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    /**
     * 显示日期为x小时前、昨天、前天、x天前等
     *
     * @param longTime
     * @return
     * @throws ParseException
     */
    public static String showTimeAhead(long longTime) throws ParseException {
        String resultTime = "";

        //传入的日期
        Date date = null;
        if (isInEasternEightZones()) {
            date = long2Date(longTime, ymdhms);
        } else {
            date = transformTimeZone(long2Date(longTime, ymdhms), TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }

        //当前日期
        Calendar cal = Calendar.getInstance();
        Date nowDate = new Date();
        int days = (int) (nowDate.getDay() - date.getDay());

        //如果日期相同
        if (days == 0) {
            int hour = nowDate.getHours() - date.getHours();
            //如果小时相同
            if (hour == 0) {
                resultTime = (nowDate.getMinutes() - date.getMinutes()) + "分钟前";
            } else {
                resultTime = hour + "小时前";
            }
        } else if (days == 1) {
            resultTime = "昨天";
        } else if (days == 2) {
            resultTime = "前天 ";
        } else if (days > 2 && days < 31) {
            resultTime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            resultTime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            resultTime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            resultTime = "3个月前";
        } else {
            resultTime = long2String(longTime, ymdhms);
        }
        return resultTime;
    }

    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE) + "";
    }

    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return "星期" + WEEK[dayOfWeek < 1 && dayOfWeek > 7 ? 1 : dayOfWeek];
    }

    public static String getMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        return MONTH[month];
    }
}
