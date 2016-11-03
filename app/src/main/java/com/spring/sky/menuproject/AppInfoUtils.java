package com.spring.sky.menuproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class AppInfoUtils {

	public static final Object DEVICE_NAME = android.os.Build.MANUFACTURER;
	public static final Object DEVICE_VERSION = android.os.Build.VERSION.SDK_INT;
	private static final String TAG = "AppInfoUtils";
	public static String IMEI = "";
	public static String IMSI = "";
	public static String PACKAGE_NAME = "";
	public static int VERSION_CODE;
	public static String VERSION_NAME;
	public static float GCREEN_DENSITY; //缩放比例
	/** 得到分辨率高度 */
	public static int heightPs = -1;
	/** 得到分辨率宽度 */
	public static int widthPs = -1;
	/** 得到屏幕密度 */
	public static int densityDpi = -1;
	public static float scaledDensity = -1;
	/** 得到X轴密度 */
	public static float Xdpi = -1;
	/** 得到Y轴密度 */
	public static float Ydpi = -1;

	private static Toast mToast ;
	private static boolean hasPermission;
	@TargetApi(Build.VERSION_CODES.M)
	public static void init(Activity context){
		PACKAGE_NAME = context.getPackageName();
		getVersion(context);
		getScreen(context);
		mToast = Toast.makeText(context,"", Toast.LENGTH_LONG);
	}

	public static void readPhoneStatus(Activity context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI =telephonyManager.getDeviceId();
		IMSI =telephonyManager.getSubscriberId();
		Log.d(TAG,"IMEI="+IMEI);
		Log.d(TAG,"IMSI="+IMSI);
	}



	public static void toast(String text){
		if(mToast != null){
			mToast.setText(text);
			mToast.show();
		}
	}
	
	/**
	 * 得到手机的屏幕基本信息
	 * 
	 * @param context
	 */
	private static void getScreen(Activity context) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		heightPs = metrics.heightPixels;
		widthPs = metrics.widthPixels;
		densityDpi = metrics.densityDpi;
		scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		GCREEN_DENSITY = context.getResources().getDisplayMetrics().density;
		Xdpi = metrics.xdpi;
		Ydpi = metrics.ydpi;
		Log.i("手机分辨率", "分辨率：" + widthPs + "X" + heightPs + "    屏幕密度：" + densityDpi + "    宽高密度：" + Xdpi + "X" + Ydpi);
	}

	/***
	 * 获取客户端版本
	 * 
	 * @param context
	 * @return
	 */
	private static void getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			VERSION_NAME = info.versionName;
			VERSION_CODE = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 把密度dip单位转化为像数px单位
	 *
	 * @param dip
	 * @return
	 */
	public static int dipToPx(int dip) {
		return (int) (dip * GCREEN_DENSITY + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public static int spToPx(float spValue) {
		return (int) (spValue * scaledDensity + 0.5f);
	}

	/***
	 * 把像数px转化为密度dip单位
	 *
	 * @param px
	 * @return
	 */
	public static int pxToDip(int px) {
		return (int) (px * GCREEN_DENSITY + 0.5f * (px >= 0 ? 1 : -1));
	}
	
	public static boolean hasPermission(Context context, String permission){
		PackageManager pm = context.getPackageManager();
        boolean has = (PackageManager.PERMISSION_GRANTED ==  pm.checkPermission(permission, PACKAGE_NAME));
        Log.d("permission",permission+ " " + has);
        return has;
	}

	public static void getViewHeight(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		view.measure(width,height);
	}


}
