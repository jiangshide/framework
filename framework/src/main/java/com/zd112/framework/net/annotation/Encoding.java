package com.zd112.framework.net.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({Encoding.ISO_8859_1, Encoding.UTF_8, Encoding.GBK, Encoding.GBK18030})
@Retention(RetentionPolicy.SOURCE)
public @interface Encoding {
    /**
     * 国际标准编码规范
     */
    String ISO_8859_1 = "ISO-8859-1";

    /**
     * 万国码
     */
    String UTF_8 = "UTF-8";

    /**
     * 汉字内码扩展规范
     * 兼容GBK_2312
     */
    String GBK = "GBK";

    /**
     * 信息技术中文编码字符集
     * 兼容GBK
     */
    String GBK18030 = "GBK18030";

}
