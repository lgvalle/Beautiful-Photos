package com.lgvalle.photosnearby.gallery;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.InjectView;
import com.lgvalle.photosnearby.BaseFragment;
import com.lgvalle.photosnearby.R;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosFragment extends BaseFragment {
	@InjectView(R.id.photos_list)
	ListView list;

	public static PhotosFragment newInstance() {
		PhotosFragment f = new PhotosFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	protected void initLayout() {
		//QuestRenderer renderer = new QuestRenderer();
		//this.adapter = new RendererAdapter<Quest>(LayoutInflater.from(getActivity()), renderer, getActivity());
		//this.list.setAdapter(adapter);
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photos_list;
	}
}
