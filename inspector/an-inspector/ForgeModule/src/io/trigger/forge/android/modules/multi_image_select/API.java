package io.trigger.forge.android.modules.multi_image_select;

import io.trigger.forge.android.core.ForgeApp;
import io.trigger.forge.android.core.ForgeTask;

import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class API {
	static View lastView = null;
	public static void getImages(final ForgeTask task) {
		task.performUI(new Runnable() {
			
			public void run() {
				final Vector<LazyImageView> images = new Vector<LazyImageView>();			
				final Vector<CheckBox> checkboxes = new Vector<CheckBox>();
				
				final RelativeLayout container = new RelativeLayout(ForgeApp.getActivity());
				container.setTag(task);
				container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				container.setBackgroundColor(Color.rgb(240, 240, 240));
				lastView = container;

				TextView title = new TextView(ForgeApp.getActivity());
				title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 50));
				title.setGravity(Gravity.CENTER);
				title.setTextSize(20);
				title.setTextColor(Color.rgb(49, 49, 49));
				title.setText("Select images");
				container.addView(title);
				
				ScrollView scroll = new ScrollView(ForgeApp.getActivity()) {
					@Override
					protected void onScrollChanged(int l, int t, int oldl,
							int oldt) {
						super.onScrollChanged(l, t, oldl, oldt);
						if (Math.floor(oldt/250) - Math.floor(t/250) != 0) {
							for (LazyImageView imageView : images) {
								imageView.showIfRequired();
							}
						}
					}
				};

				RelativeLayout.LayoutParams scrollLayout = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				scrollLayout.setMargins(0, 50, 0, 100);
				scroll.setLayoutParams(scrollLayout);
				container.addView(scroll);

				LinearLayout layout = new LinearLayout(ForgeApp.getActivity());
				layout.setOrientation(LinearLayout.VERTICAL);
				scroll.addView(layout);
				
				@SuppressWarnings("deprecation")
				Cursor cursor = ForgeApp.getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { ImageColumns._ID }, null, null, null);
				
				Bitmap placeholder = BitmapFactory.decodeResource(ForgeApp.getActivity().getResources(), ForgeApp.getResourceId("multi_image_select_loading", "drawable"));
				placeholder = Bitmap.createScaledBitmap(placeholder, 200, 200, true);
				
				Bitmap error = BitmapFactory.decodeResource(ForgeApp.getActivity().getResources(), ForgeApp.getResourceId("multi_image_select_error", "drawable"));
				error = Bitmap.createScaledBitmap(error, 200, 200, true);
				
				int count = 0;
				int rows = Math.min(ForgeApp.getActivity().webView.getWidth(), ForgeApp.getActivity().webView.getHeight()) / 220;
				
				LinearLayout curLayout = null;
				while (cursor != null && cursor.moveToNext()) {
					if (count == 0) {
						curLayout = new LinearLayout(ForgeApp.getActivity());
						curLayout.setGravity(Gravity.CENTER_HORIZONTAL);
						layout.addView(curLayout);
					}
					count = (count + 1) % rows;
					RelativeLayout rl = new RelativeLayout(ForgeApp.getActivity());			
					rl.setPadding(0, 10, count != 0 ? 10 : 0, 0);
					
					final int imageId = cursor.getInt(0);
					
					LazyImageView iv = new LazyImageView(ForgeApp.getActivity(), imageId, placeholder, error);	
					iv.setLayoutParams(new LayoutParams(200, 200));
					rl.addView(iv);
					images.add(iv);
					
					final CheckBox check = new CheckBox(ForgeApp.getActivity());
					check.setTag(imageId);
					checkboxes.add(check);
					rl.addView(check);
					
					iv.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							check.setChecked(!check.isChecked());
						}
					});
					
					curLayout.addView(rl);
				}
				curLayout.setPadding(0, 0, 0, 10);
				
				LinearLayout buttons = new LinearLayout(ForgeApp.getActivity());
				RelativeLayout.LayoutParams buttonsLayout = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100);
				buttonsLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				buttons.setLayoutParams(buttonsLayout);
				buttons.setPadding(10, 10, 10, 0);
				container.addView(buttons);
				
				Button cancel = new Button(ForgeApp.getActivity());
				cancel.setText("Cancel");
				cancel.setTextSize(15);
				cancel.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
				cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						ForgeApp.getActivity().removeModalView(container, new Runnable() {
							public void run() {
								task.error("User cancelled", "EXPECTED_FAILURE", null);
								lastView = null;
							}
						});
					}
				});
				buttons.addView(cancel);
				
				Button select = new Button(ForgeApp.getActivity());
				select.setText("Select");
				select.setTextSize(15);
				select.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
				select.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						ForgeApp.getActivity().removeModalView(container, new Runnable() {
							public void run() {
								JsonArray images = new JsonArray();
								for (CheckBox checkBox : checkboxes) {
									if (checkBox.isChecked()) {
										JsonObject image = new JsonObject();
										image.addProperty("type", "image");
										image.addProperty("uri", "content://media/external/images/media/"+(Integer)checkBox.getTag());
										images.add(image);
									}
								}
								task.success(images);
								lastView = null;
							}
						});
					}
				});
				buttons.addView(select);
				
				ForgeApp.getActivity().addModalView(container);
				
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {}
						
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
						for (LazyImageView imageView : images) {
							imageView.showIfRequired();
						}
					}
				}.execute();
			}
		});
	}
}