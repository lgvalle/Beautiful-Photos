package com.lgvalle.beaufitulphotos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by lgvalle on 21/07/14.
 * Base class for all activities
 * Provides an initialization template and hooks for child activities.
 * <p/>
 * Provides common utility methods
 */
public abstract class BaseActivity extends ActionBarActivity {

	/**
	 * @return Activity layout resource
	 */
	protected abstract int getContentView();

	/**
	 * Init Activity Presenter
	 */
	protected abstract void initPresenter();

	/**
	 * Bind layout
	 */
	protected abstract void initLayout();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Child classes can override or extend this methods to provide their own functionality
		setLayout();
		getExtras();
		initPresenter();
		initLayout();
		initActionBar();
	}

	/**
	 * Add fragment to container
	 *
	 * @param fragmentContainer container id in which fragment is added
	 * @param fragment          fragment to add
	 */
	protected void addFragment(int fragmentContainer, Fragment fragment) {
		getSupportFragmentManager().beginTransaction().add(fragmentContainer, fragment).commit();
	}

	/**
	 * Add fragment to container and also to fragmentmanager backstack
	 *
	 * @param fragmentContainer container id in which fragment is added
	 * @param fragment          fragment to add
	 */
	protected void addFragmentToBackStack(int fragmentContainer, Fragment fragment) {
		getSupportFragmentManager().beginTransaction().addToBackStack(null).add(fragmentContainer, fragment).commit();
	}

	/**
	 * Convenience method to give child classes room for extras processing
	 */
	protected void getExtras() {
	}

	/**
	 * Convenience method to give child classes room for actionbar title modifications
	 */
	protected void initActionBar() {
	}

	protected void replaceFragment(int fragmentContainer, Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(fragmentContainer, fragment).commit();
	}

	/**
	 * By default sets content view with layout provided. Can be extended
	 */
	protected void setLayout() {
		setContentView(getContentView());
	}
}


