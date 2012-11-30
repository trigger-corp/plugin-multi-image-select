package io.trigger.forge.android.modules.gallerypicker;

import io.trigger.forge.android.core.ForgeLog;
import io.trigger.forge.android.inspector.R;

import android.content.Context;
import android.util.TypedValue;

//import com.chute.android.multiimagepicker.app.MultiImagePickerApp;

import com.darko.imagedownloader.ImageLoader;

public class Application extends android.app.Application {
	private static ImageLoader createImageLoader(Context context) {
		ForgeLog.d("MyApplication.createImageLoader");
		ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder_image_small);
		imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
				.getDisplayMetrics()));
		return imageLoader;
	}

	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		ForgeLog.d("MyApplication.onCreate");
		super.onCreate();
		mImageLoader = createImageLoader(this);
	}

	@Override
	public Object getSystemService(String name) {
		ForgeLog.d("MyApplication.getSystemService");
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			ForgeLog.d("MyApplication.getSystemService - ImageLoader");
			return mImageLoader;
		} else {
			return super.getSystemService(name);
		}
	}
}
