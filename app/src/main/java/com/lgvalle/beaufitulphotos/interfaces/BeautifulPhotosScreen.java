package com.lgvalle.beaufitulphotos.interfaces;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Interface for main activity.
 * Just a simple example to illustrate how it's works
 */
public interface BeautifulPhotosScreen {
	/**
	 * Show error on UI
	 * @param errorId Error resource id
	 */
	void showError(int errorId);

	/**
	 * Update UI title
	 * @param titleId Title resource id
	 */
	void updateTitle(int titleId);

}
