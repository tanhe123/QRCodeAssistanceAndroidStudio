package com.sdutlinux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sdutlinux.adapter.MainUIAdapter;
import com.sdutlinux.service.SysApplication;
import com.sdutlinux.utils.AlertDialogFactory;
import com.zxing.activity.CaptureActivity;

public class QRCodeAssistance extends Activity implements OnItemClickListener{
    private static final String TAG = "QRCodeAssistancetest";

    public static final String ANONYMOUS = "anonymous";
    
    private boolean isAnonymous;
    private GridView gridView;	
    private LinearLayout ll;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置图标
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.main);

		// 设置图标
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
        
        SysApplication.getInstance().addActivity(this);
        
        // 处理匿名登录
        isAnonymous = 
        		getIntent().getBooleanExtra(ANONYMOUS, false);
        if (isAnonymous) {
        	setTitle(getTitle() + " [匿名用户]");
        }
        
        gridView = (GridView) this.findViewById(R.id.gv_main);
        gridView.setAdapter(new MainUIAdapter(this));
        gridView.setOnItemClickListener(this);
       
        // 淡入淡出效果
        ll = (LinearLayout) this.findViewById(R.id.ll_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(1000);
		ll.startAnimation(alphaAnimation);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	switch (position) {
		case 0:			// 扫描
			Intent openCameraIntent = new Intent(QRCodeAssistance.this,CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);		
			break;
/*		case 1:			// 历史
			if (isAnonymous) {
				AlertDialogFactory.createWarningAlertDialog(this, "警告", "请登录").show();
			} else {
				Intent historyIntent = new Intent(QRCodeAssistance.this, HistoryActivity.class);
				startActivity(historyIntent);				
			}
			break;*/
		case 1:			// 备忘
			Intent noteIntent = new Intent(QRCodeAssistance.this, NoteActivity.class);
			startActivity(noteIntent);
			break;
		case 2:			// 注销
			Intent logoutIntent = new Intent(QRCodeAssistance.this, UserLoginActivity.class);
			logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(logoutIntent);
			break;
		case 3: 		// 退出
			SysApplication.getInstance().exit();
			break;
		default:
			break;
		}
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle result = data.getExtras();
			String scanResult = result.getString("result");
		
			// 测试开始
//			String scanResult = "20036370AA;钥匙柜;";
			// 测试开始
																																																																																						
			String[] results = scanResult.split(";");
		
			Bundle bundle = new Bundle();																																																																																																																																																																																																																																																																																																																																															
			bundle.putString("id", results[0]);
			bundle.putString("name", results[1]);
			bundle.putBoolean(ANONYMOUS, isAnonymous);
									
			if (isAnonymous) {		// 如果匿名登录
				Intent intent = new Intent(QRCodeAssistance.this, BasicInfoActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Intent intent = new Intent(QRCodeAssistance.this, ShowInfoActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		} 
	}

	//按下返回键后不应该去LoginActivity，应该使其直接回到桌面，
	//次方法只适用于 2.0 以上版本，
	//低于2.0 使用public boolean onKeyDown(int keyCode, KeyEvent event) 
//	public void onBackPressed() {
//		Intent i = new Intent(Intent.ACTION_MAIN);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		i.addCategory(Intent.CATEGORY_HOME);
//		startActivity(i);
//		super.onBackPressed();
//	}
}
