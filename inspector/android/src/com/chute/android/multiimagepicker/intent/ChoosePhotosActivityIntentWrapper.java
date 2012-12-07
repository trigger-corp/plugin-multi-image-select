package com.chute.android.multiimagepicker.intent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.multiimagepicker.app.MultiImagePickerActivity;

public class ChoosePhotosActivityIntentWrapper {
    public static final int ACTIVITY_FOR_RESULT_KEY = 112;

    private static final String EXTRA_KEY_PATH_LIST = "key_path_list";

    @SuppressWarnings("unused")
    private static final String TAG = ChoosePhotosActivityIntentWrapper.class.getSimpleName();

    private final Intent intent;

    public ChoosePhotosActivityIntentWrapper(Intent intent) {
	super();
	this.intent = intent;
    }

    public ChoosePhotosActivityIntentWrapper(Context packageContext, Class<?> cls) {
	super();
	intent = new Intent(packageContext, cls);
    }

    public ChoosePhotosActivityIntentWrapper(Context packageContext) {
	super();
	intent = new Intent(packageContext, MultiImagePickerActivity.class);
    }

    public Intent getIntent() {
	return intent;
    }

    public void setAssetPathList(ArrayList<String> pathList) {
	intent.putStringArrayListExtra(EXTRA_KEY_PATH_LIST, pathList);
    }

    public ArrayList<String> getAssetPathList() {
	return intent.getExtras().getStringArrayList(EXTRA_KEY_PATH_LIST);
    }

    public void startActivityForResult(Activity context, int code) {
	context.startActivityForResult(intent, code);
    }
}
