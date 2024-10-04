package com.pasc.business.ewallet.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.pasc.business.ewallet.NotProguard;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
@NotProguard
public class Util {
    public static boolean needCheckPhone=true;
    public static String getLastStr(String str, int lastNum) {
        int len = 0;
        if (!Util.isEmpty (str) && (len = str.length ()) > lastNum) {
            return str.substring (len - lastNum, len);
        } else {
            return str;
        }

    }

    public static boolean ignoreAnim(){
        if ("SM-A9200".equals (Build.MODEL)){
            return true;
        }
        return false;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo ();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected ();
    }

    public static boolean isValidPhone(String phoneNum) {
        boolean matchPhone = false;
        if (!isEmpty (phoneNum)) {
            phoneNum = phoneNum.replace (" ", "");
            if (phoneNum.length () == 11) {
                if (needCheckPhone){
                 String regexPhoneNum = "^((1[3-9]))\\d{9}$";
                 matchPhone = Pattern.matches (regexPhoneNum, phoneNum);
                }else {
                    matchPhone=true;
                }
            }
        }

        return matchPhone;
    }

    public static boolean isValidBankCard(String bankCard) {
        boolean matchBankCard = false;
        if (!isEmpty (bankCard)) {
            bankCard = bankCard.replace (" ", "");
            if (bankCard.length () >= 16) {
                String regexCardNum = "^62[0-5]\\d{13,16}$";
                matchBankCard = Pattern.matches (regexCardNum, bankCard);
            }
        }
        return matchBankCard;
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isValidIdCard(String idCard) {
//        String regex15 ="^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
//        String regex18 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";
        String regex = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
//        return Pattern.matches(regex18, idCard) || Pattern.matches(regex15, idCard);
        return Pattern.matches (regex, idCard);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.replace (" ", "").length () == 0;
    }

    public static String getBanKCard4Last(String text) {
        if (isEmpty (text)) {
            return "";
        } else {
            return text.replace ("*", "").replace (" ", "");
        }
    }

    public static String stringPoint(float num, int point) {
        String data = String.format ("%." + point + "f", num);
        return data;
    }

    public static String stringPoint(double num, int point) {
        String data = String.format ("%." + point + "f", num);
        return data;
    }

    public static String doublePoint(long fen, int point) {
        double aa = fen / 100.0;
        return stringPoint (aa,point);
    }

    public static Calendar getDate(String str) {
        Calendar calendar = Calendar.getInstance ();
        Date date;
        try {
//            "tradeTime": "2019-08-07 14:17:45",
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse (str);

        } catch (Exception e) {
            e.getMessage ();
            date = new Date ();

        }
        calendar.setTime (date);
        return calendar;
    }

    public static Calendar getDate(long time) {
        Calendar calendar = Calendar.getInstance ();
        Date date;
        try {
//            "tradeTime": "2019-08-07 14:17:45",
            date=new Date ();
            date.setTime (time);
        } catch (Exception e) {
            e.getMessage ();
            date = new Date ();
        }
        calendar.setTime (date);

        return calendar;
    }

    public static String doubleStr(int num) {
        return num >= 10 ? num + "" : "0" + num;
    }

    public static String getYear_Month(String str) {
        Calendar date = Util.getDate (str);
        int year = date.get (Calendar.YEAR);
        int month = date.get (Calendar.MONTH) + 1;
        return doubleStr (year) + "-" + doubleStr (month);
    }

    public static String getMonth_Day_Hour_Min(String str) {
        Calendar date = Util.getDate (str);
        int month = date.get (Calendar.MONTH) + 1;
        int day = date.get (Calendar.DAY_OF_MONTH);
        int hour = date.get (Calendar.HOUR_OF_DAY);
        int min = date.get (Calendar.MINUTE);

        return doubleStr (month) + "-" + doubleStr (day) + " " + doubleStr (hour) + ":" + doubleStr (min);
    }

    public static String getMonth_Day_Hour_Min(long time) {
        Calendar date = Util.getDate (time);
        int month = date.get (Calendar.MONTH) + 1;
        int day = date.get (Calendar.DAY_OF_MONTH);
        int hour = date.get (Calendar.HOUR_OF_DAY);
        int min = date.get (Calendar.MINUTE);

        return doubleStr (month) + "-" + doubleStr (day) + " " + doubleStr (hour) + ":" + doubleStr (min);
    }


    /**
     * 输入格式化参数，比如 4，4，4，4 类型格式化银行卡，输出为1234 5678 1234 5678.
     *
     * @param content
     * @param ints
     * @return
     */
    public static String addTextSpace(String content, int... ints) {
        if (content == null || content.length () == 0) {
            return "";
        }
        //去空格
        content = content.replaceAll ("\\s", "");
        if (content == null || content.length () == 0) {
            return "";
        }

        if (ints.length == 0) {
            return content;
        }

        int count = ints.length - 1;
        for (int i : ints) {
            count += i;
        }

        if (content.length () > count) {
            content = content.substring (0, count);
        }
        StringBuilder newString = new StringBuilder ();

        int[] formatInts = formatIntegerSpace (ints);
        boolean hit;
        for (int i = 1; i <= content.length (); i++) {
            //格式化
            //丢弃最后一个
            hit = false;
            for (int j = 0; j < formatInts.length - 1; j++) {
                if (i == formatInts[j] && i != content.length ()) {
                    newString.append (content.charAt (i - 1)).append (" ");
                    hit = true;
                }
            }
            if (!hit) {
                newString.append (content.charAt (i - 1));
            }
        }
        return newString.toString ();
    }

    /**
     * 格式化 比如 4，4，4，4 格式化之后为 4，8，12，16
     *
     * @param ints
     * @return
     */
    public static int[] formatIntegerSpace(int... ints) {
        int[] result = new int[ints.length];
        int count = 0;
        for (int i = 0; i < ints.length; i++) {
            count += ints[i];
            result[i] = count;
        }
        return result;
    }

    /**
     * 脱敏
     *
     * @param name
     * @return
     */
    public static String formatName(String name) {

        if (!isEmpty (name)) {
            if (name.contains ("*")){
                return name;
            }
            else if (name.length () > 2) {
                name = name.substring (0, 1) + "*" + name.substring (name.length () - 1, name.length ());
            } else if (name.length () > 1) {
                //就是2
//                name = name.substring (0, 1) + "*";
                name = "*"+name.substring (1,2);

            }
        }
        return name;
    }

    /**
     * 脱敏
     *
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNum(String phoneNum) {
        if (!isEmpty (phoneNum)) {

            if (phoneNum.contains ("*")) {
                return phoneNum;
            }else if (phoneNum.length () != 11) {
                return phoneNum;
            }

            phoneNum = phoneNum.substring (0, 3) + "****" + phoneNum.substring (phoneNum.length () - 4, phoneNum.length ());
        }
        return phoneNum;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res = s;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("MM-dd HH:mm");
            long lt = new Long (s);
            Date date = new Date (lt);
            res = simpleDateFormat.format (date);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return res;
    }

    public static String stampToAllDate(String s) {
        String res = s;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
            long lt = new Long (s);
            Date date = new Date (lt);
            res = simpleDateFormat.format (date);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return res;
    }

    public static String stampToYearAndMonth(String s) {
        String res = s;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM");
            long lt = new Long (s);
            Date date = new Date (lt);
            res = simpleDateFormat.format (date);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return res;
    }

    private static final String defaultMac = "000000000000";

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    public static String getMacFromHardware() {
        String mac = defaultMac;
        try {
            List<NetworkInterface> all = Collections.list (NetworkInterface.getNetworkInterfaces ());
            for (NetworkInterface nif : all) {
                if (!nif.getName ().equalsIgnoreCase ("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress ();
                if (macBytes == null) {
                    break;
                }

                StringBuilder res1 = new StringBuilder ();
                for (byte b : macBytes) {
                    res1.append (String.format ("%02X:", b));
                }

                if (res1.length () > 0) {
                    res1.deleteCharAt (res1.length () - 1);
                }
//                return res1.toString ().replace (":", "");
                mac = res1.toString ();
                break;

            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return mac.replace (":", "");
    }

    public static String getIPAddress(Context context) {
        String ip = "";
        ConnectivityManager conMann = (ConnectivityManager)
                context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo (ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo (ConnectivityManager.TYPE_WIFI);

        if (mobileNetworkInfo.isConnected ()) {
            ip = getLocalIpV4Address ();
        } else if (wifiNetworkInfo.isConnected ()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo ();
            int ipAddress = wifiInfo.getIpAddress ();
            ip = intToIp (ipAddress);
        }

        if (isEmpty (ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    private static String getLocalIpV4Address() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list (NetworkInterface.getNetworkInterfaces ());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list (ni.getInetAddresses ());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress () && !address.isLinkLocalAddress ()) {
                        ipv4 = address.getHostAddress ();
                        return ipv4;
                    }
                }

            }

        } catch (SocketException ex) {
            ex.printStackTrace ();
        }
        return null;
    }

    //获取Wifi ip 地址
    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /*****网络类型
     * 枚举：1,2 ,3
     1: 3g/4G网络
     2: WIFI网络
     3: Edge网络(2G网络)
     4:Unknown网络
     * ****/
    public static String GetNetworkType(Context context) {
        String strNetworkType = "Unknown";
        ConnectivityManager activeNetworkInfo = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = activeNetworkInfo.getActiveNetworkInfo ();
        if (networkInfo != null && networkInfo.isConnected ()) {
            if (networkInfo.getType () == ConnectivityManager.TYPE_WIFI) {
//                strNetworkType = "WIFI";
                strNetworkType = "2";
            } else if (networkInfo.getType () == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName ();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype ();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
//                        strNetworkType = "2G";
                        strNetworkType = "3";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
//                        strNetworkType = "3G";
                        strNetworkType = "1";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
//                        strNetworkType = "4G";
                        strNetworkType = "1";

                        break;
                    default:
                        if (TextUtils.isEmpty (_strSubTypeName)) {
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if ("TD-SCDMA".equalsIgnoreCase (_strSubTypeName) || "WCDMA".equalsIgnoreCase (_strSubTypeName) || "CDMA2000".equalsIgnoreCase (_strSubTypeName)) {
//                            strNetworkType = "3G";
                                strNetworkType = "1";
                            } else {
//                                strNetworkType = _strSubTypeName;
                            }
                        }
                        break;
                }
            }
        }

        return strNetworkType;
    }

}
