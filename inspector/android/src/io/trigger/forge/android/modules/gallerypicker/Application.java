package io.trigger.forge.android.modules.gallerypicker;

import android.content.Context;
import android.util.TypedValue;

import com.chute.android.multiimagepicker.R;
import com.darko.imagedownloader.ImageLoader;

// As required by: https://github.com/chute/chute-tutorials/tree/master/Android/Multi-Image%20Picker%20Tutorial
public class Application extends android.app.Application {
	private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder_image_small);
		imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources().getDisplayMetrics()));
		return imageLoader;
	}

	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mImageLoader = createImageLoader(this);
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return mImageLoader;
		} else {
			return super.getSystemService(name);
		}
	}
}
