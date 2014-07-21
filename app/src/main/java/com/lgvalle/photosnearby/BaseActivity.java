package com.lgvalle.photosnearby;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by lgvalle on 21/07/14.
 * Base class for all activities
 * Provides an initialization template and hooks for child activities.
 * Provides common utility methods
 */
public abstract class BaseActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		getExtras();
		initPresenter();
		initLayout();
		initActionBar();

	}

	/**
	 * Set custom font on a textview array
	 */
	protected void setFont(Typeface font, TextView... views) {
		for(TextView v : views) {
			v.setTypeface(font);
		}
	}

	protected void addFragment(int fragmentContainer, Fragment fragment) {
		getSupportFragmentManager().beginTransaction().add(fragmentContainer, fragment).commit();
	}

	protected void replaceFragment(int fragmentContainer, Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(fragmentContainer, fragment).commit();
	}


	/**
	 * Init Action Bar with app title. Can be extended
	 */
	protected void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("app name");
	}

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

	/**
	 * Convenience to give child classes room for extras processing
 	 */

	protected void getExtras() {}

	/**
	 * By default sets content view with layout provided. Can be extended
	 */
	protected void setLayout() {
		setContentView(getContentView());
	}
}


