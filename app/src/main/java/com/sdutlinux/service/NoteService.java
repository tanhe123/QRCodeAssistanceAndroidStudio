package com.sdutlinux.service;

import java.util.ArrayList;
import java.util.List;

import com.sdutlinux.domain.Note;
import com.sdutlinux.utils.Time;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteService {
	private String table = "notes";
	
	private DatabaseHelper noteDatabaseHelper;
	
	public NoteService(Context context) {
		noteDatabaseHelper = new DatabaseHelper(context);
	}
	
	public void save(Note note) {
		SQLiteDatabase db = noteDatabaseHelper.getWritableDatabase();
		db.execSQL("insert into notes(content, date) values(?, ?)", 
				new Object[] {note.getContent(), note.getDate()});
		db.close();
	}
	
	public void delete(int id) {
		SQLiteDatabase db = noteDatabaseHelper.getWritableDatabase();
		db.execSQL("delete from notes where noteid=?",
				new Object[] {id});
		db.close();
	}
	
	public void update(Note note) {
		SQLiteDatabase db = noteDatabaseHelper.getWritableDatabase();
		db.execSQL("update notes set content=?, date=? where noteid=?",
				new Object[] {note.getContent(), note.getDate(), note.getId()});
		db.close();
	}
	
	public List<Note> getScrollData() {
		List<Note> notes = new ArrayList<Note>();
		
		SQLiteDatabase db = noteDatabaseHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("select * from notes order by date desc", null);
		
		while (cursor.moveToNext()) {
			Note note = readOneFromCursor(cursor);
			notes.add(note);
		}
		
		cursor.close();
		db.close();
		return notes;
	}
	
	public Note find(int id) {
		SQLiteDatabase db = noteDatabaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from notes where noteid=?", 
				new String[] {id+""});
		
		Note note = null;
		if (cursor.moveToFirst()) {
			note = readOneFromCursor(cursor);
			cursor.close();
		}
		db.close();
		return note;
	}
	
	private Note readOneFromCursor(Cursor cursor) {
		String content = cursor.getString(cursor.getColumnIndex("content")); 
		int id = cursor.getInt(cursor.getColumnIndex("noteid"));
		String date = cursor.getString(cursor.getColumnIndex("date"));
		Note note = new Note(id, content, date);
		return note;
	}
}
