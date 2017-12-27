package com.thekey;

import java.io.File;

import com.hisign.facelivedetection.R;
import com.hisign.facelivedetection.db.SharedPreferencesUtils;
import com.livedetect.data.ConstantValues;
import com.livedetect.data.ValueUtils;
import com.livedetect.data.XmlParserHelper;
import com.livedetect.data.XmlParserHelper.XmlParserHelperCallback;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.SdUtils;
import com.livedetect.utils.SerializableObjectForData;
import com.livedetect.utils.StringUtils;
import com.livedetect.utils.VersionUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.Log;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主类
 * 
 * @ClassName: MainActivity
 * @Description:TODO
 * @author: lvwenyan
 * @date: Jul 15, 2015 9:35:58 AM
 */
public class MainActivity extends Activity implements Callback {

	private ImageView startImg;
	private Handler mHandler;
	private final int START_LIVEDETECT = 0;
	private final String TAG = MainActivity.class.getSimpleName();
	public SerializableObjectForData  mSerializableObjectForData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Integer.parseInt(VERSION.SDK) >= 14) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		}
		mSerializableObjectForData=new SerializableObjectForData();
		mHandler = new Handler(this);
		FileUtils.init(this);
		
		initView();

	}

	private void initView(){
		setContentView(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_LAYOUT, "htjc_activity_main"));

		TextView versionTV = (TextView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "tv_version"));
		
		versionTV.setText(VersionUtils.getApplicationVersion(this)+"    "+VersionUtils.getFaceDetectSDKVersion());
        
		
		
		
		
		
		
		
		
		
		startImg = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "iv_start"));
		startImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(MainActivity.this,
						LiveDetectActivity.class);
				
				Bundle bundle = new Bundle();

				
				intent.putExtras(bundle);
				
				startActivityForResult(intent,START_LIVEDETECT);
			}
		});

		

		
		
		
		
		
		
		
		
		
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && 0 == event.getRepeatCount()) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstantValues.LIVE_CALLBACK_10:
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		LogUtil.i(TAG, " 109 requestCode = " + requestCode + " resultCode = " + resultCode);
		if (requestCode == START_LIVEDETECT) {
			switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
			case RESULT_OK:
				if (null != intent) {
					Bundle result = intent.getBundleExtra("result");
					if (null != result) {
						//只有失败时返回  失败动作  l代表静止凝视动作  s代表摇头动作，n代表点头动作
						String mMove = result.getString("mMove");
						//只有失败时返回  失败的原因: 0代表默认失败提示，1代表无人脸，2代表多人脸，3代表活检动作失败，4代表错误动作的攻击，5代表超时，6代表图片加密失败，7代表3D检测失败，8代表肤色检测失败
						String mRezion = result.getString("mRezion");
						//活检是否通过 true代表检测通过，false代表检测失败
						boolean isLivePassed = result.getBoolean("check_pass");
						//图片的byte[]的形式，格式为jpg。失败时返回值为null
						byte[] picbyte = result.getByteArray("pic_result");
						if (StringUtils.isNotNull(mMove)) {
							LogUtil.i(TAG, " mMove = " + mMove);
						}
						if (StringUtils.isNotNull(mRezion)) {//10为 初始化 失败 ，11为授权过期
							LogUtil.i(TAG, " mRezion = " + mRezion);
							Toast.makeText(getApplicationContext(),  " mRezion = " + mRezion, Toast.LENGTH_SHORT).show();
						}
						LogUtil.i(TAG, " isLivePassed= " + isLivePassed);
						if (null != picbyte) {
							LogUtil.i(TAG, " picbyte = " + picbyte.length);
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	
}
