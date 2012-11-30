package io.trigger.forge.android.modules.gallerypicker;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import io.trigger.forge.android.core.ForgeLog;
import io.trigger.forge.android.core.ForgeTask;

import android.app.Activity;
import android.content.Intent;
import io.trigger.forge.android.core.ForgeIntentResultHandler;

import com.chute.android.multiimagepicker.app.MultiImagePickerActivity;
import com.chute.android.multiimagepicker.intent.ChoosePhotosActivityIntentWrapper;

public class API {

	public static void getImages(final ForgeTask task) throws JSONException {
		
		// Kick off MultiImageChooserActivity
		Intent intent;
		ForgeIntentResultHandler handler = new ForgeIntentResultHandler() {
		   @Override
		   public void result(int requestCode, int resultCode, Intent data) {
				JSONArray images = new JSONArray();		
				JSONObject file = new JSONObject();
				try {
					file.put("type", "image");
					file.put("uri", "myselecteduri");
				} catch (JSONException e) {
					task.error(e);
				}
				images.put(file);
				task.success(images);			   
		   }
		};
		
		//intent = new Intent(task.context, MultiImagePickerActivity.class);
		//task.intentWithHandler(intent, handler);
		ForgeLog.d("Launching image gallery 1");
		ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(task.context);
		ForgeLog.d("Launching image gallery 2");
		wrapper.startActivityForResult((Activity) task.context, ChoosePhotosActivityIntentWrapper.ACTIVITY_FOR_RESULT_KEY);
		ForgeLog.d("Launching image gallery 3");
		
		task.success("f");
	}
	
	public static void URL(final ForgeTask task) {
        if (!task.params.has("uri") || task.params.isNull("uri")) {
            task.error("Invalid parameters sent to forge.file.URL", "BAD_INPUT", null);
            return;
        }
        try {
        	task.success(Util.fileToUri(task.context, task.params).toString());
            //task.success(Util.fileToUri(ForgeApp.getActivity(), task.params).toString());
        } catch (Exception e) {
            task.error("Error reading file", "UNEXPECTED_FAILURE", null);
        }
	}
}
