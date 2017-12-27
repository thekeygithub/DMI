package com.thekey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.livedetect.data.ConstantValues;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.StringUtils;

public class FailActivity extends Activity {
	private final String TAG = FailActivity.class.getSimpleName();
	private ImageView mReturnImg;
	private ImageView mAgainImg;

	private TextView mRezionTv = null;
	private ImageView returnImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FileUtils.init(this);
		try{
			setContentView(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_LAYOUT, "htjc_activity_fail"));
		}catch(Exception e){
			Log.d(TAG, "onCreate Exception : " , e);
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
			Log.d(TAG, "this.finish()...");
			return;
		}

		initView();
		Intent mIntent = getIntent();
		Bundle result = mIntent.getBundleExtra("result");

		String mMove = result.getString("mMove");
		String mRezion = result.getString("mRezion");
		LogUtil.i(TAG, "35 mRezion = " + mRezion);
		if(StringUtils.isStrEqual(mRezion, "0")){
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_default"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.NO_FACE)){
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_noface"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.MORE_FACE)){
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_moreface"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.NOT_LIVE)){//非活体
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_notlive"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.BAD_MOVEMENT_TYPE)){//互斥
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badmovementtype"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.TIME_OUT)){//超时
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_timeout"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.GET_PGP_FAILED)){
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_pgp_fail"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_3D_FAILED)){//3d
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_3d"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_SKIN_COLOR_FAILED)){//肤色
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badcolor"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_CONTINUITY_COLOR_FAILED)){//连续性
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badcontinuity"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_ABNORMALITY_FAILED)){//
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_abnormality"));
		}else if(StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.GUIDE_TIME_OUT)){//超时
			mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_guide_time_out"));
		}
	}

	private void initView() {
		mAgainImg = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "btn_again"));
		mReturnImg = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "btn_return"));
		mRezionTv = (TextView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "rezion_tv"));
		mAgainImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FailActivity.this, LiveDetectActivity.class);
	             Bundle bundle = new Bundle();
				
			
				startActivity(intent);
				finish();
			}
		});
		mReturnImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		returnImg = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "iv_return"));
		returnImg.setVisibility(View.INVISIBLE);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		FileUtils.setmContext(null);
	}
}
