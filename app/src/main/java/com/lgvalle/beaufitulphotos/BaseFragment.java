package com.lgvalle.beaufitulphotos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by lgvalle on 21/07/14.
 * Base class for all fragments.
 * It's main function is to provide a template for view creation and injection
 */
public abstract class BaseFragment extends Fragment {

	/**
	 * Bind layout
	 */
	protected abstract void initLayout();

	/**
	 * @return Activity layout resource
	 */
	protected abstract int getContentView();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(getContentView(), container, false);
		// Inject views after inflating and just before init layout. This way every child fragment follows correct sequence
		ButterKnife.inject(this, rootView);
		initLayout();

		return rootView;
	}

	/**
	 * Wrapper for null check
	 *
	 * @param b if true up is enabled
	 */
	protected void displayHomeAsUp(boolean b) {
		if (getActivity() != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(b);
		}
	}

	protected void setActionBarTitle(String title) {
		if (getActivity() != null) {
			getActivity().getActionBar().setTitle(title);
		}

	}
}
