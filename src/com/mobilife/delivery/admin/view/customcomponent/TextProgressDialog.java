package com.mobilife.delivery.admin.view.customcomponent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.view.activity.NavigationActivity;

public class TextProgressDialog extends Dialog {
	private ImageView iv;
	Activity current;
	String msg;

	public TextProgressDialog(Activity mc, String msg) {
		super(mc, R.style.TransparentProgressDialog);
		this.msg = msg;
		current = mc;
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(msg);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(current);
		layout.setOrientation(LinearLayout.VERTICAL);
		DisplayMetrics dm = new DisplayMetrics();
		mc.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int w = dm.widthPixels;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w / 2,
				w / 2);
		iv = new ImageView(current);
		iv.setImageResource(R.drawable.spinner);
		layout.addView(iv, params);
		addContentView(layout, params);
	}

	@Override
	public void show() {
		super.show();

		RotateAnimation anim = new RotateAnimation(360.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(2000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}

	public void showProg() {
		Handler h;
		Runnable r;
		h = new Handler();
		DeliveryAdminApplication.txtDialog = new TextProgressDialog(current, this.msg);
		r = new Runnable() {
			@Override
			public void run() {
				if (DeliveryAdminApplication.txtDialog.isShowing()) {
					DeliveryAdminApplication.txtDialog.dismiss();
					Intent i = new Intent(current, NavigationActivity.class);

					current.startActivity(i);
				}
			}
		};
		DeliveryAdminApplication.txtDialog.show();
		h.postDelayed(r, 1000);
	}
}
