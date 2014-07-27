package com.lgvalle.beaufitulphotos.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;
import com.lgvalle.beaufitulphotos.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 27/07/14.
 * <p/>
 * Custom class to wrap common Picasso calls.
 */
public class PicassoHelper {
	/**
	 * Wrap Picasso. Load url into target. Default callback shows error if something wrong happen.
	 *
	 * @param url    Photo url
	 * @param target ImageView where photo is going to be loaded
	 */
	public static void load(final Context ctx, String url, ImageView target) {
		load(ctx, url, target, new Callback() {
			@Override
			public void onError() {
				Toast.makeText(ctx, ctx.getString(R.string.service_error), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess() {
				/* Photo loaded. Nothing else to do */
			}
		});
	}

	/**
	 * Wrap Picasso. Load url into target and execute param callback
	 *
	 * @param url    Photo url
	 * @param target ImageView where photo is going to be loaded
	 */
	public static void load(Context ctx, String url, ImageView target, Callback callback) {
		Picasso.with(ctx).load(url).into(target, callback);
	}

	/**
	 * Wrap Picasso. Load url into target, apply blur transformation and execute param callback
	 *
	 * @param url      Photo url
	 * @param target   ImageView where photo is going to be loaded
	 * @param callback Callback executed after image is loaded
	 */
	public static void loadWithBlur(Context ctx, String url, ImageView target, Callback callback) {
		// After loading, execute parameter callback
		Picasso.with(ctx).load(url).transform(new BlurTransformation(ctx)).into(target, callback);
	}
}
