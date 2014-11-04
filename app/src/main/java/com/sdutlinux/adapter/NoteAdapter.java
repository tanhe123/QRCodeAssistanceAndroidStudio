package com.sdutlinux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sdutlinux.R;
import com.sdutlinux.domain.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Note> noteList;
	
	public NoteAdapter(Context context, List<Note> notes) {
		this.context = context;
		noteList = notes;
		inflater = LayoutInflater.from(context);
	}

	
	public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
	}

	@Override
	public int getCount() {
		return noteList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return noteList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView itemView;
		
		if (convertView == null) {
			itemView = new ItemView();
			convertView = inflater.inflate(R.layout.note_item, null);
			itemView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			itemView.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			
			convertView.setTag(itemView);
		} else {
			itemView = (ItemView) convertView.getTag();
		}
		
		Note note = noteList.get(position);
		
		itemView.tv_title.setText(note.getContent());
		itemView.tv_date.setText(note.getDate());
		
		return convertView;
	}
	
	class ItemView {
		public TextView tv_title;
		public TextView tv_date;
	}
}
