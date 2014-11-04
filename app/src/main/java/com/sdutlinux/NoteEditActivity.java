package com.sdutlinux;

import com.sdutlinux.domain.Note;
import com.sdutlinux.service.NoteService;
import com.sdutlinux.utils.Time;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class NoteEditActivity extends Activity {
	private static final String TAG = "NoteEditActivity";
	
	private EditText et_content;
	private Button bt_save, bt_cancel;
	private boolean isNew = false;
	private int noteid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_note_edit);
		
		et_content = (EditText) this.findViewById(R.id.et_content);
		bt_save = (Button) this.findViewById(R.id.bt_save);
		bt_cancel = (Button) this.findViewById(R.id.bt_cancel);
		
		
		Intent data = getIntent();
		isNew = data.getBooleanExtra("new", false);
		
		if (!isNew) {
			noteid = data.getIntExtra("noteid", 0);
			String content = data.getStringExtra("content");
			et_content.setText(content);			
		}
		
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				NoteEditActivity.this.finish();
			}
		});
		
		bt_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
				setResult(RESULT_OK);
				NoteEditActivity.this.finish();
			}
		});	
	}

	public void save() {
		NoteService service = new NoteService(NoteEditActivity.this);
		if (isNew) {	// 如果是新建备忘就保存
			Note note = new Note(et_content.getText().toString(), Time.getTime());
			service.save(note);
		} else {		// 如果是修改备忘则更新
			Note note = new Note(noteid, et_content.getText().toString(), Time.getTime());
			Log.i(TAG, Time.getTime());
			service.update(note);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_edit, menu);
		return true;
	}
}

