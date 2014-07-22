package com.lgvalle.beaufitulphotos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by lgvalle on 21/07/14.
 */
public abstract class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(getContentView(), container, false);
		ButterKnife.inject(this, rootView);
		initLayout();
		return rootView;
	}


	protected void displayHomeAsUp(boolean b) {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(b);
	}
	/**
	 * Bind layout
	 */
	protected abstract void initLayout();

	/**
	 * @return Activity layout resource
	 */
	protected abstract int getContentView();
}
