package ir.zelzele.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ir.zelzele.Naji;
import ir.zelzele.storage.PreferenceManager;

/**
 * Created by Payam on 12/28/2017.
 */

public class Tools {
    public static boolean internetConnect() {
        ConnectivityManager cv = (ConnectivityManager) Naji.getInstance().
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean checkNet = false;

        NetworkInfo netInfo = cv.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {

            checkNet = true;
        }
        return checkNet;


    }

    public static Date convertToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            String engDate = converNumberPersianToEnglish(date);
            d = sdf.parse(engDate);
        } catch (ParseException ex) {
            Log.d("exMsg", ex.toString());
        }
        return d;
    }


    public static boolean internetConnectWifi() {
        ConnectivityManager cv = (ConnectivityManager)
                Naji.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean checkNet = false;

        NetworkInfo netInfo = cv.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                checkNet = true;
        }
        return checkNet;
    }

    public static boolean internetConnectMobileData() {
        ConnectivityManager cv = (ConnectivityManager) Naji.getInstance().getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean checkNet = false;

        NetworkInfo netInfo = cv.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                checkNet = true;
        }
        return checkNet;
    }


    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String convertEnglishNumbersToPersian(String input) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                builder.append(arabicChars[(int) (input.charAt(i)) - 48]);
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String converNumberPersianToEnglish(String input) {
        final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
        char[] chars = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static boolean requestcheck(Context context) {
        if (PreferenceManager.getInstance(context).lastRequest() > 0) {
            long lastRequestTime = PreferenceManager.getInstance(context).lastRequest();
            long curTime = System.currentTimeMillis();
            if (curTime - lastRequestTime > 1800000) {
                return true;
            } else
                return false;
        }

        return true;
    }

    private class SolarCalendar {

        public String strWeekDay = "";
        public String strMonth = "";

        int date;
        int month;
        int year;

        public SolarCalendar() {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }

        public SolarCalendar(Date MiladiDate) {
            calcSolarCalendar(MiladiDate);
        }

        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = "فروردين";
                    break;
                case 2:
                    strMonth = "ارديبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تير";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهريور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دي";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = "يکشنبه";
                    break;
                case 1:
                    strWeekDay = "دوشنبه";
                    break;
                case 2:
                    strWeekDay = "سه شنبه";
                    break;
                case 3:
                    strWeekDay = "چهارشنبه";
                    break;
                case 4:
                    strWeekDay = "پنج شنبه";
                    break;
                case 5:
                    strWeekDay = "جمعه";
                    break;
                case 6:
                    strWeekDay = "شنبه";
                    break;
            }

        }

    }

    public static String getCurrentShamsidate() {
        Locale loc = new Locale("en_US");
        Tools util = new Tools();
        SolarCalendar sc = util.new SolarCalendar();
        return convertEnglishNumbersToPersian(String.valueOf(sc.strWeekDay + " " + String.valueOf(sc.year) + "/" + String.format(loc, "%02d",
                sc.month) + "/" + String.format(loc, "%02d", sc.date)));
    }

    public static String getShamsidate(Date MiladiDate) {
        Locale loc = new Locale("en_US");
        Tools util = new Tools();
        SolarCalendar sc = util.new SolarCalendar(MiladiDate);
        return convertEnglishNumbersToPersian(String.valueOf(sc.strWeekDay + " " + String.valueOf(sc.year) + "/" + String.format(loc, "%02d",
                sc.month) + "/" + String.format(loc, "%02d", sc.date)));
    }
}