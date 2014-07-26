package com.lgvalle.beaufitulphotos;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import org.junit.After;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

/**
 * Thanks to: http://blog.nikhaldimann.com/2013/10/10/robolectric-2-2-some-pages-from-the-missing-manual/
 */
public class FragmentTestCase<T extends Fragment> {
	private static final String FRAGMENT_TAG = "fragment";

	private ActivityController controller;
	private FragmentActivity activity;
	private T fragment;

	@After
	public void destroyFragment() {
		if (fragment != null) {
			FragmentManager manager = activity.getSupportFragmentManager();
			manager.beginTransaction().remove(fragment).commit();
			fragment = null;
			activity = null;
		}
	}

	public void pauseAndResumeFragment() {
		controller.pause().resume();
	}

	public T recreateFragment() {
		activity.recreate();
		// Recreating the activity creates a new instance of the
		// fragment which we need to retrieve by tag.
		fragment = (T) activity.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		return fragment;
	}

	/**
	 * Adds the fragment to a new blank activity, thereby fully
	 * initializing its view.
	 */
	public void startFragment(T fragment) {
		this.fragment = fragment;
		controller = Robolectric.buildActivity(ActionBarActivity.class);
		activity = (ActionBarActivity) controller.create().start().visible().get();
		FragmentManager manager = activity.getSupportFragmentManager();
		manager.beginTransaction().add(fragment, FRAGMENT_TAG).commit();
	}

}
