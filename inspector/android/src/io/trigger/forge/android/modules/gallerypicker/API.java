package io.trigger.forge.android.modules.gallerypicker;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import io.trigger.forge.android.core.ForgeTask;

import android.content.Intent;
import android.widget.Toast;
import android.net.Uri;
import io.trigger.forge.android.core.ForgeIntentResultHandler;

import com.chute.android.multiimagepicker.app.MultiImagePickerActivity;
import com.chute.android.multiimagepicker.intent.ChoosePhotosActivityIntentWrapper;

public class API {
	
	public static void getImages(final ForgeTask task) throws JSONException {
		
		// Result Handler
		ForgeIntentResultHandler handler = new ForgeIntentResultHandler() {
		   @Override
		   public void result(int requestCode, int resultCode, Intent data) {
				JSONArray images = new JSONArray();				
				if (resultCode != android.app.Activity.RESULT_OK) {
					Toast.makeText(task.context, "No photos selected", Toast.LENGTH_SHORT).show();
					task.success(images);
					return;
				} 
				
				ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(data);
				ArrayList<String> results = wrapper.getAssetPathList();
				for (String path : results) {
					path = Uri.fromFile(new File(path)).toString();
					JSONObject file = new JSONObject();
					try {
						file.put("type", "image");
						file.put("uri",  path);
					} catch (JSONException e) {
						task.error(e);
					}
					images.put(file);
				}
				task.success(images);
		   }
		};
		
		// Kick off MultiImageChooserActivity
		Intent intent;		
		intent = new Intent(task.context, MultiImagePickerActivity.class);
		task.intentWithHandler(intent, handler);
	}

	// Helper because forge.file.URL is not available to us in the plugin development environment
	public static void URL(final ForgeTask task) throws JSONException {		
        if (!task.params.has("uri") || task.params.isNull("uri")) {
            task.error("Invalid parameters sent to forge.file.URL", "BAD_INPUT", null);
            return;
        }        
        try {
        	task.success(Util.fileToUri(task.context, task.params).toString());
        } catch (Exception e) {
            task.error("Error reading file", "UNEXPECTED_FAILURE", null);
        }
	}
}
