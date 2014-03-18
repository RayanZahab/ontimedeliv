package com.example.ontimedeliv;

 
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
 
public class SplashScreenActivity extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
        		String lang = settings1.getString("lang", null);
        		Intent i;
        		if(lang==null)
        		{
        			i = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
        		}
        		else
        		{
        			boolean isChecked = settings1.getBoolean("isChecked", false);
        			if(isChecked)
        			{
        				i = new Intent(SplashScreenActivity.this,NavigationActivity.class);
        			}
        			else
        			{
        				i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        			}
        		}
                startActivity(i); 
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}