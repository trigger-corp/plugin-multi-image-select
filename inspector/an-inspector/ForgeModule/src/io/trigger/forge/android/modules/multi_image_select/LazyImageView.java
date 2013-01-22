package io.trigger.forge.android.modules.multi_image_select;

import io.trigger.forge.android.core.ForgeApp;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

class LazyImageView extends ImageView {
	private int imageId;
	private boolean isActual = false;
	private Bitmap placeholder;
	private Bitmap error;
	
	public LazyImageView(Context context, int imageId, Bitmap placeholder, Bitmap error) {
		super(context);
		this.imageId = imageId;
		this.placeholder = placeholder;
		this.error = error;
		this.setImageBitmap(placeholder);
	}
	
	public void showIfRequired() {
		final LazyImageView iv = this;
		int[] position = new int[2];
		this.getLocationOnScreen(position);
		if (-1000 < position[1] && 2000 > position[1]) {
			if (!isActual) {
				isActual = true;
				AsyncTask task = new AsyncTask<Object, Object, Object>() {
					@Override
					protected Object doInBackground(Object... params) {
						try {
							if (!isActual) {
								return null;
							}
							final Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail(ForgeApp.getActivity().getContentResolver(),
									imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
													
							if (thumb != null && thumb.getWidth() > 0 && thumb.getHeight() > 0) {
								final Bitmap bm = Bitmap.createBitmap(thumb, 0, 0, Math.min(200, thumb.getWidth()), Math.min(200, thumb.getHeight()));
								
								thumb.recycle();
								
								ForgeApp.getActivity().runOnUiThread(new Runnable() {
									public void run() {
										iv.setImageBitmap(bm);
									}
								});
							} else {
								ForgeApp.getActivity().runOnUiThread(new Runnable() {
									public void run() {
										iv.setImageBitmap(error);
									}
								});
							}
						} catch (Exception e) {}
						iv.setTag(null);
						return null;
					}
				};
				task.execute();
				iv.setTag(task);
			}
		} else {
			if (isActual) {
				isActual = false;
				
				new Thread(new Runnable() {
					public void run() {
						if (iv.getTag() != null) {
							AsyncTask task = (AsyncTask)iv.getTag();
							task.cancel(true);
							MediaStore.Images.Thumbnails.cancelThumbnailRequest(ForgeApp.getActivity().getContentResolver(), imageId);
							iv.setTag(null);
						}
					}
				}).start();
								
				this.setImageBitmap(placeholder);
			}
		}
	}
}