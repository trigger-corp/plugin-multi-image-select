package com.chute.android.multiimagepicker.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaDAO {
	@SuppressWarnings("unused")
	private static final String TAG = MediaDAO.class.getSimpleName();

	private MediaDAO() {
	}

	public static Cursor getMediaPhotos(Context context) {
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
		//return context.getContentResolver().query(images, projection, query, null, null);
		return context.getContentResolver().query(images, projection, null, null, null);
	}
}
