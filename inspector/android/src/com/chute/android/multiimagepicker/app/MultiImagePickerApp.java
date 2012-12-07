package com.chute.android.multiimagepicker.app;

import io.trigger.forge.android.core.ForgeApp;
import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.darko.imagedownloader.ImageLoader;

public class MultiImagePickerApp extends Application {

	@SuppressWarnings("unused")
	private static final String TAG = MultiImagePickerApp.class.getSimpleName();

	private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context, ForgeApp.getResourceId("placeholder_image_small", "drawable"));
		imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
						.getDisplayMetrics()));
		return imageLoader;
	}

	private ImageLoader mImageLoader = null;

	@Override
	public void onCreate() {
		super.onCreate();
		//mImageLoader = createImageLoader(this);
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			if (mImageLoader == null) {
				mImageLoader = createImageLoader(this);
			}
			return mImageLoader;
		}
		return super.getSystemService(name);
	}

}
