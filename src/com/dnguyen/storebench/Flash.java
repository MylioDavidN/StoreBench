package com.dnguyen.storebench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dnguyen.storebench.Shell;
import com.dnguyen.storebench.Shell.ShellException;
import com.dnguyen.storebench.R;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Flash extends FragmentActivity implements ActionBar.TabListener,ResultsListener,ResultsListenerSDcard,ResultsListenerApps {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	public static View rootView; 
	public static MyViewPager mViewPager;
	public static ActionBar actionBar;
	public static Intent myIntent;
	public static MyAsyncTaskFlash taskFlash;
	public static MyAsyncTaskSDcard taskSDcard;
	public static MyAsyncTaskApps taskApps;
	public static Button buttonTest;	
	public static TextView dummyTextView;
	public static GridView grid;
	public static boolean hasSDcard;
	//public static TextView status;
	//public static ProgressBar bar;
	
	public static String fileName = "myrootdir";
	public static String dBaseName = "file.txt";
	public static String appPath;
	public static String versionName = ""; 
	
	public static Context c;
	
	public static String phoneModel;
	public static String androidVersion = android.os.Build.VERSION.RELEASE;
	
	
	
	@Override
	public void onBackPressed() {
	}
	
	/*
	 * @Override
    public boolean onOptionsItemSelected(Menu item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
            Intent intent = new Intent(this,MyNewActivity.class);
            startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);
		
		if (!isRooted()){
			// not rooted
			new AlertDialog.Builder(this)
		    .setTitle("Not Rooted Device")
		    .setMessage("Please root your device properly, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 		            
		        	finish();
		        }
		     })
		    
		     .show();
		}
		
		if ((!appInstalledOrNot("stericson.busybox")) && (!appInstalledOrNot("stericson.busybox.donate"))){
			// missing BusyBox
			new AlertDialog.Builder(this)
		    .setTitle("Missing BusyBox")
		    .setMessage("Please install BusyBox app first, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 		            
		        	finish();
		        }
		     })
		    
		     .show();
		}
		
		if (!isNetworkAvailable()){
			// Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Internet Connection")
		    .setMessage("Please connect your device to Internet, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    
		     .show();
		}
		
	
		//clearAppCache();
		
		try {
			versionName = getPackageManager()
				    .getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		c = this.getApplicationContext();
		
		// copy scripts, download dbase
		init();
		
		hasSDcard = true;
			
		
		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				
		
		       
		// Create the adapter that will return a fragment for each of the four
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (MyViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new MyViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		try {
		taskFlash = new MyAsyncTaskFlash(this.getApplicationContext());
        /**
         * Set this Activity as the listener
         * on the AsyncTask. The AsyncTask will now
         * have a refence to this Activity and will
         * call onResultsSucceeded() in its
         * onPostExecute method.
         */
		taskFlash.setOnResultsListener(this);		
		}
		catch (Exception e){
			
		}
		
		try {
			taskSDcard = new MyAsyncTaskSDcard(this.getApplicationContext());
	        /**
	         * Set this Activity as the listener
	         * on the AsyncTask. The AsyncTask will now
	         * have a refence to this Activity and will
	         * call onResultsSucceeded() in its
	         * onPostExecute method.
	         */
			taskSDcard.setOnResultsListenerSDcard(this);		
			}
		catch (Exception e){
				
		}
		
		try {
			taskApps = new MyAsyncTaskApps(this.getApplicationContext());
	        /**
	         * Set this Activity as the listener
	         * on the AsyncTask. The AsyncTask will now
	         * have a refence to this Activity and will
	         * call onResultsSucceeded() in its
	         * onPostExecute method.
	         */
			taskApps.setOnResultsListenerApps(this);		
			}
		catch (Exception e){
				
		}
		
		
				
		
	}

	// kill settings  button
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuItem item= menu.findItem(R.id.action_settings);
	    item.setVisible(false);
	    return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given  tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		//mViewPager.setCurrentItem(tab.getPosition());
	}
	
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			
			}
			return null;
		}
	}


	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";		

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			/*final View*/ rootView = inflater.inflate(R.layout.fragment_flash_dummy,
					container, false);
			
			dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			
			buttonTest = (Button) rootView.findViewById(R.id.button1);
			
			final ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
			bar.setVisibility(View.INVISIBLE);
			
			
			grid = (GridView) rootView.findViewById(R.id.gridView1);
			 
	        // Instance of ImageAdapter Class
	        grid.setAdapter(new ImageAdapter(c));
			//grid.setVisibility(View.INVISIBLE);
	        grid.setVisibility(View.INVISIBLE);
			
	        
			int tab = getArguments().getInt(ARG_SECTION_NUMBER);
			
			if (tab == 4) {
				// Credits tab
				
				dummyTextView.setTextColor(Color.WHITE);
				dummyTextView.setText("StoreBench BETA " + versionName + "\n\n" +
						"Requires: Rooted device, installed BusyBox app, Android 3.0 or higher.\n\n" +
						//"David T. Nguyen, William and Mary, USA\n" +
						//"Gang Zhou, William and Mary, USA\n" +
						//"Guoliang Xing, Michigan State U., USA\n\n" +																
						"Learn about cool features that keep us busy at StoreBench.com\n\n");
				dummyTextView.setVisibility(View.VISIBLE);					
				
				addLink(dummyTextView, "StoreBench.com", "http://www.storebench.com");
				addLink(dummyTextView, "BusyBox", "market://details?id=stericson.busybox");
				dummyTextView.setLinkTextColor(Color.WHITE);
						
											
				buttonTest.setText("EMAIL US");
				buttonTest.setTextColor(Color.WHITE);						
				
				mViewPager.setPagingEnabled(true);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				
				
				buttonTest.setOnClickListener(new View.OnClickListener() {				
					//@Override
					public void onClick(View v) {					
						
						Uri uri = Uri.parse("mailto:storebench@gmail.com");
						Intent it = new Intent(Intent.ACTION_SENDTO, uri);
						startActivity(it);
						
					}
				});
			}
			
						
			else if (tab == 1) {
								
				// FLASH tab
				dummyTextView.setTextColor(Color.WHITE);
				dummyTextView.setText("Touch RUN to test internal FLASH storage (read/write performance, etc.). " +
						"The test runs about 20 seconds. Results will be compared with those of " +
						"other devices in our online database.");
				dummyTextView.setVisibility(View.VISIBLE);	
				
							
				buttonTest.setOnClickListener(new View.OnClickListener() {				
					//@Override
					public void onClick(View v) {					
							
							v.setEnabled(false);
							//TextView status = (TextView) rootView.findViewById(R.id.textView1);
							
														
							
							dummyTextView.setTextColor(Color.WHITE);
							dummyTextView.setText("Benchmark in progress..");
							dummyTextView.setVisibility(View.VISIBLE);
							
							//buttonTest.setText("STOP");
							//buttonTest.setTextColor(Color.RED);
							
							
							//ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
							bar.setVisibility(View.VISIBLE);
									
							mViewPager.setPagingEnabled(false);
							//actionBar.hide();
							actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);												
							
							
							new AlertDialog.Builder(getActivity())
							.setTitle("Submit Test Results?")
						    .setMessage("Are you ok with submitting the results of this test to our online database? No user information will " +
						    		"be sent. Everything is anonymized.")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // continue with delete
						        	//finish();
						        	taskFlash.execute();
						        }
						     })
						    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // do nothing
						        	taskFlash.execute();
						        }
						     })
						     .show();
							
							
							
							// call async benchmark process
							// ============================
							//taskFlash.execute();
							
							
							
						
					}
				});
					
				
			}
			
			
			else if (tab == 2) {
				
				if (hasSDcard == false){
				// NO SDcard
					//ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);					
					//bar.setVisibility(View.INVISIBLE);
					
					//TextView status = (TextView) rootView.findViewById(R.id.textView1);
					
					dummyTextView.setTextColor(Color.WHITE);
					dummyTextView.setText("NO SDcard available.");
					dummyTextView.setVisibility(View.VISIBLE);
					
									
					buttonTest.setText("RUN");
					buttonTest.setTextColor(Color.WHITE);											
					buttonTest.setEnabled(false);
					
					bar.setVisibility(View.INVISIBLE);
					
					mViewPager.setPagingEnabled(true);
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					
				} 
				else {
				// has SD card 
					// FLASH tab
					dummyTextView.setTextColor(Color.WHITE);
					dummyTextView.setText("Touch RUN to test external SD CARD (read/write performance, etc.). " +
							"The test runs about 30 seconds. Results will be compared with our online database. " +
							"SD CARD path is detected automatically. ");
					dummyTextView.setVisibility(View.VISIBLE);
					
									
					buttonTest.setOnClickListener(new View.OnClickListener() {				
						//@Override
						public void onClick(View v) {					
							
								v.setEnabled(false);
								//TextView status = (TextView) rootView.findViewById(R.id.textView1);
								
								dummyTextView.setTextColor(Color.WHITE);
								dummyTextView.setText("Benchmark in progress..");
								dummyTextView.setVisibility(View.VISIBLE);
								
								 //buttonTest.setText("STOP");
								//buttonTest.setTextColor(Color.RED);
								
								//ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
								bar.setVisibility(View.VISIBLE);
										
								mViewPager.setPagingEnabled(false);
								//actionBar.hide();
								actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);												
								
								
								new AlertDialog.Builder(getActivity())
								.setTitle("Submit Test Results?")
							    .setMessage("Are you ok with submitting the results to our online database? No user information will " +
							    		"be sent. Everything is anonymized.")
							    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) { 
							            // continue with delete
							        	//finish();
							        	taskSDcard.execute();
							        }
							     })
							    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) { 
							            // do nothing
							        	taskSDcard.execute();
							        }
							     })
							     .show();
								
								// call async benchmark process
								// ============================
								//taskSDcard.execute();
								//String sd = getSDcardDir();
								
						}
					});
					
					
				}	
			}
			else if (tab == 3) {
				
				dummyTextView.setTextColor(Color.WHITE);
				dummyTextView.setText("Touch RUN to test launch and run-time delay of 20 popular apps below. " +
						"The test runs about 5 minutes. When you see the list of apps with measurements, the test is finished." +
						"You will see a crazy screen launching and running apps. " +
						"Sit tight, stay cool, and enjoy the show! All is under control of StoreBench. It is not needed, " +
						"nor suggested to interrupt the process. Even when warning or error dialogs appear. " +
						"All is handled by StoreBench. " +
						"You still can answer phone calls. If you want to close StoreBench, hit the Home button.");
				dummyTextView.setVisibility(View.VISIBLE);
				
				
				grid.setVisibility(View.VISIBLE);
				
								
				buttonTest.setOnClickListener(new View.OnClickListener() {				
					//@Override
					public void onClick(View v) {					
						
							v.setEnabled(false);
							//TextView status = (TextView) rootView.findViewById(R.id.textView1);
							
							dummyTextView.setTextColor(Color.WHITE);
							dummyTextView.setText("Benchmark in progress..");
							dummyTextView.setVisibility(View.VISIBLE);
							
							//buttonTest.setText("STOP");
							//buttonTest.setTextColor(Color.RED);
							
							
							//ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
							bar.setVisibility(View.VISIBLE);
									
							mViewPager.setPagingEnabled(false);
							//actionBar.hide();
							actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);												
							
							
							new AlertDialog.Builder(getActivity())
							.setTitle("Submit Test Results?")
						    .setMessage("Are you ok with submitting the results to our online database? No user information will " +
						    		"be sent. Everything is anonymized.")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // continue with delete
						        	//finish();
						        	taskApps.execute();
						        }
						     })
						    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // do nothing
						        	taskApps.execute();
						        }
						     })
						     .show();
							
							
							
							
					}
				});
			}
				
			return rootView;
		}
	}

	
	
	
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	
	
	@Override
	public void onResultsSucceeded(String result) {
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		 
		if (result == "nonetwork"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Internet Connection")
		    .setMessage("Please connect your device to Internet, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    
		     .show();
		}
		else if (result == "nodbase"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Stable Internet Connection")
		    .setMessage("Please make sure you have a stable Internet connection first, then start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    
		     .show();
		}
		
		else if (result == "flash_doInBackground_error"){
			// Error in Flash doInBackground function
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("Problem Occurred")
		    .setMessage("There was a problem while benchmarking flash, which might have been caused by specific configurations of your device. " +  
		    "We will try to fix this as soon as we get this message. Report this to us?")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 		        	
		        	
		        	try{
		        		phoneModel = getDeviceName();
		        		
            			// email results
            			String uriText =
            			    "mailto:StoreBench@gmail.com" + 
            			    "?subject=" + Uri.encode("flash_doInBackground_issue - pls help!") + 
            			    "&body=" + Uri.encode(phoneModel + "\n" + androidVersion);

            			Uri uri = Uri.parse(uriText);

            			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            			sendIntent.setData(uri);
            			startActivity(Intent.createChooser(sendIntent, "Send email"));
            		}
            		catch (Exception e){	
            			new AlertDialog.Builder(c)
            		    .setTitle("Email Not Sent")
            		    .setMessage("There was a problem while sending the email. Please try again.")
            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            		        public void onClick(DialogInterface dialog, int which) { 
            		            // continue with delete
            		        	finish();
            		        }
            		     })
            		    
            		     .show();
            		}
		        	
		        	finish();
		        	
		        }
		     })
		     
		     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            finish();
		        }
		     })
		     
		     .show();
		    
		}
		
		else {
			
			
			
			
			buttonTest.setText("RUN");
			buttonTest.setTextColor(Color.WHITE);	
			buttonTest.setEnabled(false);
			
			mViewPager.setPagingEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
			
			// create Messages activity
			Intent myIntent = new Intent(this.getApplicationContext(), Messages.class);
			myIntent.putExtra("ioPerformance", result);
			startActivity(myIntent);
			
			
			
			cleanCache();
		}
		
	}

	
	@Override
	public void onResultsSucceededSDcard(String result) {
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
		if (result == "nonetwork"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Internet Connection")
		    .setMessage("Please connect your device to Internet, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })*/
		     .show();
		}
		
		else if (result == "nodbase"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Stable Internet Connection")
		    .setMessage("Please make sure you have a stable Internet connection first, then start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })*/
		     .show();
		}
		
		else if (result == "sdcard_doInBackground_error"){
			// Error in Flash doInBackground function
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("Problem Occurred")
		    .setMessage("There was a problem while benchmarking SD CARD, which might have been caused by specific configurations of your device. " +  
		    "We will try to fix this as soon as we get this message. Report this to us?")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 		        	
		        	
		        	try{
		        		phoneModel = getDeviceName();
		        		
            			// email results
            			String uriText =
            			    "mailto:StoreBench@gmail.com" + 
            			    "?subject=" + Uri.encode("sdcard_doInBackground_issue - pls help!") + 
            			    "&body=" + Uri.encode(phoneModel + "\n" + androidVersion);

            			Uri uri = Uri.parse(uriText);

            			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            			sendIntent.setData(uri);
            			startActivity(Intent.createChooser(sendIntent, "Send email"));
            		}
            		catch (Exception e){	
            			new AlertDialog.Builder(c)
            		    .setTitle("Email Not Sent")
            		    .setMessage("There was a problem while sending the email. Please try again.")
            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            		        public void onClick(DialogInterface dialog, int which) { 
            		            // continue with delete
            		        	finish();
            		        }
            		     })
            		    
            		     .show();
            		}
		        	
		        	finish();
		        	
		        }
		     })
		     
		     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            finish();
		        }
		     })
		     
		     .show();
		    
		}
		
		
		else { 
				if (result == "") {
				// no SD card
				
				hasSDcard = false;			 
				
				buttonTest.setText("RUN");
				buttonTest.setTextColor(Color.WHITE);	
				buttonTest.setEnabled(false);
				
				dummyTextView.setText("NO SDcard available.");
				
				mViewPager.setPagingEnabled(true);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				Toast.makeText(this, "NO SDcard available.", Toast.LENGTH_LONG).show();
	
				// resume
				Intent myIntent = new Intent(this.getApplicationContext(), Flash.class);
				//myIntent.putExtra("ioPerformance", result);
				startActivity(myIntent);
			}
			else {
				
				//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				
				
				buttonTest.setText("RUN");
				buttonTest.setTextColor(Color.WHITE);	
				buttonTest.setEnabled(false);
				
				mViewPager.setPagingEnabled(true);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
				
				//  create Messages activity
				Intent myIntent = new Intent(this.getApplicationContext(), MessagesSDcard.class);
				myIntent.putExtra("ioPerformance", result);
				startActivity(myIntent);
				
				cleanCache();
				
			}
		}
		
		
	}
	
	
	
	@Override
	public void onResultsSucceededApps(String result) {
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
		
		
		if (result == "nonetwork"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Internet Connection")
		    .setMessage("Please connect your device to Internet, and start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })*/
		     .show();
		}
		
		else if (result == "nodbase"){
			// NO Internet connection
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("No Stable Internet Connection")
		    .setMessage("Please make sure you have a stable Internet connection first, then start this app again.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	finish();
		        }
		     })
		    /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })*/
		     .show();
		}
		
		else if (result == "apps_doInBackground_error"){
			// Error in APPS doInBackground function
			// show warning, and exit
			new AlertDialog.Builder(this)
		    .setTitle("Problem Occurred")
		    .setMessage("There was a problem while benchmarking apps, which might have been caused by specific configurations of your device. " +  
		    "We will try to fix this as soon as we get this message. Report this to us?")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 		        	
		        	
		        	try{
		        		phoneModel = getDeviceName();
		        		
            			// email results
            			String uriText =
            			    "mailto:StoreBench@gmail.com" + 
            			    "?subject=" + Uri.encode("apps_doInBackground_issue - pls help!") + 
            			    "&body=" + Uri.encode(phoneModel + "\n" + androidVersion);

            			Uri uri = Uri.parse(uriText);

            			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            			sendIntent.setData(uri);
            			startActivity(Intent.createChooser(sendIntent, "Send email"));
            		}
            		catch (Exception e){	
            			new AlertDialog.Builder(c)
            		    .setTitle("Email Not Sent")
            		    .setMessage("There was a problem while sending the email. Please try again.")
            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            		        public void onClick(DialogInterface dialog, int which) { 
            		            // continue with delete
            		        	finish();
            		        }
            		     })
            		    
            		     .show();
            		}
		        	
		        	finish();
		        	
		        }
		     })
		     
		     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            finish();
		        }
		     })
		     
		     .show();
		    
		}
		
		else {
		
			buttonTest.setText("RUN");
			buttonTest.setTextColor(Color.WHITE);	
			buttonTest.setEnabled(false);
			
			mViewPager.setPagingEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
			
			
			// get my app to foreground
			// ========================
			Intent it = new Intent("android.intent.action.MAIN");
			it.putExtra("apps", result);
			it.setComponent(new ComponentName(this.getPackageName(), MessagesApps.class.getName()));
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.getApplicationContext().startActivity(it);		
			
		}
		//cleanCache();
		
	}
	
	
	
	
	public void init(){
		
		PackageManager m = getPackageManager();
		appPath = getPackageName();
		try {
		    PackageInfo p = m.getPackageInfo(appPath, 0);
		    appPath = p.applicationInfo.dataDir;
		} catch (NameNotFoundException e) {
		    Log.w("yourtag", "Error package name not found.", e);
		}
		
		
		//Toast.makeText(getApplicationContext(), "dataDir:" + appPath, Toast.LENGTH_LONG).show();
		
    	try {
    		
    		//copy scripts  to /data/data/com.dnguyen.storebench
    		File f = new File(appPath + "/" + fileName + "/" + "fio");
    		if (f.isFile() == false) {
    		
	    		Toast.makeText(this, "Copying files...", Toast.LENGTH_LONG).show();
	    		copyFileOrDir(fileName, appPath);
	    		copyFileOrDir(dBaseName, appPath);
	    		
	    		 //String  fioDir = appPath + "/" + fileName + "/fio";
	    		// copy fio to /system/app
	    		//copyFileOrDir(fioDir, "/system/bin");
	    		
	    		Process proc1 = Runtime.getRuntime().exec("chmod 777 " + appPath + "/"+ fileName + "/" + "fio");
	    		proc1.waitFor();    		
	    		
	    		Process proc2 = Runtime.getRuntime().exec("chmod 777 " + appPath + "/" + fileName + "/" + "iogetshortresults");
	    		proc2.waitFor();
    		}
    		
    		//Toast.makeText(BenchmarkListActivity.this, "Cleaning cache...", Toast.LENGTH_LONG).show();
    		
    		cleanCache(); // clear system cache
    		//clearAppCache(); // clear all app caches!
    		
    		// since version 2.4 the dbase is not downloaded, but is copied from the ASSETS
    		//downloadDbase();
    		
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	} catch (InterruptedException e) {
    	    throw new RuntimeException(e);
    	}
    	
    	//downloadDbase();
	    	
	}
	

	private void copyFileOrDir(String path, String appPath) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path, appPath);
            } else {
                String fullPath = appPath + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i], appPath);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

	
	private void copyFile(String filename, String appPath) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = appPath + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
	
	
	/**
	 * Add a link to the TextView which is given.
	 * 
	 * @param textView the fiel d containing the text
	 * @param patternToMatch a regex pattern to put a link around
	 * @param link the link to add
	 */
	public static void addLink(TextView textView, String patternToMatch, final String link) {
	    Linkify.TransformFilter filter = new Linkify.TransformFilter() {
	        @Override 
	        public String transformUrl(Matcher match, String url) {
	            return link;
	        }
	    };
	    Linkify.addLinks(textView, Pattern.compile(patternToMatch), null, null, filter);
	}
	
	
	public void cleanCache(){
		
		String cmd = "echo 3 > /proc/sys/vm/drop_caches";
		//execCmd (cmd);
		
		try {
			Shell.sudo(cmd);
		} catch (ShellException e) {
			e.printStackTrace();
		}
	}
	
	
	public void downloadDbase() {
		
		HashMap<String, String> data = new HashMap<String, String>();
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(data);
		asyncHttpGet.execute("http://smartstorage.us/read.php");
		
	}
	
	
	
	public static void execCmd(String command)
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
	
	
	public static String getShellOutput(String cmd) {
			String output = null;
			
			try {
				output = Shell.sudo(cmd);
				
			} catch (ShellException e) {
				Log.e("shell-example", e.getMessage());
			}

			
			return output;
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
	
	
	
	
	public void clearAppCache(){
		PackageManager  pm = getPackageManager();
		// Get all methods on the PackageManager
		Method[] methods = pm.getClass().getDeclaredMethods();
		for (Method m : methods) {
		    if (m.getName().equals("freeStorageAndNotify")) {
		        // Found the method I want to use
		        try {
		            long desiredFreeStorage = Long.MAX_VALUE; // Request for 8GB of free space
		            m.invoke(pm, desiredFreeStorage , null);
		        } catch (Exception e) {
		            // Method invocation failed. Could be a permission problem
		        }
		        break;
		    }
		}
	}
	
	
	
	
	/**
	   * Checks if the device is rooted.
	   *
	   * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
	   */
	public static boolean isRooted() {

	    // get from build info
	    String buildTags = android.os.Build.TAGS;
	    if (buildTags != null && buildTags.contains("test-keys")) {
	      return true;
	    }

	    // check if /system/app/Superuser.apk is present
	    try {
	      File file = new File("/system/app/Superuser.apk");
	      if (file.exists()) {
	        return true;
	      }
	    } catch (Exception e1) {
	    	return false;
	    }

	    // try executing commands
	    return canExecuteCommand("/system/xbin/which su")
	        || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
	  }

	 // executes a command on the system
	 private static boolean canExecuteCommand(String command) {
	    boolean executedSuccesfully;
	    try {
	      Runtime.getRuntime().exec(command);
	      executedSuccesfully = true;
	    } catch (Exception e) {
	      executedSuccesfully = false;
	    }

	    return executedSuccesfully;
	  }
	
	
	
	 
	 private boolean appInstalledOrNot(String uri){
	        PackageManager pm = getPackageManager();
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
