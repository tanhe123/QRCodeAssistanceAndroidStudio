package com.sdutlinux.test;

import java.util.List;

import com.sdutlinux.domain.Note;
import com.sdutlinux.service.NoteService;
import com.sdutlinux.utils.Time;

import android.test.AndroidTestCase;
import android.util.Log;

public class NoteServiceTest extends AndroidTestCase {
	private static final String TAG = "NoteServiceTest";
	
	public void testSave() {
		NoteService service = new NoteService(getContext());
		service.save(new Note("this is the first note", Time.getTime()));
		service.save(new Note("this is the second note", "2014-04-07 02:21:11"));
		service.save(new Note("this is the third note", "2011-04-07 02:21:11"));
		service.save(new Note("this is the fourth note", "2017-04-07 02:21:11"));
	}
	
	public void testDelete() {
		NoteService service = new NoteService(getContext());
		service.delete(1);
	}
	
	public void testGetScrollData() {
		NoteService service = new NoteService(getContext());
		List<Note> notes = service.getScrollData();
		
		for (Note e : notes) {
			Log.i(TAG, e.toString());
		}
	}
	
	public void testUpdate() {
		NoteService service = new NoteService(getContext());
		Note note = new Note(2, "this is one 111111111", Time.getTime());
		service.update(note);
	}
	
	public void testFind() {
		NoteService service = new NoteService(getContext());
		Note note = service.find(2);
		if (note != null) {
			Log.i(TAG, "find");
		} else {
			Log.i(TAG, "not find");
		}
	}
}
