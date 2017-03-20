package com.jeeba.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yuyang Zhang on 2017/1/3.
 */
public class DateUtil {
    /**
     * 按照传入的匿名SimpleDateFormat对象格式化时间成字符串返回
     *
     * @param date
     * @param simpleDateFormat
     */
    public static String FormatDateToString(Date date, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(date);
    }
}
