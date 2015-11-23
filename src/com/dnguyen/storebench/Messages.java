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
import android.view.ContextMenu.ContextMenuInfo;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class Messages extends Activity {
	ListView msgList;
	ArrayList<MessageDetails> details;
	AdapterView.AdapterContextMenuInfo info;
	
	String ioPerformance;
	public static GridView grid;
	Context c;
		
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
			
			
			grid = (GridView) findViewById(R.id.gridSendResults);
	        // Instance of ImageAdapter Class
	        grid.setAdapter(new ImageAdapterSendResults(this.getApplicationContext()));
			
	        grid.setVisibility(View.VISIBLE);
	        
	        c = this.getApplicationContext();
	        
	        
			
			
			details = new ArrayList<MessageDetails>();
			
			// get data from intent
			// ====================
			Intent intent = getIntent();
			ioPerformance = intent.getExtras().getString("ioPerformance");
			String[] line = ioPerformance.split("\n");
			
			
			
			
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
	            			    "?subject=" + Uri.encode("StoreBench Flash Results") + 
	            			    "&body=" + Uri.encode(ioPerformance);

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
	
		        		    Intent shareIntent = ShareCompat.IntentBuilder.from(Messages.this)
		        		            .setText("Just used StoreBench.com app to test my phone's FLASH storage!").setType("image/jpeg")
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
	            		    intent.putExtra(Intent.EXTRA_TEXT, "Just used StoreBench.com app to test my phone's FLASH storage!");  
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
			Detail.setIcon(R.drawable.disk);
			Detail.setName("TOTAL RANK");
			Detail.setSub("MY DEVICE");
			Detail.setDesc("Average over all categories.");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[27] + "/" + line [28] + " | Rank among all: " + line[33] + "/" + line [34]);
			Detail.setArrow(R.drawable.arrow4small);
			details.add(Detail);
			
			// Bandwidth
			// =========
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("BANDWIDTH");
			Detail.setSub(line[2] + " KB/s");
			Detail.setDesc("Sequential Read");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[23] + "/" + line [28] + " | Rank among all: " + line[29] + "/" + line [34]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("BANDWIDTH");
			Detail.setSub(line[3] + " KB/s");
			Detail.setDesc("Sequential Write");
			Detail.setTime("Rank in brand: " + line[24] + "/" + line [28] + " | Rank among all: " + line[30] + "/" + line [34]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("BANDWIDTH");
			Detail.setSub(line[4] + " KB/s");
			Detail.setDesc("Random Read");
			Detail.setTime("Rank in brand: " + line[25] + "/" + line [28] + " | Rank among all: " + line[31] + "/" + line [34]);
			details.add(Detail);		
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("BANDWIDTH");
			Detail.setSub(line[5] + " KB/s");
			Detail.setDesc("Random Write");
			Detail.setTime("Rank in brand: " + line[26] + "/" + line [28] + " | Rank among all: " + line[32] + "/" + line [34]);
			details.add(Detail);	
			
			// Single response time
			// ====================
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("RESPONSE TIME");
			Detail.setSub(line[15] + " ms");
			Detail.setDesc("Sequential Read");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[23] + "/" + line [28] + " | Rank among all: " + line[29] + "/" + line [34]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("RESPONSE TIME");
			Detail.setSub(line[16] + " ms");
			Detail.setDesc("Sequential Write");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[24] + "/" + line [28] + " | Rank among all: " + line[30] + "/" + line [34]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("RESPONSE TIME");
			Detail.setSub(line[17] + " ms");
			Detail.setDesc("Random Read");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[25] + "/" + line [28] + " | Rank among all: " + line[31] + "/" + line [34]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("RESPONSE TIME");
			Detail.setSub(line[18] + " ms");
			Detail.setDesc("Random Write");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("Rank in brand: " + line[26] + "/" + line [28] + " | Rank among all: " + line[32] + "/" + line [34]);
			details.add(Detail);
			
			// Storage Info
			// ============
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("FLASH");
			Detail.setSub(line[9] + "B - " + line[10] + "B - " + line[11] + "KB");
			Detail.setDesc("Total Capacity - Available Capacity - Block Size");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			/*
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("FLASH");
			Detail.setSub(line[10]);
			Detail.setDesc("Available Capacity (/data)");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("FLASH");
			Detail.setSub(line[11] + " KB");
			Detail.setDesc("Block Size");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			*/
			
			
			// iowait
			// ======
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("IOWAIT");
			Detail.setSub(line[21] + " %");
			Detail.setDesc("Percentage of time that CPUs were idle during " +
					"which system had outstanding disk I/O requests.");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			

			// Info on Processes
			// =================
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("PROCESSES");
			Detail.setSub(line[7] + " - " + line[8]);
			Detail.setDesc("Number of all processes - Number of user processes");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			/*
			// Info on Processes
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.disk);
			Detail.setName("PROCESSES");
			Detail.setSub(line[8]);
			Detail.setDesc("Number of user processes.");
			//Detail.setTime("12/12/2012 12:12");
			Detail.setTime("");
			details.add(Detail);
			 */
			
			msgList.setAdapter(new CustomAdapter(details , this));

			registerForContextMenu(msgList);
			
			// get phones for ranking
			// ======================
			String rankStr = line[35];
			
			for (int i = 36; i < line.length; i++){
				String help = rankStr + "\n" + line[i];
				rankStr = help;
			}
						
			final String rank = rankStr;
			
			msgList.setOnItemClickListener(new OnItemClickListener() {
				   public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					   System.out.println("Name: "+details.get(position).getName());
					   String s =(String) ((TextView) v.findViewById(R.id.From)).getText();
					   
					   //Toast.makeText(Messages.this, s, Toast.LENGTH_LONG).show();
					   
					   if (s == "TOTAL RANK") {
						   // create Rank activity
						   Intent myIntent = new Intent(Messages.this, Rank.class);
						   myIntent.putExtra("rank", rank);
						   myIntent.putExtra("ioPerformance", ioPerformance);
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
		           /*
		           image.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder adb=new AlertDialog.Builder(Messages.this);
			        adb.setMessage("Add To Contacts?");
			        adb.setNegativeButton("Cancel", null);
			        final int selectedid = position;
			        final String itemname= (String) _data.get(position).getName();

			        adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int which) {
			        				
			        		System.out.println("Select " + selectedid);
			        		System.out.println("Project " + itemname);
			        		
			        		Bundle b = new Bundle();
			    			b.putString("project", itemname);
			    			Intent createTask = new Intent ("com.loginworks.tasktrek.CREATETASK");
			    			createTask.putExtras(b);
			    			startActivity(createTask);    	
			        		}});
			        
			        adb.show();      
				}    						
			});
		        */
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
