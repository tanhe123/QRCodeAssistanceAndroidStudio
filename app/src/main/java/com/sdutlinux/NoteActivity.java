package com.sdutlinux;

import java.util.List;
import com.sdutlinux.adapter.NoteAdapter;
import com.sdutlinux.domain.Note;
import com.sdutlinux.service.NoteService;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class NoteActivity extends Activity {
	private static final String TAG = "NoteActivity";
	
	private ListView listView;
	private List<Note> noteList;
	private ImageButton bt_new;
	private NoteAdapter noteAdapter;;
	private TextView tv_list_size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_note);
		
		listView = (ListView) this.findViewById(R.id.listView);
		bt_new = (ImageButton) this.findViewById(R.id.bt_new);
		tv_list_size = (TextView) this.findViewById(R.id.tv_list_size);
		
		noteList = getAllNote();
		noteAdapter = new NoteAdapter(this, noteList);
		listView.setAdapter(noteAdapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Note note = noteList.get(position);
				Intent intent = new Intent(NoteActivity.this,
						NoteEditActivity.class);
				
				intent.putExtra("noteid", note.getId());
				intent.putExtra("content", note.getContent());
				intent.putExtra("new", false);
				
				startActivityForResult(intent, 0);
			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				AlertDialog.Builder builder = new Builder(NoteActivity.this);
				builder.setTitle("提示");
				builder.setMessage("确定要删除该备忘?");
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						NoteService service = new NoteService(NoteActivity.this);
						service.delete(noteList.get(position).getId());
						freshListView();
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 什么也不做
					}
				});
				
				builder.create().show();
				
				return true;
			}
		});
		
		bt_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NoteActivity.this,
						NoteEditActivity.class);
				intent.putExtra("new", true);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {	// 如果确定保存了，就更新
			freshListView();
		}
	}
	
	protected void freshListView() {
		noteList = getAllNote();
		noteAdapter.setNoteList(noteList);
		noteAdapter.notifyDataSetChanged();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note, menu);
		return true;
	}

	public List<Note> getAllNote() {
		NoteService service = new NoteService(this);
		List<Note> notes = service.getScrollData();
		tv_list_size.setText(notes.size()+"");
		return notes;
	}
}
