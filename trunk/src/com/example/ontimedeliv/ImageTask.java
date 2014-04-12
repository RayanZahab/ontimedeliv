package com.example.ontimedeliv;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, Void, Bitmap>{
	ImageView bmImage;
	Activity current;

	  public ImageTask(ImageView bmImage, Activity current) {
	      this.bmImage = bmImage;
	      this.current=current;
	  }	  

	  protected void onPostExecute(Bitmap result) {
	      bmImage.setImageBitmap(result);
	  }

	@Override
	protected Bitmap doInBackground(String... url) {
		String urldisplay = url[0];
	      Bitmap mIcon11 = null;
	      try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	    	  try{
	    		  File imgFile = new File(urldisplay);
	    		  mIcon11 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	    			    	    	  
	    	  }catch (Exception e2) {
	    		  try {
	    		      mIcon11 = BitmapFactory.decodeResource(current.getResources(), 
	    		    		  R.drawable.moto);
	    		  }
	    		  catch (Exception e3) {
	    			  Log.d("Error", e3.getMessage());
	    		  }
	          e.printStackTrace();
	    	  }
	      }
	      return mIcon11;
	}
}
