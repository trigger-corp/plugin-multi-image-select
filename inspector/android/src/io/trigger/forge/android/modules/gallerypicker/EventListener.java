package io.trigger.forge.android.modules.gallerypicker;

import android.util.TypedValue;

import io.trigger.forge.android.core.ForgeApp;
import io.trigger.forge.android.core.ForgeEventListener;

import com.darko.imagedownloader.ImageLoader;

//As required by: https://github.com/chute/chute-tutorials/tree/master/Android/Multi-Image%20Picker%20Tutorial
public class EventListener extends ForgeEventListener {
	private ImageLoader imageLoader = null;

	@Override
	public Object getSystemService(String name) {
		android.util.Log.d("CHUTE", "EventListener.getSystemService -> " + name);		
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			if (imageLoader == null) {
				imageLoader = new ImageLoader(ForgeApp.getActivity(), 
						ForgeApp.getResourceId("placeholder_image_small", "drawable")); 
				imageLoader.setDefaultBitmapSize((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
						75, ForgeApp.getActivity().getResources().getDisplayMetrics()));
			}
			return imageLoader;
		} else {
			return null; 
		}
	}
}
