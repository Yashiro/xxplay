package com.xxplay.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 根据时间生成唯一序列ID
 * 
 * @file:GetSeqIdUtils.java
 * @package_name:com.xxplay.utils
 * @Project:xxplay
 *
 * @Author:陈明
 * @Copyright 陈明   2016 All Rights Reserved.
 * 
 * @data:2016年4月17日
 */
public class GenerateSequenceUtil {
	private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
	
	/** 时间：精确到毫秒 */
	private final static Format dateFormat = new SimpleDateFormat("YYYYMMddHHmmssS");
	
	private final static NumberFormat numberFormat = new DecimalFormat("0000");
	
	private static int seq = 0;
	 
    private static final int MAX = 9999;
	
	public static synchronized String generateSequenceNo() {
		 
        Calendar rightNow = Calendar.getInstance();
       
        StringBuffer sb = new StringBuffer();
 
        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
 
        numberFormat.format(seq, sb, HELPER_POSITION);
 
        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
 
        return sb.toString();
    }
}