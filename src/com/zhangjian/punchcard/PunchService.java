package com.zhangjian.punchcard;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class PunchService extends AccessibilityService {
	private WakeLock mWakelock;
	private boolean mGloableCtrl = false;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		int eventType = event.getEventType();
		switch (eventType) {
		case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
			System.out.println("TYPE_NOTIFICATION_STATE_CHANGED");
			List<CharSequence> texts = event.getText();
			if (!texts.isEmpty()) {
				for (CharSequence text : texts) {
					String content = text.toString();
					if (content.contains("打卡")) {
						if(!mGloableCtrl){
							mGloableCtrl = true;
							//WakeUp();
							wakeUpAndUnlock();
							handle();
						}
					}
				}
			}
			break;
		case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			System.out.println("TYPE_WINDOW_STATE_CHANGED");
			punchCard(event);
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	@SuppressLint("NewApi") 
	private void punchCard(AccessibilityEvent event) {
		System.out.println(event.getClassName());
		
		if(event.getClassName().equals("com.tencent.mm")){
			WeChatLogin();
			if(true){
				punchCardOk();
			}
			return;
		}
		
		final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo == null) {
			System.out.println("rootWindow为空");
			return;
		}
		
		if(event.getClassName().equals("com.huawei.hrandroidframe.app.login.LoginWithMjetActivity")){
	        login(nodeInfo);		
		}
		//继续注册
		continueRegister(nodeInfo);
		//默认情况下不升级应用
		notUpdate(nodeInfo);
		//适配最新版本
		List<AccessibilityNodeInfo> list = null;
		list = nodeInfo
				.findAccessibilityNodeInfosByViewId("com.huawei.hrandroidframe:id/punch_process_start");
		if (null == list || (list.size() == 0)) {
			System.out.println("list error!");
			return;
		}
		
		AccessibilityNodeInfo nodeList = list.get(0);
		if (null == nodeList) {
			return;
		}
		
		if ((null != nodeList) && nodeList.isClickable()) {
			System.out.println("find punchCard!");
			nodeList.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			System.out.println("punchCard!");
			Handler handle = new Handler();
			handle.postDelayed(new Runnable() {
				@Override
				public void run() {
					onBackPressed();						
				}
			}, 10000);
			/*
	        new AsyncTask<String, String, String>() {
	        	String result;
				@Override
				protected String doInBackground(String... arg0) {
					while(true){
						result = getTextById(nodeInfo, "com.huawei.hrandroidframe:id/punch_notice");
						if((null != result) && (result.equals("打卡成功") || result.equals("打卡失败"))){
							System.out.println("punchCard AsyncTask " + result);
							break;
						}
					}
					return result;
				}
				@Override
				protected void onPostExecute(String result) {
					onBackPressed();	
				}
			}.execute("Hello");
			*/
		}
	}
	
	private void punchCardOk() {
		
	}

	private void WeChatLogin() {
		
	}

	private void login(AccessibilityNodeInfo nodeInfo) {
		setEditTextById(nodeInfo,"com.huawei.hrandroidframe:id/username","z00353083");
		setEditTextById(nodeInfo,"com.huawei.hrandroidframe:id/password","zj@*963.");
		clickById(nodeInfo,"com.huawei.hrandroidframe:id/login_submit");
		return;
	}
	
	private void continueRegister(AccessibilityNodeInfo nodeInfo){
		clickById(nodeInfo,"com.huawei.hrandroidframe:id/btn_dialogbox1");
		return;
	}
	
	private void notUpdate(AccessibilityNodeInfo nodeInfo){
		clickById(nodeInfo,"com.huawei.hrandroidframe:id/btn_negative");
		return;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private String getTextById(AccessibilityNodeInfo nodeInfo,String textViewId){
		List<AccessibilityNodeInfo> list = null;
		list = nodeInfo
				.findAccessibilityNodeInfosByViewId(textViewId);
		if (null == list || (list.size() == 0)) {
			System.out.println("not founded textView!");
			return null;
		}
		
		AccessibilityNodeInfo textView = list.get(0);
		if (null == textView) {
			System.out.println("textView is null!");
			return null;
		}
		
		CharSequence text = textView.getText();
		
		return text.toString();
		
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private void clickById(AccessibilityNodeInfo nodeInfo,String clickId){
		List<AccessibilityNodeInfo> list = null;
		list = nodeInfo
				.findAccessibilityNodeInfosByViewId(clickId);
		if (null == list || (list.size() == 0)) {
			System.out.println("not founded button!");
			return;
		}
		
		AccessibilityNodeInfo clickView = list.get(0);
		if (null == clickView) {
			System.out.println("clickView is null!");
			return;
		}
		
		clickView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
		
		return;
		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private void setEditTextById(AccessibilityNodeInfo nodeInfo,String textId,String setString){
		List<AccessibilityNodeInfo> list = null;
		list = nodeInfo
				.findAccessibilityNodeInfosByViewId(textId);
		if (null == list || (list.size() == 0)) {
			System.out.println("not founded button!");
			return;
		}
		
		AccessibilityNodeInfo nodeToInput = list.get(0);
		if (null == nodeToInput) {
			System.out.println("clickView is null!");
			return;
		}
		
        if ("android.widget.EditText".equals(nodeToInput.getClassName())) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, setString);
            nodeToInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
        
		return;
		
	}

	private void onBackPressed() {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);
		mGloableCtrl = false;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void handle() {
		Intent intent = new Intent();
		PackageManager packageManager = getPackageManager();
		intent = packageManager
				.getLaunchIntentForPackage("com.huawei.hrandroidframe");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	@SuppressWarnings("deprecation")
	public void WakeUp() {

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
		mWakelock.release();

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
		keyguardLock.disableKeyguard();

		System.out.println("wake up --------------- >");

	}
	
	
	public void wakeUpAndUnlock(){  
		  //屏锁管理器  
		  KeyguardManager km= (KeyguardManager) getSystemService(KEYGUARD_SERVICE);  
		  KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");  
		  //解锁  
		  kl.disableKeyguard();  
		  //获取电源管理器对象  
		  PowerManager pm=(PowerManager)getSystemService(POWER_SERVICE);  
		  //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
		  PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");  
		  //点亮屏幕  
		  wl.acquire();  
		  //释放  
		  wl.release();  
	} 

	@Override
	public void onInterrupt() {

	}

}
