package com.lgvalle.beaufitulphotos;

import android.view.LayoutInflater;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Photo500px;
import com.lgvalle.beaufitulphotos.gallery.GalleryItemRenderer;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.RendererAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by luis.gonzalez on 23/07/14.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PhotoAdapterTest {

	private GalleryItemRenderer renderer;
	private RendererAdapter<PhotoModel> adapter;
	private List<PhotoModel> photos;
	private List<PhotoModel> morePhotos;
	private int numElements;

	@Before
	public void setUp() throws Exception {
		renderer = new GalleryItemRenderer();
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(Robolectric.application), renderer, Robolectric.application);

		photos = new ArrayList<PhotoModel>();
		photos.add(new Photo500px("title1"));
		photos.add(new Photo500px("title2"));
		photos.add(new Photo500px("title3"));

		morePhotos = new ArrayList<PhotoModel>();
		morePhotos.add(new Photo500px("other1"));
		morePhotos.add(new Photo500px("other2"));
		morePhotos.add(new Photo500px("other3"));

		numElements = photos.size() + morePhotos.size();

		adapter.addElements(photos);
		adapter.addElements(morePhotos);
	}

	@Test
	public void getCount_shouldReturnListSize() throws Exception {
		assertThat(adapter.getCount(), equalTo(numElements));
	}

	@Test
	public void getItem_shouldReturnObjectAtIndex() throws Exception {
		assertThat(adapter.getItem(0), equalTo(photos.get(0)));
		assertThat(adapter.getItem(1), equalTo(photos.get(1)));
		assertThat(adapter.getItem(2), equalTo(photos.get(2)));

		assertThat(adapter.getItem(3), equalTo(morePhotos.get(0)));
		assertThat(adapter.getItem(4), equalTo(morePhotos.get(1)));
		assertThat(adapter.getItem(5), equalTo(morePhotos.get(2)));
	}



}
