package com.sdutlinux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.sdutlinux.service.SysApplication;
import com.sdutlinux.service.WebService;
import com.sdutlinux.utils.JsonParser;
import com.sdutlinux.utils.SimpleProgressDialog;

public class BasicInfoActivity extends Activity {
//	private ExpandableListView expListView;
	private ListView lv_info;
	
	private TextView nameTxt;
	 
	private SimpleProgressDialog progressDialog;
	private Boolean isAnonymous;
	
    public static final String ANONYMOUS = "anonymous";
	private final static String TAG = "BasicInfoActivity";
//	private static final String CATEGORY = "Catogery";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_info);
		
		SysApplication.getInstance().addActivity(this);
		
		Log.i(TAG, "BasicInfoActivity");
		
//		expListView = (ExpandableListView) this.findViewById(R.id.expListView);
		lv_info = (ListView) this.findViewById(R.id.lv_info);
		nameTxt = (TextView) this.findViewById(R.id.nameTxt);
		
		Intent data = getIntent();
		String id = data.getStringExtra("id");
		String name = data.getStringExtra("name");
		isAnonymous = data.getBooleanExtra(ANONYMOUS, false);
		
		nameTxt.setText("设备名称: " + name);
		
		show(id);
	}

	private void show(String id) {
		Log.i(TAG, "show test");
		new UpdateTask(id).execute(WebService.SERVER_URL);
	}
	
	class UpdateTask extends AsyncTask<String, String, Boolean> {
		private String num;
		
//		private List<List<HashMap<String, String>>> childData;
//		private List<HashMap<String, String>> groupData;
//		private List<HashMap<String, String>> datas;
		private HashMap<String, String> datas;
		
		// 显示结果的顺序
		public String[] normal_keys = new String[] {
				"设备编号", "设备名称", "单价", 
				"总造价", "存放地点", "使（领）用人", 
				"现状", "型号", "规格"};

		public String[] anony_keys = new String[] {
				"设备编号", "设备名称", "型号", "规格"
		};
		
		public UpdateTask(String num) {
			this.num = num;
			progressDialog = new SimpleProgressDialog(BasicInfoActivity.this, "提示", "正在获取机器信息");
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}
		
		/**
		 * 后台查询信息
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			Log.i(TAG, "开始查询");
			
			String url = params[0];
			
			WebService service = new WebService(getApplicationContext());
			
			/* ExpandableList 
			String[] labels = new String[] {"设备基本信息", "设备类型", "使用单位相关", "财务相关", "财务审核相关", "归口审核相关"};
			
			Log.i(TAG, "ha");
			groupData = new ArrayList<HashMap<String, String>>();
			childData = new ArrayList<List<HashMap<String, String>>>();
			
			for (int i = 0; i <= 5; i++) {
				HashMap<String, String> curGroupMap = new HashMap<String, String>();
				groupData.add(curGroupMap);
				curGroupMap.put(CATEGORY, labels[i]);
				
				List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
				
				postParams.add(new BasicNameValuePair("num", num));
				postParams.add(new BasicNameValuePair("flag", i+""));
				
				try {
					JSONObject jsonObj = service.getJson(url, postParams);
					Log.i(TAG, jsonObj.toString());
					
					List<HashMap<String, String>> children = service.jsonToList(jsonObj);
					childData.add(children);
					
				} catch (JSONException e) {
					return false;
				}
			}
			*/
			
//			datas = new ArrayList<HashMap<String,String>>();
			datas = new HashMap<String, String>();
			
			for (int i = 0; i <= 1; i++) {
				List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
				
				postParams.add(new BasicNameValuePair("num", num));
				postParams.add(new BasicNameValuePair("flag", i+""));
				
				try {
					JSONObject jsonObj = service.getJson(url, postParams);
			//		Log.i(TAG, jsonObj.toString());
					
//					List<HashMap<String, String>> data = JsonParser.jsonToList(jsonObj);
					HashMap<String, String> data = JsonParser.jsonToHash(jsonObj);
						
					datas.putAll(data);
				} catch (JSONException e) {
					return false;
				}
			}
			
			
			return true;
		}
		
		protected void onPostExecute(Boolean result) {
//			ExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(BasicInfoActivity.this, groupData,
//					R.layout.category, new String[] {
//							CATEGORY }, new int[] { R.id.category }, childData,
//					R.layout.item, new String[] {
//							"key", "value" }, new int[] { R.id.key, R.id.value });
//			expListView.setAdapter(mAdapter);
			
			Log.i(TAG, datas.toString());
			
			List<HashMap<String, String>> res = transToHash(datas);
			SimpleAdapter adapter = new SimpleAdapter(BasicInfoActivity.this, res, R.layout.basic_info_item, new String[] {"key",  "value"},
					new int[] {R.id.key, R.id.value});
			lv_info.setAdapter(adapter);
			
			progressDialog.dismiss();
		}
		
		// 为了让显示结果顺序排列
		public List<HashMap<String, String>> transToHash(HashMap<String, String> data) {
			List<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
			
			String[] keys;
			
			// 匿名登录和正常登录显示的信息是不同的
			if (isAnonymous) keys = anony_keys;
			else keys = normal_keys;
			
			for (String key : keys) {
				String value = datas.get(key);
				
//				if (value.equals("") || value.equals("*")) continue;
				
				
				HashMap<String, String> r = new HashMap<String, String>();
				r.put("key", key);
				r.put("value", value);
				res.add(r);				
			}
			
			return res;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_info, menu);
		return true;
	}

}
