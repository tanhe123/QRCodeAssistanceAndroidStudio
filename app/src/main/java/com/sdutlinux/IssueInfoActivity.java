package com.sdutlinux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.sdutlinux.BasicInfoActivity.UpdateTask;
import com.sdutlinux.service.WebService;
import com.sdutlinux.utils.JsonParser;
import com.sdutlinux.utils.SimpleProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;

public class IssueInfoActivity extends Activity {
	private ExpandableListView el_issue_info;
	private SimpleProgressDialog progressDialog;
	
	private Button bt_fresh;
	
	private String id;
	private static final String TAG = "IssueInfoActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_info);
		
		Intent data = getIntent();
		id = data.getStringExtra("id");
		String name = data.getStringExtra("name");
		
		el_issue_info = (ExpandableListView) this.findViewById(R.id.el_issue_info);
		el_issue_info.setGroupIndicator(null);
		
		bt_fresh = (Button) this.findViewById(R.id.bt_fresh);
		
		bt_fresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				show(id);
			}
		});
		
		show(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.issue_info, menu);
		return true;
	}
	
	private void show(String id) {
		new UpdateTask(id).execute(WebService.ISSUE_INFO_URL);
	}

	class UpdateTask extends AsyncTask<String, String, JSONObject> {
		private String num;
		
		private List<List<HashMap<String, String>>> childData;
		private List<HashMap<String, String>> groupData;
		
		public UpdateTask(String num) {
			this.num = num;
			progressDialog = new SimpleProgressDialog(IssueInfoActivity.this, "提示", "正在获取问题信息");
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}
		
		/**
		 * 后台查询信息
		 */
		@Override
		protected JSONObject doInBackground(String... params) {

			String url = params[0];
			
			WebService service = new WebService(getApplicationContext());
			
//			String[] labels = new String[] {"设备基本信息", "设备类型", "使用单位相关", "财务相关", "财务审核相关", "归口审核相关"};
			
			List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
			
			postParams.add(new BasicNameValuePair("num", num));
			postParams.add(new BasicNameValuePair("flag", 2+""));	// 2 代表维修记录
				
			JSONObject jsonObject = null;
			try {
				jsonObject = service.getJson(url, postParams);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return jsonObject;
		}
		
		protected void onPostExecute(JSONObject jsonObject) {
			groupData = new ArrayList<HashMap<String, String>>();
			childData = new ArrayList<List<HashMap<String, String>>>();
			
			Iterator<String> iter = jsonObject.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				try {
					JSONObject jo = jsonObject.getJSONObject(key);
					HashMap<String, String> curGroupMap = new HashMap<String, String>();
					groupData.add(curGroupMap);
					curGroupMap.put("malfunction", "[简述]" + jo.getString("malfunction"));
					curGroupMap.put("date", "[维护日期]" + jo.getString("date"));
					
					List<HashMap<String, String>> children = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("solution", "[详细内容]"+jo.getString("solution"));
					children.add(map);
					childData.add(children);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			ExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(IssueInfoActivity.this, groupData,
			R.layout.issue_info_title_item, new String[] {
					"malfunction", "date" }, new int[] { R.id.tv_input_title, R.id.tv_issue_time }, childData,
			R.layout.issue_info_desc_item, new String[] {
					"solution" }, new int[] { R.id.tv_issue_desc });
			el_issue_info.setAdapter(mAdapter);
			
			/*
//			for (int i = 0; i <= 5; i++) {
				HashMap<String, String> curGroupMap = new HashMap<String, String>();
				groupData.add(curGroupMap);
				curGroupMap.put(CATEGORY, labels[i]);

				try {
					JSONObject jsonObj = service.getJson(url, postParams);				
					List<HashMap<String, String>> children = service.jsonToList(jsonObj);
					childData.add(children);
					
				} catch (JSONException e) {
					return false;
				}
//			}
			
			
			datas = new ArrayList<HashMap<String,String>>();
				List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
				
				postParams.add(new BasicNameValuePair("num", num));
				postParams.add(new BasicNameValuePair("flag", 2+""));
				
				try {
					JSONObject jsonObj = service.getJson(url, postParams);
					
					List<HashMap<String, String>> data = JsonParser.jsonToList(jsonObj);
					datas.addAll(data);
					
				} catch (JSONException e) {
					return false;
				}
			}*/
			
//			SimpleAdapter adapter = new SimpleAdapter(IssueInfoActivity.this, datas, R.layout.item, new String[] {"key",  "value"},
//					new int[] {R.id.key, R.id.value});
//			lv_history.setAdapter(adapter);
			
			progressDialog.dismiss();
		}
	}
}
