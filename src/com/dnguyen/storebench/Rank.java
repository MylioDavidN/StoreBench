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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class Rank extends Activity{
	ListView msgList;
	ArrayList<MessageDetails> details;
	AdapterView.AdapterContextMenuInfo info;
	
	String ioPerformance;
	public static GridView grid;
	Context c;
		
		@Override
		public void onBackPressed() {
			Intent myIntent = new Intent(this.getApplicationContext(), Messages.class);
			myIntent.putExtra("ioPerformance", ioPerformance);
			startActivity(myIntent);
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
			
			
			
			grid = (GridView) findViewById(R.id.gridSendResults);
	        // Instance of ImageAdapter Class
	        grid.setAdapter(new ImageAdapterSendResults(this.getApplicationContext()));
			
	        grid.setVisibility(View.VISIBLE);
	        
	        c = this.getApplicationContext();
	        
	        
			
			
			
			// get data from intent
			// ====================
			Intent intent = getIntent();
			ioPerformance = intent.getExtras().getString("ioPerformance");
			String rank = intent.getExtras().getString("rank");
			String[] line = rank.split("\n");
			//String[] fields = new String[line.length];
			//String[][] matrix;
			
			
			
			
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
	
		        		    Intent shareIntent = ShareCompat.IntentBuilder.from(Rank.this)
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
			
			
			
			
			
			
			
			String empty = ";;;;;;;;;;;;;;;;;;;;;;;;";
			String[] fields0 = line[0].split(";");
			String[] fields1 = line[1].split(";");
			String[] fields2 = line[2].split(";");
			
			String[] fields3 = empty.split(";");
			String[] fields4 = empty.split(";");
			String[] fields5 = empty.split(";");
			String[] fields6 = empty.split(";");
			String[] fields7 = empty.split(";");
			String[] fields8 = empty.split(";");
			String[] fields9 = empty.split(";");
						
					
			
			if (line.length == 4) {
				fields3 = line[3].split(";");
			}
			if (line.length == 5) {
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
			}
			if (line.length == 6) {
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
				fields5 = line[5].split(";");
			}
			if (line.length == 7) {
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
				fields5 = line[5].split(";");
				fields6 = line[6].split(";");
			}
			if (line.length == 8) {
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
				fields5 = line[5].split(";");
				fields6 = line[6].split(";");
				fields7 = line[7].split(";");
			}
			if (line.length == 9) {
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
				fields5 = line[5].split(";");
				fields6 = line[6].split(";");
				fields7 = line[7].split(";");
				fields8 = line[8].split(";");
			}
			if ((line.length == 10) || (line.length > 10)){
				fields3 = line[3].split(";");
				fields4 = line[4].split(";");
				fields5 = line[5].split(";");
				fields6 = line[6].split(";");
				fields7 = line[7].split(";");
				fields8 = line[8].split(";");
				fields9 = line[9].split(";");
			}
			
			// show data with listview
			// = ======================
			MessageDetails Detail;
			
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number1);
			Detail.setName(getNiceName(fields0[0]));
			//Detail.setName("Jesus");
			Detail.setSub(fields0[2] + " - " + fields0[3] + " - " + fields0[4] + " - " + fields0[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");			
			Detail.setTime("Score (Total Bandwidth): " + fields0[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number2);
			Detail.setName(getNiceName(fields1[0]));
			Detail.setSub(fields1[2] + " - " + fields1[3] + " - " + fields1[4] + " - " + fields1[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields1[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number3);
			Detail.setName(getNiceName(fields2[0]));
			Detail.setSub(fields2[2] + " - " + fields2[3] + " - " + fields2[4] + " - " + fields2[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields2[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number4);
			Detail.setName(getNiceName(fields3[0]));
			Detail.setSub(fields3[2] + " - " + fields3[3] + " - " + fields3[4] + " - " + fields3[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields3[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number5);
			Detail.setName(getNiceName(fields4[0]));
			Detail.setSub(fields4[2] + " - " + fields4[3] + " - " + fields4[4] + " - " + fields4[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields4[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number6);
			Detail.setName(getNiceName(fields5[0]));
			Detail.setSub(fields5[2] + " - " + fields5[3] + " - " + fields5[4] + " - " + fields5[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields5[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number7);
			Detail.setName(getNiceName(fields6[0]));
			Detail.setSub(fields6[2] + " - " + fields6[3] + " - " + fields6[4] + " - " + fields6[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields6[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number8);
			Detail.setName(getNiceName(fields7[0]));
			Detail.setSub(fields7[2] + " - " + fields7[3] + " - " + fields7[4] + " - " + fields7[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields7[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number9);
			Detail.setName(getNiceName(fields8[0]));
			Detail.setSub(fields8[2] + " - " + fields8[3] + " - " + fields8[4] + " - " + fields8[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields8[24]);
			details.add(Detail);
			
			Detail = new MessageDetails();
			Detail.setIcon(R.drawable.number10);
			Detail.setName(getNiceName(fields9[0]));
			Detail.setSub(fields9[2] + " - " + fields9[3] + " - " + fields9[4] + " - " + fields9[5] + "");
			Detail.setDesc("Sequential Read - Sequential Write - Random Read - Random Write (KB/s)");
			Detail.setTime("Score (Total Bandwidth): " + fields9[24]);
			details.add(Detail);
			
			
			msgList.setAdapter(new CustomAdapter(details , this));

			registerForContextMenu(msgList);
			
			msgList.setOnItemClickListener(new OnItemClickListener() {
				   public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					   System.out.println("Name: "+details.get(position).getName());
					   // String s =(String) ((TextView) v.findViewById(R.id.From)).getText();
					   //Toast.makeText(Messages.this, s, Toast.LENGTH_LONG).show();  
				   }
		   });	
		}
		
		public boolean onOptionsItemSelected(MenuItem item){
			 /*
		    Intent myIntent = new Intent(getApplicationContext(), Messages.class);
		    startActivityForResult(myIntent, 0);
		    return true;
		    */
			
			Intent myIntent = new Intent(this.getApplicationContext(), Messages.class);
			myIntent.putExtra("ioPerformance", ioPerformance);
			startActivity(myIntent);
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

		           MessageDetails msg = _data.get(position);
		           image.setImageResource(msg.icon);
		           fromView.setText(msg.from);
		           subView.setText(msg.sub);
		           descView.setText(msg.desc);
		           timeView.setText(msg.time);		              		
		        
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
		
		
		
		public String getNiceName(String uglyName) {
			String result = uglyName;
			
			if (uglyName.toUpperCase().contains("ZTE BLADE")) {
				result = "ZTE Blade";
			}
			else if (uglyName.toUpperCase().contains("I9505")) {
				result = "Samsung Galaxy S4";
			}
			else if (uglyName.toUpperCase().contains("I9100")) {
				result = "Samsung Galaxy S2";
			}			
			else if (uglyName.toUpperCase().contains("SONY C6606")) {
				result = "Sony Xperia Z";
			}
			else if (uglyName.toUpperCase().contains("MOTOROLA XT910")) {
				result = "Motorola DROID RAZR XT910";
			}
			else if (uglyName.toUpperCase().contains("MOTOROLA XT912")) {
				result = "Motorola DROID RAZR XT912";
			}
			else if (uglyName.toUpperCase().contains("HTC EVO 3D")) {
				result = "HTC EVO 3D";
			}
			else if (uglyName.toUpperCase().contains("LG-D800")) {
				result = "LG G2";
			}
			else if (uglyName.toUpperCase().contains("86VEBC")) {
				result = "Samsung Galaxy Tab";
			}
			else if (uglyName.toUpperCase().contains("GT-N710")) {
				result = "Samsung Galaxy Note 2";
			}
			else if (uglyName.toUpperCase().contains("LG-LS720")) {
				result = "LG Optimus F3";
			}
			else if (uglyName.toUpperCase().contains("I9300")) {
				result = "Samsung Galaxy S3";
			}
			else if (uglyName.toUpperCase().contains("LG-F300L")) {
				result = "LG Vu 3";
			}
			return result;
		}
		
		
		

}
