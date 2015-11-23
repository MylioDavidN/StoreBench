package com.dnguyen.storebench;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.dnguyen.storebench.Shell.ShellException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;

public class MyAsyncTaskApps extends AsyncTask<Void, Void, String>{
	
	ResultsListenerApps listener;
	
	public static String androidID;
	public static String appPath;	
	public static String fileName = "myrootdir";
	public static String phoneModel;
	public static String androidVersion = android.os.Build.VERSION.RELEASE;
		
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault());
	public static String currentDateAndTime;
	
	
	
	private Context mContext;	
	
	
	public MyAsyncTaskApps(Context context){
		mContext = context;
	}
	
	
    public void setOnResultsListenerApps(ResultsListenerApps listener) {
        this.listener = listener;
    }
    
    
    
    @Override
    protected String doInBackground(Void... voids) {
    	
    	if (!isNetworkAvailable()){
			// Internet connection
			// show warning, and exit
    		return "nonetwork";
		}
    	
    	
    	appPath = "/data/data/com.dnguyen.storebench";
    	
    	
    	// verify if dbase downloaded
    	// ==========================
    	String dbaseString = "";
    	try {    		
    		dbaseString = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/file.txt");
    	}
    	catch(Exception e){    		
    		return "nodbase";
    	}
    	
    	if (dbaseString == "") {
    		// dbase not downloaded properly
    		// show warning, and exit
    		return "nodbase";
    	}
    	
    	
    	String result = "";
    	String youtubeResult = "", angryResult = "", mapsResult = "", 
    			pandoraResult = "", gmailResult = "", facebookResult = "",
    			twitterResult = "", templeResult = "", cnnResult = "",
    			abcResult = "", simpsonsResult = "", accuResult = "",
    			nightlyResult = "", gtaResult = "", nfsResult = "", 
    			compassResult = "", gyroResult = "", accelResult = "",
    			proxResult = "", presResult = "";
    	
    	androidID = Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID); 
    	
    	currentDateAndTime = sdf.format(new Date());
   
    	String result_1 = "";
    	try {
    		phoneModel = getDeviceName();
    		
    		if (killPackage("com.google.android.youtube") == true){
    			youtubeResult = launchPackage("com.google.android.youtube");
    		}
    		else {
    			youtubeResult = "n/a" + "\n" + "n/a";
    		}
    		    		
    		if (killPackage("com.rovio.angrybirds") == true) {
    			angryResult = launchPackage("com.rovio.angrybirds");
    		
    		}
    		else {
    			angryResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.google.android.apps.maps") == true) {
    			mapsResult = launchPackage("com.google.android.apps.maps");
    		
    		}
    		else {
    			mapsResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.pandora.android") == true) {
    			pandoraResult = launchPackage("com.pandora.android");
    		
    		}
    		else {
    			pandoraResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.google.android.gm") == true) {
    			gmailResult = launchPackage("com.google.android.gm");
    		
    		}
    		else {
    			gmailResult = "n/a" + "\n" + "n/a";
    		}
    		    		
    		if (killPackage("com.facebook.katana") == true) {
    			facebookResult = launchPackage("com.facebook.katana");
    		
    		}
    		else {
    			facebookResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.twitter.android") == true) {
    			twitterResult = launchPackage("com.twitter.android");
    		
    		}
    		else {
    			twitterResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.imangi.templerun2") == true) {
    			templeResult = launchPackage("com.imangi.templerun2");
    		
    		}
    		else {
    			templeResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.cnn.mobile.android.phone") == true) {
    			cnnResult = launchPackage("com.cnn.mobile.android.phone");
    		
    		}
    		else {
    			cnnResult = "n/a" + "\n" + "n/a";
    		}    		
    		
    		if (killPackage("com.abc.abcnews") == true) {
    			abcResult = launchPackage("com.abc.abcnews");
    		
    		}
    		else {
    			abcResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.ea.game.simpsons4_na") == true) {
    			simpsonsResult = launchPackage("com.ea.game.simpsons4_na");
    		
    		}
    		else {
    			simpsonsResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.accuweather.android") == true) {
    			accuResult = launchPackage("com.accuweather.android");
    		
    		}
    		else {
    			accuResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.msnbc.nightlynews") == true) {
    			nightlyResult = launchPackage("com.msnbc.nightlynews");    			
    		
    		}
    		else {
    			nightlyResult = "n/a" + "\n" + "n/a";
    		}
    		    		
    		if (killPackage("com.rockstar.gta3") == true) {
    			gtaResult = launchPackage("com.rockstar.gta3");    			
    		
    		}
    		else {
    			gtaResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.ea.games.nfs13_na") == true) {
    			nfsResult = launchPackage("com.ea.games.nfs13_na");    			
    		
    		}
    		else {
    			nfsResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("kr.sira.compass") == true) {
    			compassResult = launchPackage("kr.sira.compass");    			
    		
    		}
    		else {
    			compassResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("pl.submachine.gyro") == true) {
    			gyroResult = launchPackage("pl.submachine.gyro");    			
    		
    		}
    		else {
    			gyroResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.lul.accelerometer") == true) {
    			accelResult = launchPackage("com.lul.accelerometer");    			
    		
    		}
    		else {
    			accelResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("com.tigermonster.proxfinder") == true) {
    			proxResult = launchPackage("com.tigermonster.proxfinder");    			
    		
    		}
    		else {
    			proxResult = "n/a" + "\n" + "n/a";
    		}
    		
    		if (killPackage("sy.android.sypressure") == true) {
    			presResult = launchPackage("sy.android.sypressure");    			
    		
    		}
    		else {
    			presResult = "n/a" + "\n" + "n/a";
    		}
    		
    		
    		
    		
    		
    		// result to write to dbase
    		// ========================
    		String result4Dbase = phoneModel + ";" + androidVersion + ";" 
    				
    				+ youtubeResult.replace('\n', ';') + ";" + angryResult.replace('\n', ';') + ";" + mapsResult.replace('\n', ';') + ";" 
        			+ pandoraResult.replace('\n', ';') + ";" + gmailResult.replace('\n', ';') + ";" + facebookResult.replace('\n', ';') + ";"
        			+ twitterResult.replace('\n', ';') + ";" + templeResult.replace('\n', ';') + ";" + cnnResult.replace('\n', ';') + ";" 
        			+ abcResult.replace('\n', ';') + ";" + simpsonsResult.replace('\n', ';') + ";" + accuResult.replace('\n', ';') + ";"
        			+ nightlyResult.replace('\n', ';') + ";" + gtaResult.replace('\n', ';') + ";" + nfsResult.replace('\n', ';') + ";"
        			+ compassResult.replace('\n', ';') + ";" + gyroResult.replace('\n', ';') + ";" + accelResult.replace('\n', ';') + ";"
        			+ proxResult.replace('\n', ';') + ";" + presResult.replace('\n', ';') + ";"
    				
    				+ currentDateAndTime + ";" + "apps" + ";" + androidID;
    								
    								 
    		String myDevice = "MY DEVICE" + ";" + androidVersion + ";" 
    				
    				+ youtubeResult.replace('\n', ';') + ";" + angryResult.replace('\n', ';') + ";" + mapsResult.replace('\n', ';') + ";" 
        			+ pandoraResult.replace('\n', ';') + ";" + gmailResult.replace('\n', ';') + ";" + facebookResult.replace('\n', ';') + ";"
        			+ twitterResult.replace('\n', ';') + ";" + templeResult.replace('\n', ';') + ";" + cnnResult.replace('\n', ';') + ";" 
        			+ abcResult.replace('\n', ';') + ";" + simpsonsResult.replace('\n', ';') + ";" + accuResult.replace('\n', ';') + ";"
        			+ nightlyResult.replace('\n', ';') + ";" + gtaResult.replace('\n', ';') + ";" + nfsResult.replace('\n', ';') + ";"
        			+ compassResult.replace('\n', ';') + ";" + gyroResult.replace('\n', ';') + ";" + accelResult.replace('\n', ';') + ";"
        			+ proxResult.replace('\n', ';') + ";" + presResult.replace('\n', ';') + ";"
    				
    				+ currentDateAndTime + ";" + "apps" + ";" + androidID;
    								
    								
    		// write to online dbase				
    		// =====================
    								
    		// write current phone results to file2.txt
    		writeStringToFile(result4Dbase + ";" + "this", "file2.txt");
    								
    								
    		try {
    			HashMap<String, String> data = new HashMap<String, String>();
    			data.put("value", result4Dbase);
    			//data.put("value2", "hello");
    			AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
    			asyncHttpPost.execute("http://smartstorage.us/file3.php");				
    		}
    		catch (Exception e){
    			// network error
    		}
    		
    		
    		// result to output to screen
        	// ==========================
    		result_1 = addSecond(youtubeResult) + "\n" + addSecond(angryResult) + "\n" + addSecond(mapsResult) + "\n" 
        			+ addSecond(pandoraResult) + "\n" + addSecond(gmailResult) + "\n" + addSecond(facebookResult) + "\n"
        			+ addSecond(twitterResult) + "\n" + addSecond(templeResult) + "\n" + addSecond(cnnResult) + "\n" 
        			+ addSecond(abcResult) + "\n" + addSecond(simpsonsResult) + "\n" + addSecond(accuResult) + "\n"
        			+ addSecond(nightlyResult) + "\n" + addSecond(gtaResult) + "\n" + addSecond(nfsResult) + "\n"
        			+ addSecond(compassResult) + "\n" + addSecond(gyroResult) + "\n" + addSecond(accelResult) + "\n"
        			+ addSecond(proxResult) + "\n" + addSecond(presResult);
    		
    		
    		
    		// get order within brand
			//=======================
			String cmd = "/system/bin/cat /data/data/com.dnguyen.storebench/file.txt > /data/data/com.dnguyen.storebench/file3.txt; echo '' >> /data/data/com.dnguyen.storebench/file3.txt; /system/bin/cat /data/data/com.dnguyen.storebench/file2.txt >> /data/data/com.dnguyen.storebench/file3.txt";
			execCmd(cmd);
						 
						
			String cmd01 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep apps | wc -l > /data/data/com.dnguyen.storebench/result01.txt";
			execCmd(cmd01);
						
			String totalInBrand = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result01.txt");
			
			
			
			String strResult1 = getAppOrder(youtubeResult, 3, Integer.parseInt(totalInBrand));
										
			String strResult2 = getAppOrder(angryResult, 5, Integer.parseInt(totalInBrand));
						
			String strResult3 = getAppOrder(mapsResult, 7, Integer.parseInt(totalInBrand));
			
			String strResult4 = getAppOrder(pandoraResult, 9, Integer.parseInt(totalInBrand));
						
			String strResult5 = getAppOrder(gmailResult, 11, Integer.parseInt(totalInBrand));
			
			String strResult6 = getAppOrder(facebookResult, 13, Integer.parseInt(totalInBrand));
			
			String strResult7 = getAppOrder(twitterResult, 15, Integer.parseInt(totalInBrand));
									    		
    		
			String strResult8 = getAppOrder(templeResult, 17, Integer.parseInt(totalInBrand));
			
			String strResult9 = getAppOrder(cnnResult, 19, Integer.parseInt(totalInBrand));
			
			String strResult10 = getAppOrder(abcResult, 21, Integer.parseInt(totalInBrand));
						
			String strResult11 = getAppOrder(simpsonsResult, 23, Integer.parseInt(totalInBrand));
			
			String strResult12 = getAppOrder(accuResult, 25, Integer.parseInt(totalInBrand));
			
			String strResult13 = getAppOrder(nightlyResult, 27, Integer.parseInt(totalInBrand));
			
    		
			String strResult14 = getAppOrder(gtaResult, 29, Integer.parseInt(totalInBrand));
			
			String strResult15 = getAppOrder(nfsResult, 31, Integer.parseInt(totalInBrand));
			
			String strResult16 = getAppOrder(compassResult, 33, Integer.parseInt(totalInBrand));
						
			String strResult17 = getAppOrder(gyroResult, 35, Integer.parseInt(totalInBrand));
			
			String strResult18 = getAppOrder(accelResult, 37, Integer.parseInt(totalInBrand));
			
			String strResult19 = getAppOrder(proxResult, 39, Integer.parseInt(totalInBrand));
			
			String strResult20 = getAppOrder(presResult, 41, Integer.parseInt(totalInBrand));
    		
    		
    		
			
			
			
			// get order among all
			//=======================
			String cmdAll = "/system/bin/cat /data/data/com.dnguyen.storebench/file.txt > /data/data/com.dnguyen.storebench/file3.txt; echo '' >> /data/data/com.dnguyen.storebench/file3.txt; /system/bin/cat /data/data/com.dnguyen.storebench/file2.txt >> /data/data/com.dnguyen.storebench/file3.txt";
			execCmd(cmdAll);
						 
						
			String cmd01All = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep apps | wc -l > /data/data/com.dnguyen.storebench/result01All.txt";
			execCmd(cmd01All);
						
			String totalAll = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result01All.txt");
			
			
			
			String strResult1All = getAppOrderAll(youtubeResult, 3, Integer.parseInt(totalAll));

			String strResult2All = getAppOrderAll(angryResult, 5, Integer.parseInt(totalAll));
			
			String strResult3All = getAppOrderAll(mapsResult, 7, Integer.parseInt(totalAll));
			
			String strResult4All = getAppOrderAll(pandoraResult, 9, Integer.parseInt(totalAll));
						
			String strResult5All = getAppOrderAll(gmailResult, 11, Integer.parseInt(totalAll));
			
			String strResult6All = getAppOrderAll(facebookResult, 13, Integer.parseInt(totalAll));
			
			String strResult7All = getAppOrderAll(twitterResult, 15, Integer.parseInt(totalAll));
									    		
    		
			String strResult8All = getAppOrderAll(templeResult, 17, Integer.parseInt(totalAll));
			
			String strResult9All = getAppOrderAll(cnnResult, 19, Integer.parseInt(totalAll));
			
			String strResult10All = getAppOrderAll(abcResult, 21, Integer.parseInt(totalAll));
						
			String strResult11All = getAppOrderAll(simpsonsResult, 23, Integer.parseInt(totalAll));
			
			String strResult12All = getAppOrderAll(accuResult, 25, Integer.parseInt(totalAll));
			
			String strResult13All = getAppOrderAll(nightlyResult, 27, Integer.parseInt(totalAll));
			
    		
			String strResult14All = getAppOrderAll(gtaResult, 29, Integer.parseInt(totalAll));
			
			String strResult15All = getAppOrderAll(nfsResult, 31, Integer.parseInt(totalAll));
			
			String strResult16All = getAppOrderAll(compassResult, 33, Integer.parseInt(totalAll));
						
			String strResult17All = getAppOrderAll(gyroResult, 35, Integer.parseInt(totalAll));
			
			String strResult18All = getAppOrderAll(accelResult, 37, Integer.parseInt(totalAll));
			
			String strResult19All = getAppOrderAll(proxResult, 39, Integer.parseInt(totalAll));
			
			String strResult20All = getAppOrderAll(presResult, 41, Integer.parseInt(totalAll));
    		
			
			
			
			// Total Rank 
			// ==========			
			int cumulativeRankHelp = 0, numOfCategories = 0;			
			
			if (!strResult1.startsWith("n")) {
				String[] str1 = strResult1.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult2.startsWith("n")) {
				String[] str1 = strResult2.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult3.startsWith("n")) {
				String[] str1 = strResult3.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult4.startsWith("n")) {
				String[] str1 = strResult4.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult5.startsWith("n")) {
				String[] str1 = strResult5.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult6.startsWith("n")) {
				String[] str1 = strResult6.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult7.startsWith("n")) {
				String[] str1 = strResult7.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult8.startsWith("n")) {
				String[] str1 = strResult8.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult9.startsWith("n")) {
				String[] str1 = strResult9.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult10.startsWith("n")) {
				String[] str1 = strResult10.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult11.startsWith("n")) {
				String[] str1 = strResult11.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult12.startsWith("n")) {
				String[] str1 = strResult12.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult13.startsWith("n")) {
				String[] str1 = strResult13.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult14.startsWith("n")) {
				String[] str1 = strResult14.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult15.startsWith("n")) {
				String[] str1 = strResult15.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult16.startsWith("n")) {
				String[] str1 = strResult16.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult17.startsWith("n")) {
				String[] str1 = strResult17.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult18.startsWith("n")) {
				String[] str1 = strResult18.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult19.startsWith("n")) {
				String[] str1 = strResult19.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult20.startsWith("n")) {
				String[] str1 = strResult20.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			
			
			int cumulativeRank = Math.round(cumulativeRankHelp / numOfCategories);
			String strCumulativeRank = Integer.toString(cumulativeRank) + "/" + totalInBrand;
			
			
			cumulativeRankHelp = 0; numOfCategories = 0;			
			
			if (!strResult1All.startsWith("n")) {
				String[] str1 = strResult1All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult2All.startsWith("n")) {
				String[] str1 = strResult2All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult3All.startsWith("n")) {
				String[] str1 = strResult3All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult4All.startsWith("n")) {
				String[] str1 = strResult4All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult5All.startsWith("n")) {
				String[] str1 = strResult5All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult6All.startsWith("n")) {
				String[] str1 = strResult6All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult7All.startsWith("n")) {
				String[] str1 = strResult7All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult8All.startsWith("n")) {
				String[] str1 = strResult8All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult9All.startsWith("n")) {
				String[] str1 = strResult9All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult10All.startsWith("n")) {
				String[] str1 = strResult10All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult11All.startsWith("n")) {
				String[] str1 = strResult11All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult12All.startsWith("n")) {
				String[] str1 = strResult12All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult13All.startsWith("n")) {
				String[] str1 = strResult13All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult14All.startsWith("n")) {
				String[] str1 = strResult14All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult15All.startsWith("n")) {
				String[] str1 = strResult15All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult16All.startsWith("n")) {
				String[] str1 = strResult16All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult17All.startsWith("n")) {
				String[] str1 = strResult17All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult18All.startsWith("n")) {
				String[] str1 = strResult18All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult19All.startsWith("n")) {
				String[] str1 = strResult19All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			if (!strResult20All.startsWith("n")) {
				String[] str1 = strResult20All.split("/");				
				cumulativeRankHelp = cumulativeRankHelp + Integer.parseInt(str1[0]);
				numOfCategories++;
			}
			
			
			int cumulativeRankAll = Math.round(cumulativeRankHelp / numOfCategories);
			String strCumulativeRankAll = Integer.toString(cumulativeRankAll) + "/" + totalAll;
			
			
			// get global APPS ranking
			// =========================
			String cmdGlobal = "/system/bin/cat /data/data/com.dnguyen.storebench/file.txt | grep apps > /data/data/com.dnguyen.storebench/file3apps.txt";
			execCmd(cmdGlobal);
			
			writeStringToFile(myDevice, "mydeviceapps.txt");
			
			// merge to 1 file
			String cmdMerge = "/system/bin/cat /data/data/com.dnguyen.storebench/file3apps.txt > /data/data/com.dnguyen.storebench/file4apps.txt; /system/bin/cat /data/data/com.dnguyen.storebench/mydeviceapps.txt >> /data/data/com.dnguyen.storebench/file4apps.txt";
			execCmd(cmdMerge);
			
			String cmdGetGlobal = "/system/bin/cat /data/data/com.dnguyen.storebench/file4apps.txt";
			String file4apps = getShellOutput(cmdGetGlobal);
			
			String[] phones = file4apps.split("\n");			
			double[] delay = new double[phones.length];
			
			// add average delay of all apps to the end of each phone
			String newPhones = "";
			
			String[] fieldsOfMyDevice = phones[phones.length - 1].split(";");
			
			for (int i = 0; i < phones.length; i++) {
				
				String[] fields = phones[i].split(";");
				delay[i] = 0;
				int numOfValues = 0;
				
				
				for (int j = 2; j <= 41; j++) {
					// care only about apps available on myDevice
					if (fieldsOfMyDevice[j] != "n/a") {
						try {
							  double value = Double.parseDouble(fields[j]); // Make use of autoboxing.  It's also easier to read.
							  delay[i] = delay[i] + value;
							  
							  numOfValues++;
						} catch (NumberFormatException e) {
							  // p did not contain a valid double
						}
										
					}
				}
				
				if (numOfValues != 0) {
					double average = delay[i] / numOfValues;				
					phones[i] = phones[i] + ";" + String.format("%.3f", average);
					
					newPhones = newPhones + phones[i] + "\n";
				}
				else {
					// no result in APPS category
					phones[i] = phones[i] + ";" + "n/a";
				}
				//newPhones = newPhones + phones[i] + "\n";
			}
			
			
			// write modified lines to file
			writeStringToFile(newPhones, "file4aapps.txt");
			
			// sort phones based on delay results
			String cmdSort = "/system/bin/cat /data/data/com.dnguyen.storebench/file4aapps.txt | sort -t';' -n -k46 > /data/data/com.dnguyen.storebench/file4bapps.txt";
			execCmd(cmdSort);
			String cmdGetSorted = "/system/bin/cat /data/data/com.dnguyen.storebench/file4bapps.txt";
			String file4b = getShellOutput(cmdGetSorted);
			
			
			// get best results for each brand
			String[] linesFile4b = file4b.split("\n");
			String brands = "";
			String file4c = "";
			for (int i = 0; i < linesFile4b.length; i++) {
				String[] fields = linesFile4b[i].split(";");
				
				if (!brands.toLowerCase().contains(fields[0].toLowerCase())) {
					if (brands == "") {
						brands = fields[0];
						file4c = linesFile4b[i];
					}
					else {
						String help = brands + ";" + fields[0];
						brands = help;
						
						String help2 = file4c + "\n" + linesFile4b[i];
						file4c = help2;
					}
						
				}
			}
			
			writeStringToFile(brands, "brandsapps.txt");
			writeStringToFile(file4c, "file4capps.txt");
			
			
			
			
			
			
			
			
			
			
			
    		// result to output to screen
        	// =========== ===============
        	result = phoneModel + "\n" + androidVersion + "\n" + result_1 + "\n" 
        		+ currentDateAndTime + "\n"
        		+ "apps" + "\n"        		
        		+ strResult1 + "\n"
        		+ strResult2 + "\n"        
        		+ strResult3 + "\n"
        		+ strResult4 + "\n"
        		+ strResult5 + "\n"
        		+ strResult6 + "\n"
        		+ strResult7 + "\n"
        		+ strResult8 + "\n"
        		+ strResult9 + "\n"
        		+ strResult10 + "\n"
        		+ strResult11 + "\n"
        		+ strResult12 + "\n"
        		+ strResult13 + "\n"
        		+ strResult14 + "\n"
        		+ strResult15 + "\n"
        		+ strResult16 + "\n"
        		+ strResult17 + "\n"
        		+ strResult18 + "\n"
        		+ strResult19 + "\n"
        		+ strResult20 + "\n"
        		+ strResult1All + "\n"
        		+ strResult2All + "\n"        
        		+ strResult3All + "\n"
        		+ strResult4All + "\n"
        		+ strResult5All + "\n"
        		+ strResult6All + "\n"
        		+ strResult7All + "\n"
        		+ strResult8All + "\n"
        		+ strResult9All + "\n"
        		+ strResult10All + "\n"
        		+ strResult11All + "\n"
        		+ strResult12All + "\n"
        		+ strResult13All + "\n"
        		+ strResult14All + "\n"
        		+ strResult15All + "\n"
        		+ strResult16All + "\n"
        		+ strResult17All + "\n"
        		+ strResult18All + "\n"
        		+ strResult19All + "\n"
        		+ strResult20All + "\n"
        		+ strCumulativeRank + "\n"
        		+ strCumulativeRankAll + "\n"
        		+ file4c;
    		
    		
    	}
    	catch (Exception e){   
    		result ="apps_doInBackground_error";
    	}
    	
    	
    	
    	
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
       listener.onResultsSucceededApps(result);

    }
    
    
    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    
    
    public String getAppOrderAll(String result, int start, int totalAll){
    	// Angry Birds order
		// =================
		String strResult2;
		
		if (result.startsWith("n")) {
			strResult2 = "n/a";
		}
		else {
			String cmd1a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep apps | sort -n -t';' -k" + Integer.toString(start) + " | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1a.txt";
			execCmd(cmd1a);
						
			String result1a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1a.txt");

			String cmd1b = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep apps | sort -n -t';' -k" + Integer.toString(start + 1) + " | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1b.txt";
			execCmd(cmd1b);
						
			String result1b = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1b.txt");
			
			int result1 = Math.round(( Integer.parseInt(result1a) + Integer.parseInt(result1b)) / 2); 
			strResult2 = Integer.toString(result1) + "/" + totalAll;
		}	
		
		return strResult2;
    }
    
    
    public String getAppOrder(String result, int start, int totalInBrand){
    	// Angry Birds order
		// =================
		String strResult2;
		
		if (result.startsWith("n")) {
			strResult2 = "n/a";
		}
		else {
			String cmd1a = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep apps | sort -n -t';' -k" + Integer.toString(start) + " | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1a.txt";
			execCmd(cmd1a);
						
			String result1a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1a.txt");

			String cmd1b = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep apps | sort -n -t';' -k" + Integer.toString(start + 1) + " | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1b.txt";
			execCmd(cmd1b);
						
			String result1b = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1b.txt");
			
			int result1 = Math.round(( Integer.parseInt(result1a) + Integer.parseInt(result1b)) / 2); 
			strResult2 = Integer.toString(result1) + "/" + totalInBrand;
		}	
		
		return strResult2;
    }
    
    
    
    public String addSecond(String input){
    	
    	String newLine0, newLine1;
    	
    	// split 
    	String[] line = input.split("\n");
    	
    	// add 's'
    	if (line[0].startsWith("n")) {
    		newLine0 = line[0];
    	}
    	else {
    		newLine0 = line[0] + "s";
    	}
    	
    	if (line[1].startsWith("n")){
    		newLine1 = line[1];
    	}
    	else {
    		newLine1 = line[1] + "s";
    	}
    	
    	
    	return (newLine0 + "\n" + newLine1);
    	
    }
    
    public void writeStringToFile(String str, String file) {
		
		
    	OutputStream out = null;
    	
    	String newFileName = appPath + "/" + file;
        try {
			out = new FileOutputStream(newFileName);
			out.write(str.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
	}

    public String capitalize(String s) {
		  if (s == null || s.length() == 0) {
		    return "";
		  }
		  char first = s.charAt(0);
		  if (Character.isUpperCase(first)) {
		    return s;
		  } else {
		    return Character.toUpperCase(first) + s.substring(1);
		  }
	} 

	
    
    public boolean killPackage(String packageName) {
    	boolean result = true;
    	
    	if (appInstalledOrNot(packageName) == false)
    		// app not installed, so cannot kill app
    		return false;
    	
    	else {
    		// app installed, kill it
    		
    		// find its PID
    		String getPID = "ps | grep " + packageName + " | xargs | cut -d' ' -f2 > /data/data/com.dnguyen.storebench/pid.txt";
			execCmd(getPID);
			String PID = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/pid.txt");
			
			// kill it
			if (PID != "") {
				String killPID = "kill " + PID;
				execCmd(killPID);
			}
			
    	}
    	
    	return result;
    }
    
    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try
        {
               pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
               app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
               app_installed = false;
        }
        return app_installed ;
    }
    
    
    public String launchPackage(String packageName){
    	String launchReal2;
    	String runtimeReal2;
    	
    	try {
			// launch delay
			// =============
			String cmdLaunch = "(time monkey -v -s 10 -p " + packageName + " 1) 2> " + appPath + "/" + packageName + "launch.txt";
				
			String cmdLaunch2 = "/system/bin/cat " + appPath + "/" + packageName + "launch.txt | xargs > " + appPath + "/" + packageName + "launchtrim.txt";
			execCmd(cmdLaunch + ";" + cmdLaunch2);
			
			String launchResult = getShellOutput("/system/bin/cat " + appPath + "/" + packageName + "launchtrim.txt");
			
			String[] launch = launchResult.split(" ");
			String launchReal = (launch[0].split("m"))[1];
			launchReal2 = (launchReal.split("s"))[0];
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// run-time delay
			// ===============
			String cmdRuntime = "(time monkey -v -s 10 -p " + packageName + " 500) 2> " + appPath + "/" + packageName + "runtime.txt";
			
			String cmdRuntime2 = "/system/bin/cat " + appPath + "/" + packageName + "runtime.txt | xargs > " + appPath + "/" + packageName + "runtimetrim.txt";
			execCmd(cmdRuntime + ";" + cmdRuntime2);
			
			String runtimeResult = getShellOutput("/system/bin/cat " + appPath + "/" + packageName + "runtimetrim.txt");
		
			String[] runtime = runtimeResult.split(" ");
			String runtimeReal = (runtime[0].split("m"))[1];
			runtimeReal2 = (runtimeReal.split("s"))[0];
			
			// close the app
			killPackage(packageName);
    	}
    	catch (Exception e){
    		launchReal2 = "n/a";
    		runtimeReal2 = "n/a";
    		return launchReal2 + "\n" + runtimeReal2;
    	}
		
		return launchReal2 + "\n" + runtimeReal2;
	}
	
    
    
    public void execCmd(String command)
   	{
   	    Runtime runtime = Runtime.getRuntime();
   	    Process proc = null;
   	    OutputStreamWriter osw = null;

   	    try
   	    {
   	        proc = runtime.exec("su");
   	        osw = new OutputStreamWriter(proc.getOutputStream());
   	        osw.write(command);
   	        osw.flush();
   	        osw.close();
   	    }
   	    catch (IOException ex)
   	    {
   	        Log.e("execCommandLine()", "Command resulted in an IO Exception: " + command);
   	        return;
   	    }
   	    finally
   	    {
   	        if (osw != null)
   	        {
   	            try
   	            {
   	                osw.close();
   	            }
   	            catch (IOException e){}
   	        }
   	    }

   	    try 
   	    {
   	        proc.waitFor();
   	    }
   	    catch (InterruptedException e){}

   	    if (proc.exitValue() != 0)
   	    {
   	        Log.e("execCommandLine()", "Command returned error: " + command + "\n  Exit code: " + proc.exitValue());
   	    }
   	}
       
       
       
       public String getShellOutput(String cmd) {
   		String output = null;
   		
   		try {
   			output = Shell.sudo(cmd);
   			
   		} catch (ShellException e) {
   			Log.e("shell-example", e.getMessage());
   		}

   		
   		return output;
   	}
       
       public void cleanCache(){
   		
   		String cmd = "echo 3 > /proc/sys/vm/drop_caches";
   		//execCmd(cmd);
   		
   		try {
   			Shell.sudo(cmd);
   		} catch (ShellException e) {
   			e.printStackTrace();
   		}
   	}
    
       
       /*
       
       public String getAppPath() {
       	
       	PackageManager m = mContext.getPackageManager();
   		String appPath = mContext.getPackageName();
   		try {
   		    PackageInfo p = m.getPackageInfo(appPath, 0);
   		    appPath = p.applicationInfo.dataDir;
   		} catch (NameNotFoundException e) {
   		    Log.w("yourtag", "Error package name not found.", e);
   		    return "";
   		}
   		
   		return appPath;
       }   
       
       */
       
       
       
    
    
}
