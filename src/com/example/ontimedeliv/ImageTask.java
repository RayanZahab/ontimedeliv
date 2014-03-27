package com.example.ontimedeliv;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, Void, Bitmap>{
	ImageView bmImage;

	  public ImageTask(ImageView bmImage) {
	      this.bmImage = bmImage;
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
	    	  InputStream in = new java.net.URL("http://107.170.86.46/uploads/item/photo/10/images.jpg").openStream();
		      mIcon11 = BitmapFactory.decodeStream(in);
	    	  }catch (Exception e2) {
	          Log.e("Error", e2.getMessage());
	          e.printStackTrace();
	    	  }
	      }
	      return mIcon11;
	}
}
