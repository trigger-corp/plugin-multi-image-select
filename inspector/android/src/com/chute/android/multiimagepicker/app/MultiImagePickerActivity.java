package com.chute.android.multiimagepicker.app;

import io.trigger.forge.android.core.ForgeApp;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.chute.android.multiimagepicker.adapters.PhotoSelectCursorAdapter;
import com.chute.android.multiimagepicker.dao.MediaDAO;
import com.chute.android.multiimagepicker.intent.ChoosePhotosActivityIntentWrapper;


public class MultiImagePickerActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = MultiImagePickerActivity.class.getSimpleName();
	private GridView grid;
	private PhotoSelectCursorAdapter gridAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(ForgeApp.getResourceId("photos_select", "layout"));

		grid = (GridView) findViewById(ForgeApp.getResourceId("gridView", "id"));
		new LoadCursorTask().execute();

		final Button ok = (Button) findViewById(ForgeApp.getResourceId("buttonOk", "id"));
		ok.setOnClickListener(new OnOkClickListener());
		final Button cancel = (Button) findViewById(ForgeApp.getResourceId("buttonCancel", "id"));
		cancel.setOnClickListener(new OnCancelClickListener());
	}

	private class LoadCursorTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(final Void... arg0) {
			return MediaDAO.getMediaPhotos(getApplicationContext());
		}

		@Override
		protected void onPostExecute(final Cursor result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			if (gridAdapter == null) {
				gridAdapter = new PhotoSelectCursorAdapter(MultiImagePickerActivity.this, result);
				grid.setAdapter(gridAdapter);
				grid.setOnScrollListener(gridAdapter);
				grid.setOnItemClickListener(new OnGridItemClickListener());
			} else {
				gridAdapter.changeCursor(result);
			}
		}
	}

	private final class OnGridItemClickListener implements OnItemClickListener {
		//@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position,
				final long id) {
			if (gridAdapter.tick.containsKey(position)) {
				gridAdapter.tick.remove(position);
			} else {
				gridAdapter.tick.put(position, gridAdapter.getItem(position));
			}
			gridAdapter.notifyDataSetChanged();
		}
	}

	private final class OnCancelClickListener implements OnClickListener {

		//@Override
		public void onClick(final View v) {
			finish();
		}
	}

	private final class OnOkClickListener implements OnClickListener {

		//@Override
		public void onClick(final View v) {
			if (!gridAdapter.hasSelectedItems()) {
				Toast.makeText(getApplicationContext(), "Choose photos", Toast.LENGTH_SHORT).show();
				return;
			}
			final ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(
					new Intent());
			wrapper.setAssetPathList(gridAdapter.getSelectedFilePath());
			setResult(Activity.RESULT_OK, wrapper.getIntent());
			finish();
		}
	}

}