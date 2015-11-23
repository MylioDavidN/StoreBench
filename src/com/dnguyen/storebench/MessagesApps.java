package com.dnguyen.storebench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.dnguyen.storebench.R;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MessagesApps extends Activity{
	ListView msgList;
	ArrayList<MessageDetails> details;
	AdapterView.AdapterContextMenuInfo info;
	
	String apps;
	String ioPerformance;
	Context c;
	public static GridView grid;
	
		@Override
		public void onBackPressed() {
			Intent myIntent = new Intent(getApplicationContext(), Flash.class);
		    startActivityForResult(myIntent, 0);
		}
			
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			msgList = (ListView) findViewById(R.id.MessageList);
			
			details = new ArrayList<MessageDetails>();
			
			// get data from intent
			// ====================
			Intent intent = getIntent();
			apps = intent.getExtras().getString("apps");
			String[] line = apps.split("\n");
			
			
			
			
			c = this.getApplicationContext();
			
			

			grid = (GridView) findViewById(R.id.gridSendResults);
	        // Instance of ImageAdapter Class
	        grid.setAdapter(new ImageAdapterSendResults(this.getApplicationContext()));
			
	        grid.setVisibility(View.VISIBLE);
	        
			
			/**
	         * On Click event for Single Gridview Item
	         * */
	        
			grid.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	            	/*
	                 // Sending image id to FullScreenActivity
	                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
	                // passing array index
	                i.putExtra("id", position);
	                startActivity(i);
	                */
	            	//Toast.makeText(c, Long.toString(id), Toast.LENGTH_LONG).show();
	            	String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "screen.jpg";
	            	
	            	if (id == 0) {
	            		try{
	            			// email results
	            			String uriText =
	            			    "mailto:youremail@gmail.com" + 
	            			    "?subject=" + Uri.encode("StoreBench Apps Results") + 
	            			    "&body=" + Uri.encode(apps);

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
	            	}
	            	else if (id == 1) {
	            		// share on Google plus
	            		// Launch the Google+ share dialog with attribution to your app.
	            	    try {
		            		takeScreenShot();
		            	
		            		// share it
		            		Uri pictureUri = Uri.fromFile(new File (mPath));
	
		        		    Intent shareIntent = ShareCompat.IntentBuilder.from(MessagesApps.this)
		        		            .setText("Just used StoreBench.com app to test my phone's APP launch and run-time delays!").setType("image/jpeg")
		        		            .setStream(pictureUri).getIntent()
		        		            .setPackage("com.google.android.apps.plus");
		        		    startActivity(shareIntent);
	            	    }
	            	    catch (Exception e){	
	            			new AlertDialog.Builder(c)
	            		    .setTitle("Results Not Shared")
	            		    .setMessage("There was a problem while sharing through G+. Please try again.")
	            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            		        public void onClick(DialogInterface dialog, int which) { 
	            		            // continue with delete
	            		        	finish();
	            		        }
	            		     })
	            		    
	            		     .show();
	            		}
	  
	            	}
	            	
	            	else if (id == 2) {
	            		//  tweet
	            		try {
	            			/*
	            			Intent tweet = new Intent(Intent.ACTION_VIEW);
	            			tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode("Just used StoreBench.com app to test my phone's FLASH storage!")));//where message is your string message
	            			startActivity(tweet);
	            			*/
	            			Intent intent = new Intent(Intent.ACTION_SEND);
	            		    intent.setType("text/plain");
	            		    intent.putExtra(Intent.EXTRA_TEXT, "Just used StoreBench.com app to test my phone's APP launch and run-time delays!");  
	            		    startActivity(Intent.createChooser(intent, "Share with"));
	            		    
	            				            			            		
	            		}
	            		catch (Exception e){	
	            			new AlertDialog.Builder(c)
	            		    .setTitle("Results Not Shared")
	            		    .setMessage("There was a problem while sharing through Twitter. Please try again.")
	            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            		        public void onClick(DialogInterface dialog, int which) { 
	            		            // continue with delete
	            		        	finish();
	            		        }
	            		     })
	            		    
	            		     .show();
	            		}
	            		
	            		
	            	}
	            	
	            	
	            	
	            	
	            	else if (id == 3) {
	            		//  Facebook
	            		try {
	            			
	            			Intent intent = new Intent(Intent.ACTION_SEND);
	            		    intent.setType("text/plain");
	            		    intent.putExtra(Intent.EXTRA_TEXT, "StoreBench.com");  
	            		    startActivity(Intent.createChooser(intent, "Share with"));
	            		    
	            				            			            		
	            		}
	            		catch (Exception e){	
	            			new AlertDialog.Builder(c)
	            		    .setTitle("Results Not Shared")
	            		    .setMessage("There was a problem while sharing through Facebook. Please try again.")
	            		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            		        public void onClick(DialogInterface dialog, int which) { 
	            		            // continue with delete
	            		        	finish();
	            		        }
	            		     })
	            		    
	            		     .show();
	            		}
	            		
	            		
	            	}
	            	
	            	
	            	
	            	
	            	
	            	
	            }
	        });
			
			
			
			
			
			
			
			
			// show data with listview
			// =======================
			MessageDetails Detail;
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.appslogo2);
			Detail.setName("TOTAL RANK");
			Detail.setSub("MY DEVICE");
			Detail.setDesc("Average over all categories.");
			//Detail.setTime("12/12/2012 12:12");
			//Detail.setTime("Rank in brand: " + line[27] + "/" + line [28] + " | Rank among all: " + line[33] + "/" + line [34]);
			Detail.setTime("Rank in brand: " + line[84] + " | Rank among all: " + line[85]);
			Detail.setArrow(R.drawable.arrow4small);
			details.add(Detail);
			
			// APPS
			// ====
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.youtube);
			Detail.setName("YouTube");			
			Detail.setSub(line[2] + " | " + line[3] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[44] + " | Rank among all: " + line[64]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.angry);
			Detail.setName("Angry Birds");
			Detail.setSub(line[4] + " | " + line[5]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[45] + " | Rank among all: " + line[65]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.maps);
			Detail.setName("Maps");
			Detail.setSub(line[6] + " | " + line[7] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[46] + " | Rank among all: " + line[66]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.pandora);
			Detail.setName("Pandora");
			Detail.setSub(line[8] + " | " + line[9]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[47] + " | Rank among all: " + line[67]);
			
				details.add(Detail);
			
			
			
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.gmail);
			Detail.setName("Gmail");
			Detail.setSub(line[10] + " | " + line[11]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[48] + " | Rank among all: " + line[68]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.fb);
			Detail.setName("Facebook");
			Detail.setSub(line[12] + " | " + line[13] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[49] + " | Rank among all: " + line[69]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.twitter);
			Detail.setName("Twitter");
			Detail.setSub(line[14] + " | " + line[15] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[50] + " | Rank among all: " + line[70]);
			
				details.add(Detail);
			
			

			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.temple);
			Detail.setName("Temple Run 2");
			Detail.setSub(line[16] + " | " + line[17]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[51] + " | Rank among all: " + line[71]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.cnn);
			Detail.setName("CNN");
			Detail.setSub(line[18] + " | " + line[19]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[52] + " | Rank among all: " + line[72]);
			
				details.add(Detail);
			
						
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.abcnews);
			Detail.setName("ABC News");
			Detail.setSub(line[20] + " | " + line[21]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[53] + " | Rank among all: " + line[73]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.simpsons);
			Detail.setName("The Simpsons");
			Detail.setSub(line[22] + " | " + line[23]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[54] + " | Rank among all: " + line[74]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.accu);
			Detail.setName("AccuWeather");
			Detail.setSub(line[24] + " | " + line[25]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[55] + " | Rank among all: " + line[75]);
			
				details.add(Detail);
						
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.nightly);
			Detail.setName("Nightly News");
			Detail.setSub(line[26] + " | " + line[27] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[56] + " | Rank among all: " + line[76]);
			
				details.add(Detail);
			
						
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.gta);
			Detail.setName("GTA 3");
			Detail.setSub(line[28] + " | " + line[29]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[57] + " | Rank among all: " + line[77]);
			
				details.add(Detail);
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.nfs);
			Detail.setName("Need for Speed");
			Detail.setSub(line[30] + " | " + line[31] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[58] + " | Rank among all: " + line[78]);
			
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.compass);
			Detail.setName("Smart Compass");
			Detail.setSub(line[32] + " | " + line[33] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[59] + " | Rank among all: " + line[79]);
				
			details.add(Detail);				
				
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.gyro);
			Detail.setName("Gyroscope Log");
			Detail.setSub(line[34] + " | " + line[35] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[60] + " | Rank among all: " + line[80]);
				
			details.add(Detail);	
				
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.accel);
			Detail.setName("Accelerometer Monitor");
			Detail.setSub(line[36] + " | " + line[37] );
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[61] + " | Rank among all: " + line[81]);
				
			details.add(Detail);	
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.proximity);
			Detail.setName("Proximity Sensor Finder");
			Detail.setSub(line[38] + " | " + line[39]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[62] + " | Rank among all: " + line[82]);
				
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.barometer);
			Detail.setName("SyPressure Barometer");
			Detail.setSub(line[40] + " | " + line[41]);
			Detail.setDesc("Launch Delay | Run-time Delay");
			Detail.setTime("Rank in brand: " + line[63] + " | Rank among all: " + line[83]);
				
			details.add(Detail);
			
			
			msgList.setAdapter(new CustomAdapter(details , this));

			registerForContextMenu(msgList);
			
			
			
			// get phones for ranking
			// ======================
			String rankStr = line[86];
			
			for (int i = 87; i < line.length; i++){
				String help = rankStr + "\n" + line[i];
				rankStr = help;
			}
						
			final String rank = rankStr;
			
			
			msgList.setOnItemClickListener(new OnItemClickListener() {
				   public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					   System.out.println("Name: "+details.get(position).getName());
					   String s =(String) ((TextView) v.findViewById(R.id.From)).getText();
					   //Toast.makeText(MessagesApps.this, s, Toast.LENGTH_LONG).show();  
					   
					   // show chart
					   // ==========
					   if (s == "TOTAL RANK") {
						   // create Rank activity
						   Intent myIntent = new Intent(MessagesApps.this, RankApps.class);
						   myIntent.putExtra("rank", rank);
						   myIntent.putExtra("apps", apps);
						   startActivity(myIntent);
					   }
					   
				   }
		   });	
		}
		
		public boolean onOptionsItemSelected(MenuItem item){
		    Intent myIntent = new Intent(getApplicationContext(), Flash.class);
		    startActivityForResult(myIntent, 0);
		    return true;
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			// TODO Auto-gener ated method stub
			super.onCreateContextMenu(menu, v, menuInfo);		
					
				info = (AdapterContextMenuInfo) menuInfo;
				System.out.println("Reached");
				 
				int id = (int) msgList.getAdapter().getItemId(info.position);			
				System.out.println("id "+msgList.getAdapter().getItem(id));
										
				System.out.println("Msg"+details.get(info.position).getName());
				
				/*
				menu.setHeaderTitle(details.get(info.position).getName());		
				menu.add(Menu.NONE, v.getId(), 0, "Reply");
				menu.add(Menu.NONE, v.getId(), 0, "Reply All");
				menu.add(Menu.NONE, v.getId(), 0, "Forward");
				*/
				System.out.println("ID "+info.id);
	        	System.out.println("Pos "+info.position);
	        	System.out.println("Info "+info.toString());
		}
		
		@Override  
	    public boolean onContextItemSelected(MenuItem item) {
	        if (item.getTitle() == "Reply") {
	        	System.out.println("Id "+info.id);
	        	System.out.println("Pos "+info.position);
	        	System.out.println("info "+info.toString());
	        	}  
	        else if (item.getTitle() == "Reply All") {
	        	System.out.println("Id "+info.id);
	        	System.out.println("Pos "+info.position);
	        	System.out.println("info "+info.toString());
	        	}  
	        else if (item.getTitle() == "Reply All") {
	        	System.out.println("Id "+info.id);
	        	System.out.println("Pos "+info.position);
	        	System.out.println("info "+info.toString());
	        }
	        else 	{
	        	return false;
	        	}  
	    return true;  
	    }
		
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();			
	}
		
		public class CustomAdapter extends BaseAdapter {

		    private ArrayList<MessageDetails> _data;
		    Context _c;
		    
		    CustomAdapter (ArrayList<MessageDetails> data, Context c){
		        _data = data;
		        _c = c;
		    }
		   
		    public int getCount() {
		        // TODO Auto-generated method stub
		        return _data.size();
		    }
		    
		    public Object getItem(int position) {
		        // TODO Auto-generated method stub
		        return _data.get(position);
		    }
		
		    public long getItemId(int position) {
		        // TODO Auto-generated method stub
		        return position;
		    }
		   
		    public View getView(final int position, View convertView, ViewGroup parent) {
		        // TODO Auto-generated method stub
		         View v = convertView;
		         if (v == null) 
		         {
		            LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            v = vi.inflate(R.layout.list_item_message, null);
		         }

		           ImageView image = (ImageView) v.findViewById(R.id.icon);
		           TextView fromView = (TextView)v.findViewById(R.id.From);
		           TextView subView = (TextView)v.findViewById(R.id.subject);
		           TextView descView = (TextView)v.findViewById(R.id.description);
		           TextView timeView = (TextView)v.findViewById(R.id.time);
		           ImageView image2 = (ImageView) v.findViewById(R.id.arrow);

		           MessageDetails msg = _data.get(position);
		           image.setImageResource(msg.icon);
		           fromView.setText(msg.from);
		           subView.setText(msg.sub);
		           descView.setText(msg.desc);
		           timeView.setText(msg.time);		              		
		           image2.setImageResource(msg.arrow);
		           
		           image.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method  stub
					AlertDialog.Builder adb=new AlertDialog.Builder(MessagesApps.this);
			        
			        final int selectedid = position;
			        final String itemname= (String) _data.get(position).getName();
			        
			        adb.setMessage("Find " + itemname + " on Google Play?");
			        adb.setNegativeButton("Cancel", null);
			        
			        
			        adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int which) {
			        				
			        		System.out.println("Select " + selectedid);
			        		System.out.println("Project " + itemname);
			        		Intent goToMarket = null;
			        		
			        		if (itemname == "YouTube") {
			                	    goToMarket = new Intent(Intent.ACTION_VIEW)
					        	    .setData(Uri.parse("market://details?id=com.google.android.youtube"));			                		
			        		}
			        		if (itemname == "Angry Birds") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.rovio.angrybirds"));			                		
			        		}      		
			        		if (itemname == "Maps") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.google.android.apps.maps"));			                		
			        		}
			        		if (itemname == "Pandora") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.pandora.android"));			                		
			        		}
			        		if (itemname == "Gmail") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.google.android.gm"));			                		
			        		}
			        		if (itemname == "Facebook") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.facebook.katana"));			                		
		        		}
			        		if (itemname == "Twitter") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.twitter.android"));			                		
		        		}
			        		if (itemname == "Temple Run 2") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.imangi.templerun2"));			                		
		        		}
			        		if (itemname == "CNN") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.cnn.mobile.android.phone"));			                		
		        		}
			        		if (itemname == "ABC News") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.abc.abcnews"));			                		
		        		}
			        	
			        		
			        		
			        		if (itemname == "The Simpsons") {
		                	    goToMarket = new Intent(Intent.ACTION_VIEW)
				        	    .setData(Uri.parse("market://details?id=com.ea.game.simpsons4_na"));			                		
		        		}
		        		if (itemname == "AccuWeather") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.accuweather.android"));			                		
		        		}      		
		        		if (itemname == "Nightly News") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.msnbc.nightlynews"));			                		
		        		}
		        		if (itemname == "GTA 3") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.rockstar.gta3"));			                		
		        		}
		        		if (itemname == "Need for Speed") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.ea.games.nfs13_na"));			                		
		        		}
		        		if (itemname == "Smart Compass") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=kr.sira.compass"));			                		
	        		}
		        		
		        		
		        		
		        		
		        		if (itemname == "Gyroscope Log") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=pl.submachine.gyro"));			                		
	        		}
		        		if (itemname == "Accelerometer Monitor") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.lul.accelerometer"));			                		
	        		}
		        		if (itemname == "Proximity Sensor Finder") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=com.tigermonster.proxfinder"));			                		
	        		}
		        		if (itemname == "SyPressure Barometer") {
	                	    goToMarket = new Intent(Intent.ACTION_VIEW)
			        	    .setData(Uri.parse("market://details?id=sy.android.sypressure"));			                		
	        		}
			        		
			        		
			        		startActivity(goToMarket);
			        		
			        		/*
			        		Bundle b = new Bundle();
			    			b.putString("project", itemname);
			    			Intent createTask = new Intent ("com.loginworks.tasktrek.CREATETASK");
			    			createTask.putExtras(b);
			    			startActivity(createTask);
			    			*/    	
			        		}});
			        
			        if (itemname != "TOTAL RANK"){			       			        
			        	adb.show();      
			        }
				}    						
				});
		        	
		        return v; 
		}
		}

		public class MessageDetails {
		    int icon ;
		    String from;
		    String sub;
		    String desc;
		    String time;
		    int arrow;

		    public String getName() {
		        return from;
		    }

		    public void setName(String from) {
		        this.from = from;
		    }

		    public String getSub() {
		        return sub;
		    }

		    public void setSub(String sub) {
		        this.sub = sub;
		    }
		    
		    public int getIcon() {
		        return icon;
		    }

		    public void setIcon(int icon) {
		        this.icon = icon;
		    }
		    
		    public String getTime() {
		        return time;
		    }

		    public void setTime(String time) {
		        this.time = time;
		    }
		    
		    public String getDesc() {
		        return desc;
		    }

		    public void setDesc(String desc) {
		        this.desc = desc;
		    }
		    
		    public int getArrow() {
		        return arrow;
		    }

		    public void setArrow(int arrow) {
		        this.arrow = arrow;
		    }
		}
		
		
		public void takeScreenShot(){
			
			// image naming and path  to include sd card  appending name you choose for file
			String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "screen.jpg";   

			// create bitmap screen capture
			Bitmap bitmap;
			View v1 = getWindow().getDecorView().findViewById(android.R.id.content);
			v1.setDrawingCacheEnabled(true);
			bitmap = Bitmap.createBitmap(v1.getDrawingCache());
			v1.setDrawingCacheEnabled(false);

			OutputStream fout = null;
			File imageFile = new File(mPath);

			try {
			    fout = new FileOutputStream(imageFile);
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
			    fout.flush();
			    fout.close();

			} catch (FileNotFoundException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		}
		
		
		
		

}
