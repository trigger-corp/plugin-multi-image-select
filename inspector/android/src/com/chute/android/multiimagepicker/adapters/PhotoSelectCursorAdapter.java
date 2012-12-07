package com.chute.android.multiimagepicker.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.darko.imagedownloader.ImageLoader;

import io.trigger.forge.android.core.ForgeApp;


public class PhotoSelectCursorAdapter extends CursorAdapter implements OnScrollListener {

	private static LayoutInflater inflater = null;
	public HashMap<Integer, String> tick;
	public ImageLoader loader;
	private boolean shouldLoadImages = true;
	private final int dataIndex;

	public PhotoSelectCursorAdapter(Context context, Cursor c) {
		super(context, c);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loader = ImageLoader.getLoader(context);
		tick = new HashMap<Integer, String>();
		dataIndex = c.getColumnIndex(MediaStore.Images.Media.DATA);
	}

	public static class ViewHolder {
		public ImageView image;
		public ImageView tick;
	}

	//@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// Do nothing
	}

	//@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			shouldLoadImages = false;
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			shouldLoadImages = true;
			notifyDataSetChanged();
			break;
		}
	}

	public ArrayList<String> getSelectedFilePath() {
		final ArrayList<String> photos = new ArrayList<String>();
		final Iterator<String> iterator = tick.values().iterator();
		while (iterator.hasNext()) {
			photos.add(iterator.next());
		}
		return photos;
	}

	public boolean hasSelectedItems() {
		return tick.size() > 0;
	}

	public int getSelectedItemsCount() {
		return tick.size();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String path = cursor.getString(dataIndex);
		if (shouldLoadImages) {
			loader.displayImage(Uri.fromFile(new File(path)).toString(), holder.image);
		} else {
			loader.displayImage(null, holder.image);
		}
		if (tick.containsKey(cursor.getPosition())) {
			holder.tick.setVisibility(View.VISIBLE);
			view.setBackgroundColor(context.getResources().getColor(ForgeApp.getResourceId("orange", "color")));
		} else {
			holder.tick.setVisibility(View.GONE);
			view.setBackgroundColor(Color.BLACK);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder;

		View vi = inflater.inflate(ForgeApp.getResourceId("photos_select_adapter", "layout"), null);
		holder = new ViewHolder();
		holder.image = (ImageView) vi.findViewById(ForgeApp.getResourceId("imageViewThumb", "id"));
		holder.tick = (ImageView) vi.findViewById(ForgeApp.getResourceId("imageTick", "id"));
		vi.setTag(holder);
		return vi;
	}

	@Override
	public String getItem(int position) {
		final Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		return cursor.getString(dataIndex);
	}

}
