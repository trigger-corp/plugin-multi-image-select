package io.trigger.forge.android.modules.gallerypicker;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.net.Uri;

public class Util {
	public static Uri fileToUri(final Context context, final JSONObject file) throws JSONException {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		@SuppressWarnings("unchecked")
		Iterator<String> keys = file.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			params.add(new BasicNameValuePair(key, file.getString(key)));
		}

		return Uri.parse("content://" + context.getApplicationContext().getPackageName() + "/file___" + URLEncodedUtils.format(params, "UTF-8"));
	}
}
