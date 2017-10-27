package com.zhangjian.punchcard;

import krelve.demo.rob.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	Button mSave;
	EditText mUserName;
	EditText mPassWord;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		handleMaterialStatusBar();
		findViewById(R.id.layout_control_accessibility).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openServiceSetting();
					}
				});
		
		mSave = (Button) findViewById(R.id.bt_save);
		mSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveUserInfo();
			}
		});
		mUserName = (EditText) findViewById(R.id.ev_username);
		mPassWord = (EditText) findViewById(R.id.ev_password);
	}

	protected void saveUserInfo() {
		//SharedPreferences preferences = getSharedPreferences(name, mode)
		
		
	}

	private void openServiceSetting() {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "ÕÒµ½´ò¿¨·þÎñ£¬¿ªÆô¼´¿É", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * ÊÊÅäMIUI³Á½þ×´Ì¬À¸
     */
    @SuppressLint("NewApi")
	private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffE46C62);

    }
}
