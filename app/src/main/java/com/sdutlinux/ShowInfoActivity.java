package com.sdutlinux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.sdutlinux.service.SysApplication;
import com.sdutlinux.service.WebService;

public class ShowInfoActivity extends Activity{
	private final static String TAG = "showinfoactivitytest";
	
	private TabHost tabHost;
	private RadioGroup radioGroup;
	
	private static final String BASIC_INFO = "基本信息";
	private static final String ISSUE_INFO = "维护记录";
	private static final String INPUT_ISSUE = "提交记录";
	
	private Intent basicInfoIntent;
	private Intent issueInfoIntent;
	private Intent inputIssueIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_info);
		
		SysApplication.getInstance().addActivity(this);
		
		Log.i(TAG, "showinfoactivity");
		
		radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		LocalActivityManager localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchCreate(savedInstanceState);
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		//如果通过findViewById得到TabHost 一定要调用 TabHost.setup();
		tabHost.setup(localActivityManager);
		
		initIntent();
		
		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId) {
			case R.id.rb_basic_info:
				tabHost.setCurrentTabByTag(BASIC_INFO);
				break;
			case R.id.rb_issue_info:
				tabHost.setCurrentTabByTag(ISSUE_INFO);
				break;
			case R.id.rb_input_issue:
				tabHost.setCurrentTabByTag(INPUT_ISSUE);
				break;
			}
		}
	};

	private void initIntent() {
		basicInfoIntent = new Intent(this, BasicInfoActivity.class);
		issueInfoIntent = new Intent(this, IssueInfoActivity.class);
		inputIssueIntent = new Intent(this, InputIssueActivity.class);
		
		// 信息传递
		Bundle data = getIntent().getExtras();
		
		basicInfoIntent.putExtras(data);
		issueInfoIntent.putExtras(data);
		inputIssueIntent.putExtras(data);
		
		tabHost.addTab(tabHost.newTabSpec(BASIC_INFO).setIndicator(BASIC_INFO).setContent(basicInfoIntent));
		tabHost.addTab(tabHost.newTabSpec(ISSUE_INFO).setIndicator(ISSUE_INFO).setContent(issueInfoIntent));
		tabHost.addTab(tabHost.newTabSpec(INPUT_ISSUE).setIndicator(INPUT_ISSUE).setContent(inputIssueIntent));
	}
}
