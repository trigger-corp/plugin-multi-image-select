package io.trigger.forge.android.modules.multi_image_select;

import android.view.KeyEvent;
import io.trigger.forge.android.core.ForgeApp;
import io.trigger.forge.android.core.ForgeEventListener;
import io.trigger.forge.android.core.ForgeTask;

public class EventListener extends ForgeEventListener {
	@Override
	public Boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && API.lastView != null) {
			ForgeApp.getActivity().removeModalView(API.lastView, new Runnable() {
				public void run() {
					((ForgeTask) API.lastView.getTag()).error("User cancelled", "EXPECTED_FAILURE", null);
					API.lastView = null;
				}
			});
			return true;
		}
		return null;
	}
}
