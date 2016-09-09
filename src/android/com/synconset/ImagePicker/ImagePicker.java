/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.synconset;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class ImagePicker extends CordovaPlugin {
	public static String TAG = "ImagePicker";

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 this.params = args.getJSONObject(0);
		if (action.equals("getPictures")) {
			Intent intent = new Intent(cordova.getActivity(), MultiImageChooserActivity.class);
			int max = 20;
			int desiredWidth = 0;
			int desiredHeight = 0;
			int quality = 100;

			String processing_images_header = "";
			String processing_images_message = "";
			String maximum_selection_count_error_header = "";
			String maximum_selection_count_error_message = "";
			
			if (this.params.has("maximumImagesCount")) {
				max = this.params.getInt("maximumImagesCount");
			}
			if (this.params.has("width")) {
				desiredWidth = this.params.getInt("width");
			}
			if (this.params.has("height")) {
				desiredHeight = this.params.getInt("height");
			}
			if (this.params.has("quality")) {
				quality = this.params.getInt("quality");
			}
			if (this.params.has("processing_images_header")) {
				processing_images_header = this.params.getString("processing_images_header");
			}
			if (this.params.has("processing_images_message")){
				processing_images_message = this.params.getString("processing_images_message");
			}
			if (this.params.has("maximum_selection_count_error_header")){
				maximum_selection_count_error_header = this.params.getString("maximum_selection_count_error_header");
			}
			if (this.params.has("maximum_selection_count_error_message")){
				maximum_selection_count_error_message = this.params.getString("maximum_selection_count_error_message");
			}
			intent.putExtra("MAX_IMAGES", max);
			intent.putExtra("WIDTH", desiredWidth);
			intent.putExtra("HEIGHT", desiredHeight);
			intent.putExtra("QUALITY", quality);
			intent.putExtra("PROCESSING_IMAGES_HEADER", processing_images_header);
			intent.putExtra("PROCESSING_IMAGES_MESSAGE", processing_images_message);
			intent.putExtra("MAXIMUM_SELECTION_COUNT_ERROR_HEADER", maximum_selection_count_error_header);
			intent.putExtra("MAXIMUM_SELECTION_COUNT_ERROR_MESSAGE", maximum_selection_count_error_message);
			if (this.cordova != null) {
				this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
			}
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
			JSONArray res = new JSONArray(fileNames);
			this.callbackContext.success(res);
		} else if (resultCode == Activity.RESULT_CANCELED && data != null) {
			String error = data.getStringExtra("ERRORMESSAGE");
			this.callbackContext.error(error);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			JSONArray res = new JSONArray();
			this.callbackContext.success(res);
		} else {
			this.callbackContext.error("No images selected");
		}
	}
}
