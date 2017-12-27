package com.thekey;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.livedetect.data.ConstantValues;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.TimeUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SuccessActivity extends Activity {
	private final String TAG = SuccessActivity.class.getSimpleName();
	private ImageView mReturnBtn, success_img;
	private ImageView returnImg;
	private ImageView mAgainImg;
	private  String dirPicSave = FileUtils.getSdcardPath() + "/DCIM/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FileUtils.init(this);
		dirPicSave = FileUtils.getSdcardPath() + "/DCIM/";
		try{
			setContentView(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_LAYOUT, "htjc_activity_success"));
		}catch(Exception e){
			Log.d(TAG, "onCreate Exception : " , e);
			//Toast.makeText(this, "系统资源不足，请重新启动应用", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
			Log.d(TAG, "this.finish()...");
			return;
		}

		LogUtil.i(TAG, "success  1 ");
		initView();

		Intent mIntent = getIntent();
		Bundle result = mIntent.getBundleExtra("result");;
		
		boolean check_pass = result.getBoolean("check_pass");

		if (check_pass) {

			byte[] pic_result = result.getByteArray("pic_result");
			if (pic_result != null) {
				Bitmap bitmap = FileUtils.getBitmapByBytesAndScale(pic_result, 1);
				if(null != bitmap){

					success_img.setImageBitmap(bitmap);

				}
			} else {
				LogUtil.i(TAG, "success pic_result = null !!!");
			}
		}
	
	}
	public  void writeFile(byte[] featrueData, String featrueFile) {
		BufferedOutputStream stream = null;
        File file = null;
        try{
            file = new File(featrueFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(featrueData);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            if (stream != null) {
                try{
                    stream.close();
                }catch(IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
	}
	   public static File getFileFromBytes(byte[] b, String outputFile) {
	        BufferedOutputStream stream = null;
	        File file = null;
	        try {
	            file = new File(outputFile);
	            FileOutputStream fstream = new FileOutputStream(file);
	            stream = new BufferedOutputStream(fstream);
	            stream.write(b);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (stream != null) {
	                try {
	                    stream.close();
	                } catch (IOException e1) {
	                    e1.printStackTrace();
	                }
	            }
	        }
	        return file;
	    }
		public static void writeFile(String featrueData, String featrueFile) {
			BufferedOutputStream stream = null;
	        File file = null;
	        try{
	            file = new File(featrueFile);
	            FileOutputStream fstream = new FileOutputStream(file);
	            stream = new BufferedOutputStream(fstream);
	            stream.write(featrueData.getBytes());
	        }catch(Exception e) {
	            e.printStackTrace();
	        }finally{
	            if (stream != null) {
	                try{
	                    stream.close();
	                }catch(IOException e1) {
	                    e1.printStackTrace();
	                }
	            }
	        }
		}
	private void initView() {
		success_img = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "success_img"));
		mReturnBtn = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "btn_return"));
		mReturnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		mAgainImg = (ImageView) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "btn_again"));
		mAgainImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SuccessActivity.this, LiveDetectActivity.class);
				
			
				startActivity(intent);
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
