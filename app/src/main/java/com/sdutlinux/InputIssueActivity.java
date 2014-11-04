package com.sdutlinux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sdutlinux.service.WebService;
import com.sdutlinux.utils.JsonParser;
import com.sdutlinux.utils.SimpleProgressDialog;

public class InputIssueActivity extends Activity {
	private static final String TAG = "InputIssueActivity";
	
//	private Spinner sp_teachers;
	private EditText et_title, et_desc;
	//private List<String> teachers;
	private Thread initSpinnerThread;
	private Button bt_input, bt_cancel;
	
	private SimpleProgressDialog progressDialog;
	
	private WebService webService;
	private JSONObject jsonObject;
	
	private String id;	// 设备编号
	
	private static final int START = 1;
	private static final int OVER = 2;
	private static final int POST_START = 4;
	private static final int POST_OVER = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_issue);
		
	//	sp_teachers = (Spinner) this.findViewById(R.id.sp_teachers);
		bt_input	= (Button) this.findViewById(R.id.bt_input);
		bt_cancel	= (Button) this.findViewById(R.id.bt_cancel);
		
		et_title	= (EditText) this.findViewById(R.id.et_issue_title);
		et_desc		= (EditText) this.findViewById(R.id.et_issue_desc);
		
		Intent data = getIntent();
		id = data.getStringExtra("id");
		
		webService = new WebService(this);
	//initSpinnerTeachers();

		bt_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new Thread(postIssueRunnable).start();
			}
		});
		
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(InputIssueActivity.this, QRCodeAssistance.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_issue, menu);
		
		return true;
	}
	
	/*
	private void initSpinnerTeachers() {
		initSpinnerThread = new Thread(initSpinnerRunnable);
		initSpinnerThread.start();
	}*/
	/*
	private void initSpinner(List<String>teachers) {
		// 将可选部分与 ArrayAdapter 连接起来
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teachers);
		
		// 设置下拉列表的风格 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// 将 adapter 添加到 spinner 中
	//	sp_teachers.setAdapter(adapter);
		
		// 添加事件到 spinner
		// sp_teachers.setOnItemSelectedListener();
	}
	*/
	private Runnable postIssueRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(START);
			
			// 提交问题记录
			String title = et_title.getText().toString();
			String desc = et_desc.getText().toString();
	//		String teacher = sp_teachers.getSelectedItem().toString();
			//String id_teacher = null;
	/*		try {
				id_teacher = jsonObject.getString(teacher);
			} catch (JSONException e) {
				e.printStackTrace();
			}*/
			
			List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
			
			postParams.add(new BasicNameValuePair("malfunction", title));
			postParams.add(new BasicNameValuePair("solution", desc));
		//	postParams.add(new BasicNameValuePair("head", id_teacher));	// 老师编号
			postParams.add(new BasicNameValuePair("id", id));
			
			// 测试用
			postParams.add(new BasicNameValuePair("solved", "True"));
			// 测试结束
			
			Log.i(TAG,  title + " " + desc + " "  + " " + id);
			
			webService.post(WebService.ISSUE_URL, postParams);		
			
			mHandler.sendEmptyMessage(POST_OVER);
		}
	};
		
	/*
	private Runnable initSpinnerRunnable = new Runnable() {
		@Override
		public void run() {
			// 任务开始前
			mHandler.sendEmptyMessage(START);
			
			try {
				JSONObject jo = webService.getJson(WebService.ISSUE_URL);
				jsonObject = JsonParser.getJsonFromJson(jo, "teacher");
				Log.i(TAG, jsonObject.toString());
				teachers = JsonParser.keysToList(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			mHandler.sendEmptyMessage(OVER);
		}
	};*/
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OVER:
//				initSpinner(teachers);
				// 关闭 progressDialog
				dismissProgressDialog();		
				break;
			case START:
				// 显示 progressDialog
				showProgressDialog();
				break;
			case POST_OVER:
				dismissProgressDialog();
				Toast.makeText(InputIssueActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
				et_title.setText("");
				et_desc.setText("");
				
				break;
			default:
				break;
			}
		}
	};
	
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new SimpleProgressDialog(this, "提示", "正在提交，清稍后");
		}
		
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		progressDialog.dismiss();
	}
}
