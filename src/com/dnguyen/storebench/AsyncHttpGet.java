package com.dnguyen.storebench;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AsyncHttpGet extends AsyncTask<String, String, String>{
	private HashMap<String, String> mData = null;// post data

    /**
     * constructor
     */
    public AsyncHttpGet(HashMap<String, String> data) {
        mData = data;
    }

    /**
     * background
     */
    @Override
    protected String doInBackground(String... params) {
        byte[] result = null;
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
        try {
            // set up post data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            //nameValuePair.add(new BasicNameValuePair("value", "12345")); 
            
            
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
			
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
            //client.execute(post);
            
            
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
            }
            
            
            
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
        }
        return str;
    }

    /**
     * on getting result
     */
    @Override
    protected void onPostExecute(String result) {
        // something...
    	//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "file.txt: " + result, Toast.LENGTH_LONG).show();
    	
    	// write to local file.txt    	
    	String filename = "file.txt";
    	writeStringToFile(result, filename);
    		    
        
    	//Toast.makeText(BenchmarkDetailFragment.this.getActivity(), "Database downloaded.", Toast.LENGTH_LONG).show();
    }
    
    
    public void writeStringToFile(String str, String filename) {
		
		
    	OutputStream out = null;
    	    	
    	String appPath = "/data/data/com.dnguyen.storebench";    	
    	String newFileName = appPath + "/" + filename;
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
}