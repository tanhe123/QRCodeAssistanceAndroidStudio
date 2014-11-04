package com.sdutlinux.adapter;

import com.sdutlinux.R;

import android.R.raw;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainUIAdapter extends BaseAdapter {

	
	private LayoutInflater inflater;
	private Context context;
	
	public MainUIAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	
	private static final String[] NAMES = new String[] {
		"扫描", /*"历史",*/ "备忘", "注销", "退出"
	};
	
	private static final int[] ICONS = new int[] {
		R.drawable.scan, /*R.drawable.history,*/ R.drawable.note, R.drawable.logout, R.drawable.exit
	};
	
	@Override
	public int getCount() {
		return NAMES.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}


	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//convertView 相当于缓存一样，只要我们判断一下它是不是为null，就可以知道现在这个view有没有绘制过出来
        //如果没有，那么就重新绘制，如果有，那么就可以使用缓存啦，这样就可以大大的节省view绘制的时间了，进行了优化，使ListView更加流畅
		MainViews views;
		View view;
		
		if (convertView == null) {
			views= new MainViews();
			view = inflater.inflate(R.layout.main_item, null);
			views.imageView = (ImageView) view.findViewById(R.id.iv_main_icon);
			views.textView = (TextView) view.findViewById(R.id.tv_main_name);
			
			views.imageView.setImageResource(ICONS[position]);
			views.textView.setText(NAMES[position]);
			
			view.setTag(views);
		} else {
			view = convertView;
			views = (MainViews) convertView.getTag();
			views.imageView = (ImageView) view.findViewById(R.id.iv_main_icon);
			views.textView = (TextView) view.findViewById(R.id.tv_main_name);
            views.imageView.setImageResource(ICONS[position]);
            views.textView.setText(NAMES[position]);
		}
		
		return view;
	}

	private class MainViews {
		ImageView imageView;
		TextView textView;
	}
}
