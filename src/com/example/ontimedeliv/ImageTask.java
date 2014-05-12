package com.example.ontimedeliv;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, Void, Bitmap>{
	ImageView bmImage;
	Activity current;
	Context ctx;
	public boolean isCat = false;



	  public ImageTask(ImageView bmImage, Context ctx) {
	      this.bmImage = bmImage;
	      this.ctx=ctx;
	  }	
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
			  Log.d("Error n", "1");
	      } catch (Exception e) {
	    	  try{
				  if(!isCat){
		    		  File imgFile = new File(urldisplay);
		    		  mIcon11 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	   
				  }else
				  {
					  mIcon11 = BitmapFactory.decodeResource(ctx.getResources(), 
	    		    		  R.drawable.moto);
				  }
	    	  }catch (Exception e2) {
	    		  try {
	    		      mIcon11 = BitmapFactory.decodeResource(current.getResources(), 
	    		    		  R.drawable.moto);
	    		  }
	    		  catch (Exception e3) {
	    			  Log.d("Error", "Error:"+ e3.getMessage());
	    		  }
	    	  }
	      }
	      return mIcon11;
	}
}
