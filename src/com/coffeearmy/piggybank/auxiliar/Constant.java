package com.coffeearmy.piggybank.auxiliar;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;



/** Constants used in EventBus and extras*/
public class Constant {
	public static String ACCOUNT_ID="account_id";
	public static String ACCOUNT_NAME="account_name";
	public static String ACCOUNT_MONEY="account_money";
	public static String ACCOUNT_ICON="account_icon";
	
	public static String OPERATION_ID="operation_id";
	public static String OPERATION_SING="operation_sign";
	public static String OPERATION_MONEY="operation_money";
	public static String OPERATION_ICON="operation_icon";
	public static String OPERATION_DATE="operation_date";
	
	//Only when edit an operation previous
	public static String OPERATION_SING_PREVIOUS="operation_sign_previous";
	public static String OPERATION_MONEY_PREVIOUS="operation_money_previous";
	
	//Number formatter
	public static DecimalFormat DF = new DecimalFormat("#");	
	
	//Date formatter
	public static DateFormat dateFormatMMMDD= SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

}
