package com.dnguyen.storebench;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.dnguyen.storebench.AsyncHttpPost;
import com.dnguyen.storebench.Shell;
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

public class MyAsyncTaskFlash extends AsyncTask<Void, Void, String> {
	
	ResultsListener listener;
	
	public static String androidID;
	public static String appPath;
	public static String fileName;
	public static String phoneModel;
	public static String androidVersion = android.os.Build.VERSION.RELEASE;
	public static int categories = 4;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault());
	public static String currentDateAndTime;
	
	private Context mContext;	
	
	
	public MyAsyncTaskFlash(Context context){
		mContext = context;
	}
	
	
    public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(Void... voids) {
    	
    	// check network connectivity
    	// ==========================
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
    	
    	
    	
    	androidID = Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID); 
    	
    	currentDateAndTime = sdf.format(new Date());
    	String result = "";
    	
    	
    	/*
    	 String io_slowdown_seqread_testResult = "";
    	String io_slowdown_seqwrite_testResult = "";
    	String io_slowdown_randread_testResult = "";
    	String io_slowdown_randwrite_testResult = "";
    	*/
    	String testResult = "";
    	
    	try{
    		
    		
    		phoneModel = getDeviceName();
    	      	  
    	  	testResult = ioPerformance();
    	  	
    	  	
    		//String testResult = "hello";
    		cleanCache();
    	  	
    		String[] line = testResult.split("\n");
    	  	
    	  	 
//    	  	io_slowdown_seqread_testResult = ioSlowdownSeqread();
//    	  	cleanCache();
//    	  	io_slowdown_seqwrite_testResult = ioSlowdownSeqwrite();
//    	  	cleanCache();
//    	  	io_slowdown_randread_testResult = ioSlowdownRandread();
//    	  	cleanCache();
//    	  	io_slowdown_randwrite_testResult = ioSlowdownRandwrite();
//			
//			
//			String output = phoneModel + ";" + androidVersion + ";" + line[0] + ";" + line[1] + ";" + line[2] + ";" + line[3];
//			
			
			
			// get #processes total
			String cmd5a = "/system/bin/ps | wc -l > /data/data/com.dnguyen.storebench/result5a.txt";
			execCmd(cmd5a);			
			String result5a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result5a.txt");
			
			// get # user processes
			String cmd5b = "/system/bin/ps | grep u0 | wc -l > /data/data/com.dnguyen.storebench/result5b.txt";
			execCmd(cmd5b);			
			String result5b = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result5b.txt");
			
			
			// get total internal memory (/data partition)
			String cmd6a = "df | grep data | xargs | cut -d' ' -f2 > /data/data/com.dnguyen.storebench/result6a.txt";
			execCmd(cmd6a);			
			String result6a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result6a.txt");
			
			// get available internal memory (/data partition)
			String cmd7a = "df | grep data | xargs | cut -d' ' -f4 > /data/data/com.dnguyen.storebench/result7a.txt";
			execCmd(cmd7a);			
			String result7a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result7a.txt");
			
			// get block size (/data partition)    		
			String cmd8a = "df | grep data | xargs | cut -d' ' -f5 > /data/data/com.dnguyen.storebench/result8a.txt";
			execCmd(cmd8a);			
			String result8a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result8a.txt");
			int blockSize = Integer.parseInt(result8a) / 1000;// in KB
			String blockSizeStr = Integer.toString(blockSize);
			
			// get sdcard block size 
			String cmd8asd = "df | grep -i sdcard | xargs | cut -d' ' -f5 > /data/data/com.dnguyen.storebench/result8asd.txt";
			execCmd(cmd8asd);			
			String result8asd = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result8asd.txt");
			String blockSizeStrsd;
			int blockSizesd;
			try { 
				blockSizesd = Integer.parseInt(result8asd) / 1000;// in KB
			}
			catch (NumberFormatException e){
				// does not have SD card!
				blockSizesd = 0;
			}
			blockSizeStrsd = Integer.toString(blockSizesd);
			
			String resultsd;
			String resultsdfree;
			if (blockSizesd == 0) {
				resultsd = "0";
				resultsdfree = "0";
			}
			else {
				// get total sdcard memory (/sdcard /extsdcard etc.)
				String cmdsd = "df | grep -i sdcard | xargs | cut -d' ' -f2 > /data/data/com.dnguyen.storebench/resultsd.txt";
				execCmd(cmdsd);			
				resultsd = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/resultsd.txt");
				
				// get available sdcard memory
				String cmdsdfree = "df | grep -i sdcard | xargs | cut -d' ' -f4 > /data/data/com.dnguyen.storebench/resultsdfree.txt";
				execCmd(cmdsdfree);			
				resultsdfree = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/resultsdfree.txt");
			}
			
			
			
			
			// get iowait
			// ==========
			
			String iostatUser = "iostat | grep -A 1 iowait | tail -n1 | xargs | cut -d' ' -f1 > /data/data/com.dnguyen.storebench/iostatuser.txt";
			execCmd(iostatUser);
			
			String resultIostatUser = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/iostatuser.txt");
			
			String iostatResults = "";
			String userStr = "";
			String sysStr = "";
			String iowaitStr = "";
			
			if (resultIostatUser == "") {
				userStr = "n/a";
				sysStr = "n/a";
				iowaitStr = "n/a";
				iostatResults = userStr + "\n" + sysStr + "\n" + iowaitStr;				
			}			
			else {
				String iostatSystem = "iostat | grep -A 1 iowait | tail -n1 | xargs | cut -d' ' -f3 > /data/data/com.dnguyen.storebench/iostatsystem.txt";
				execCmd(iostatSystem);
				
				String resultIostatSystem = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/iostatsystem.txt");
				
				String iostatIowait = "iostat | grep -A 1 iowait | tail -n1 | xargs | cut -d' ' -f4 > /data/data/com.dnguyen.storebench/iostatiowait.txt";
				execCmd(iostatIowait);
				
				String resultIostatIowait = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/iostatiowait.txt");
				
				double totalIostat = Double.parseDouble(resultIostatUser) + Double.parseDouble(resultIostatSystem) 
									+ Double.parseDouble(resultIostatIowait);
				int user = (int) Math.round((Double.parseDouble(resultIostatUser) / totalIostat) * 100);
				userStr = Integer.toString(user);
				
				int sys = (int) Math.round((Double.parseDouble(resultIostatSystem) / totalIostat) * 100);
				sysStr = Integer.toString(sys);
				
				iowaitStr = Integer.toString(100 - user - sys);
				
				iostatResults = userStr + "\n" + sysStr + "\n" + iowaitStr;
			}
			
		    
			
			
			// get single R/W response time
			// ============================
							
			double seqR = blockSize * 1000 / Double.parseDouble(line[0]); // in ms				
			String seqRstr = String.format("%.3f", seqR);
			
			double seqW = blockSize * 1000 / Double.parseDouble(line[1]); // in ms				
			String seqWstr = String.format("%.3f", seqW);
			
			double randR = blockSize * 1000 / Double.parseDouble(line[2]); // in ms				
			String randRstr = String.format("%.3f", randR);
			
			double randW = blockSize * 1000 / Double.parseDouble(line[3]); // in ms				
			String randWstr = String.format("%.3f", randW);
			
			String singleResponseTime = seqRstr + "\n" + seqWstr + "\n" + randRstr + "\n" + randWstr;
			
			
			
			// get slowdown
			// ============
//			double seqRslowdown = 5 + Double.parseDouble(line[0]) / Double.parseDouble(io_slowdown_seqread_testResult);
//			String seqRslowdownStr = String.format("%.3f", seqRslowdown);
//			
//			double seqWslowdown = Double.parseDouble(line[1]) / Double.parseDouble(io_slowdown_seqwrite_testResult);
//			String seqWslowdownStr = String.format("%.3f", seqWslowdown);
//			
//			double randRslowdown = 2 + Double.parseDouble(line[2]) / Double.parseDouble(io_slowdown_randread_testResult);
//			String randRslowdownStr = String.format("%.3f", randRslowdown);
//			
//			double randWslowdown = Double.parseDouble(line[3]) / Double.parseDouble(io_slowdown_randwrite_testResult);
//			String randWslowdownStr = String.format("%.3f", randWslowdown);
			
			
			
			// result to output to screen
			// ==========================
			String result_1 = phoneModel + "\n" + androidVersion + "\n" + testResult + "\n" 
			+ currentDateAndTime + "\n" + result5a + "\n" + result5b + "\n"+ result6a
			+ "\n" + result7a + "\n" + blockSizeStr + "\n" + resultsd + "\n" + resultsdfree
			+ "\n" + blockSizeStrsd + "\n" + singleResponseTime + "\n" + iostatResults + "\n"
			+ "flash";
			
			
			// result to write to dbase
			// ========================
			String result4Dbase = phoneModel + ";" + androidVersion + ";" + line[0] + ";" + line[1] + ";" 
					+ line[2] + ";" + line[3] + ";"
					+ currentDateAndTime + ";" + result5a + ";" + result5b + ";"+ result6a
					+ ";" + result7a + ";" + blockSizeStr + ";" + resultsd + ";" + resultsdfree
					+ ";" + blockSizeStrsd + ";" + seqRstr + ";" + seqWstr + ";" + randRstr + ";" + randWstr + ";" + userStr + ";" + sysStr + ";" + iowaitStr + ";"
					+ "flash" + ";" + androidID;
			
			String myDevice = "MY DEVICE" + ";" + androidVersion + ";" + line[0] + ";" + line[1] + ";" 
					+ line[2] + ";" + line[3] + ";"
					+ currentDateAndTime + ";" + result5a + ";" + result5b + ";"+ result6a
					+ ";" + result7a + ";" + blockSizeStr + ";" + resultsd + ";" + resultsdfree
					+ ";" + blockSizeStrsd + ";" + seqRstr + ";" + seqWstr + ";" + randRstr + ";" + randWstr + ";" + userStr + ";" + sysStr + ";" + iowaitStr + ";"
					+ "flash" + ";" + androidID;
			 //output = output + ";" + currentDateAndTime + ";" + result5a + ";" + result5b + ";" + result6a + ";"+ result7a + ";" + result8a;
			
			
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
			
			
			
			
			
			// get order within brand
			//=======================
			String cmd = "/system/bin/cat /data/data/com.dnguyen.storebench/file.txt > /data/data/com.dnguyen.storebench/file3.txt; echo '' >> /data/data/com.dnguyen.storebench/file3.txt; /system/bin/cat /data/data/com.dnguyen.storebench/file2.txt >> /data/data/com.dnguyen.storebench/file3.txt";
			execCmd(cmd);
			 
			
			String cmd01 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep flash | wc -l > /data/data/com.dnguyen.storebench/result01.txt";
			execCmd(cmd01);
			
			String totalInBrand = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result01.txt");
			
			String cmd1 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k3 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1.txt";
			execCmd(cmd1);
			
			String result1 = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1.txt");
			
			String cmd2 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k4 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result2.txt";
			execCmd(cmd2);
			
			String result2 = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result2.txt");
			
			String cmd3 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k5 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result3.txt";
			execCmd(cmd3);
			
			String result3 = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result3.txt");
			
			String cmd4 = "grep -i " + phoneModel + " /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k6 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result4.txt";
			execCmd(cmd4);
			
			String result4 = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result4.txt");
			
			//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Rank within brand: " + result4, Toast.LENGTH_LONG).show();
			
			int cumulativeRank = Math.round(( Integer.parseInt(result1) + Integer.parseInt(result2) + Integer.parseInt(result3) + Integer.parseInt(result4) ) / categories); 
			String strCumulativeRank = Integer.toString(cumulativeRank);
			
			
			
			
			
			// get order among all phones
			// ==========================
			
			String cmd1a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k3 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result1a.txt";
			execCmd(cmd1a);
							
			String result1a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result1a.txt");
			
			String cmd01a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | wc -l > /data/data/com.dnguyen.storebench/result01a.txt";
			execCmd(cmd01a);
			
			String totalAll = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result01a.txt");
			
			
			
			String cmd2a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k4 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result2a.txt";
			execCmd(cmd2a);
			
			
			String result2a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result2a.txt");
			
			String cmd3a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k5 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result3a.txt";
			execCmd(cmd3a);
			
			String result3a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result3a.txt");
			
			String cmd4a = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -rn -t';' -k6 | grep -ne this | cut -d':' -f1 > /data/data/com.dnguyen.storebench/result4a.txt";
			execCmd(cmd4a);
			
			String result4a = getShellOutput("/system/bin/cat /data/data/com.dnguyen.storebench/result4a.txt");
			
			int cumulativeRanka = Math.round(( Integer.parseInt(result1a) + Integer.parseInt(result2a) + Integer.parseInt(result3a) + Integer.parseInt(result4a) ) / categories); 
			String strCumulativeRanka = Integer.toString(cumulativeRanka);
			
			
			
			// get global FLASH ranking
			// ========================
			//String cmdGlobal = "/system/bin/cat /data/data/com.dnguyen.storebench/file3.txt | grep flash | sort -t';' -u -k1,1 > /data/data/com.dnguyen.storebench/file3unique.txt";
			String cmdGlobal = "/system/bin/cat /data/data/com.dnguyen.storebench/file.txt | grep flash > /data/data/com.dnguyen.storebench/file3flash.txt";
			execCmd(cmdGlobal);
			
			writeStringToFile(myDevice, "mydevice.txt");
			
			// merge to 1 file
			String cmdMerge = "/system/bin/cat /data/data/com.dnguyen.storebench/file3flash.txt > /data/data/com.dnguyen.storebench/file4.txt; /system/bin/cat /data/data/com.dnguyen.storebench/mydevice.txt >> /data/data/com.dnguyen.storebench/file4.txt";
			execCmd(cmdMerge);
			
			String cmdGetGlobal = "/system/bin/cat /data/data/com.dnguyen.storebench/file4.txt";
			String file4 = getShellOutput(cmdGetGlobal);			
			String[] phones = file4.split("\n");
			
			int[] sumFlash = new int[phones.length];
			// add sum of flash categories at the end of each phone
			String newPhones = "";
			
			for (int i = 0; i < phones.length; i++) {
				String[] fields = phones[i].split(";");
				
				sumFlash[i] = Integer.parseInt(fields[2]) + Integer.parseInt(fields[3]) + Integer.parseInt(fields[4]) + Integer.parseInt(fields[5]);
				phones[i] = phones[i] + ";" + sumFlash[i];
				
				newPhones = newPhones + phones[i] + "\n";
			}
			
			// write modified lines to file
			writeStringToFile(newPhones, "file4a.txt");
			
			// sort phones based on flash results
			String cmdSort = "/system/bin/cat /data/data/com.dnguyen.storebench/file4a.txt | sort -t';' -nr -k25 > /data/data/com.dnguyen.storebench/file4b.txt";
			execCmd(cmdSort);
			String cmdGetSorted = "/system/bin/cat /data/data/com.dnguyen.storebench/file4b.txt";
			String file4b = getShellOutput(cmdGetSorted);
			
			// get best results for each brand
			String[] linesFile4b = file4b.split("\n");
			String brands = "";
			String file4c = "";
			for (int i = 0; i < linesFile4b.length; i++) {
				String[] fields = linesFile4b[i].split(";");
				
				if (!brands.toLowerCase(Locale.getDefault()).contains(fields[0].toLowerCase())) {
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
			
			writeStringToFile(brands, "brands.txt");
			writeStringToFile(file4c, "file4c.txt");
			
			
			result = result_1 + "\n" + result1 + "\n" + result2 + "\n" 
					+ result3 + "\n" + result4 + "\n" + strCumulativeRank + "\n" + totalInBrand 
					+ "\n" + result1a + "\n" + result2a + "\n" + result3a + "\n" + result4a 
					+ "\n" + strCumulativeRanka + "\n" + totalAll + "\n" + file4c; 
			
			//result = testResult;
    	}
    	catch (Exception e) {
    		result ="flash_doInBackground_error";
        }
    	
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
       listener.onResultsSucceeded(result);

    }
    
    
    
    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    
    public String ioPerformance() {
		
		//testResultTemp = "entered ioPerformance";		
		String result = null; 
		fileName ="myrootdir";
		
		
		//appPath = getAppPath();
		
		
		//appPath = "/data/data/com.dnguyen.storebench";
		String ioPerformanceScript = "io_performance.fio";
		//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Benchmarking...", Toast.LENGTH_LONG).show();
		
		try {
			//testResultTemp = "entered try in ioPerformance";
			Process proc;
			proc = Runtime.getRuntime().exec(appPath + "/" + fileName + "/" + "fio" + " " + appPath + "/" + fileName + "/" + ioPerformanceScript);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
			    output.append(buffer, 0, read);
			}
			reader.close();		    
			proc.waitFor();
			
			//save results to /data/data/com.storebenchbeta		  
			BufferedWriter out = new BufferedWriter(new FileWriter(appPath + "/" + "results_io_performance.txt"));			
			out.write(output.toString());
			out.close();
			
			//testResultTemp = "reach end of try in ioPerformance";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "IOException";
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "Interrupt exception";
			e.printStackTrace();
		}    	
		
		
		String cmd = "/system/bin/cat " + appPath + "/results_io_performance.txt | grep aggrb | cut -d'=' -f2,3,7 | sed -e 's/\\([^,]*\\),[^=]*=\\([^,]*\\),[^=]*=\\(.*\\)/\\2/' | cut -d'K' -f1 > "+ appPath +"/results_io_performance_short.txt";
		execCmd(cmd);
		
		String cmdChmod = "/system/bin/chmod 777 " + appPath + "/results_io_performance_short.txt";
		execCmd(cmdChmod);
	
		result = getShellOutput("/system/bin/cat " + appPath + "/results_io_performance_short.txt");		
		
		//result = "jesus where is the problem? \n\n\n\n why isnt it workin???";
		
		return result;
				
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


    public String ioSlowdownSeqread() {
		
		//testResultTemp = "entered ioPerformance";		
		String result = null; 
		
		//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Benchmarking...", Toast.LENGTH_LONG).show();
		
		try {
			//testResultTemp = "entered try in ioPerformance";
			Process proc;
			proc = Runtime.getRuntime().exec(appPath + "/" + fileName + "/" + "fio" + " " + appPath + "/" + fileName + "/" + "io_slowdown_seqread.fio");
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
			    output.append(buffer, 0, read);
			}
			reader.close();		    
			proc.waitFor();
			
			//save results to /data/data/com.storebenchbeta		  
			BufferedWriter out = new BufferedWriter(new FileWriter(appPath + "/" + fileName + "/" + "results_io_slowdown_seqread.txt"));			
			out.write(output.toString());
			out.close();
			
			//testResultTemp = "reach end of try in ioPerformance";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "IOException";
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "Interrupt exception";
			e.printStackTrace();
		}    	
		
		
		String cmd = "cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqread.txt | grep aggrb | cut -d'=' -f2,3,7 | sed -e 's/\\([^,]*\\),[^=]*=\\([^,]*\\),[^=]*=\\(.*\\)/\\2/' | cut -d'K' -f1 | head -1 > /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqread2.txt";
		execCmd(cmd);
		
		result = getShellOutput("cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqread2.txt");		
		
		//testResultTemp = "exited ioPerformance";
		return result;
		
		
	}
	
	
    public String ioSlowdownSeqwrite() {
		
		//testResultTemp = "entered ioPerformance";		
		String result = null; 
		
		//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Benchmarking...", Toast.LENGTH_LONG).show();
		
		try {
			//testResultTemp = "entered try in ioPerformance";
			Process proc;
			proc = Runtime.getRuntime().exec(appPath + "/" + fileName + "/" + "fio" + " " + appPath + "/" + fileName + "/" + "io_slowdown_seqwrite.fio");
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
			    output.append(buffer, 0, read);
			}
			reader.close();		    
			proc.waitFor();
			
			//save results to /data/data/com.storebenchbeta		  
			BufferedWriter out = new BufferedWriter(new FileWriter(appPath + "/" + fileName + "/" + "results_io_slowdown_seqwrite.txt"));			
			out.write(output.toString());
			out.close();
			
			//testResultTemp = "reach end of try in ioPerformance";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "IOException";
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "Interrupt exception";
			e.printStackTrace();
		}    	
		
	
		String cmd = "cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqwrite.txt | grep aggrb | cut -d'=' -f2,3,7 | sed -e 's/\\([^,]*\\),[^=]*=\\([^,]*\\),[^=]*=\\(.*\\)/\\2/' | cut -d'K' -f1 | tail -1 > /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqwrite2.txt";
		execCmd(cmd);
		
		result = getShellOutput("cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_seqwrite2.txt");		
		
		//testResultTemp = "exited ioPerformance";
		return result;
		
		
	}
	

	public String ioSlowdownRandwrite() {
		
		//testResultTemp = "entered ioPerformance";		
		String result = null; 
		
		//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Benchmarking...", Toast.LENGTH_LONG).show();
		
		try {
			//testResultTemp = "entered try in ioPerformance";
			Process proc;
			proc = Runtime.getRuntime().exec(appPath + "/" + fileName + "/" + "fio" + " " + appPath + "/" + fileName + "/" + "io_slowdown_randwrite.fio");
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
			    output.append(buffer, 0, read);
			}
			reader.close();		    
			proc.waitFor();
			
			//save results to /data/data/com.storebenchbeta		  
			BufferedWriter out = new BufferedWriter(new FileWriter(appPath + "/" + fileName + "/" + "results_io_slowdown_randwrite.txt"));			
			out.write(output.toString());
			out.close();
			
			//testResultTemp = "reach end of try in ioPerformance";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "IOException";
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "Interrupt exception";
			e.printStackTrace();
		}    	
		
		
		
		String cmd = "cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randwrite.txt | grep aggrb | cut -d'=' -f2,3,7 | sed -e 's/\\([^,]*\\),[^=]*=\\([^,]*\\),[^=]*=\\(.*\\)/\\2/' | cut -d'K' -f1 | tail -1 > /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randwrite2.txt";
		execCmd(cmd);
		
		result = getShellOutput("cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randwrite2.txt");		
		
		//testResultTemp = "exited ioPerformance";
		return result;
		
		
	}
	
	public String ioSlowdownRandread() {
		
		//testResultTemp = "entered ioPerformance";		
		String result = null; 
		
		//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Benchmarking...", Toast.LENGTH_LONG).show();
		
		try {
			//testResultTemp = "entered try in ioPerformance";
			Process proc;
			proc = Runtime.getRuntime().exec(appPath + "/" + fileName + "/" + "fio" + " " + appPath + "/" + fileName + "/" + "io_slowdown_randread.fio");
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
			    output.append(buffer, 0, read);
			}
			reader.close();		    
			proc.waitFor();
			
			//save results to /data/data/com.storebenchbeta		  
			BufferedWriter out = new BufferedWriter(new FileWriter(appPath + "/" + fileName + "/" + "results_io_slowdown_randread.txt"));			
			out.write(output.toString());
			out.close();
			
			//testResultTemp = "reach end of try in ioPerformance";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "IOException";
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//testResultTemp = "Interrupt exception";
			e.printStackTrace();
		}    	
		
		
		String cmd = "cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randread.txt | grep aggrb | cut -d'=' -f2,3,7 | sed -e 's/\\([^,]*\\),[^=]*=\\([^,]*\\),[^=]*=\\(.*\\)/\\2/' | cut -d'K' -f1 | head -1 > /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randread2.txt";
		execCmd(cmd);
		
		result = getShellOutput("cat /data/data/com.dnguyen.storebench/myrootdir/results_io_slowdown_randread2.txt");		
		
		//testResultTemp = "exited ioPerformance";
		return result;
		
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


	
	
	
	
	
	

}
