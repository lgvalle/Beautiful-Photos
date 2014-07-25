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

	@Before
	public void setUp() throws Exception {
		renderer = new GalleryItemRenderer();
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(Robolectric.application), renderer, Robolectric.application);

		photos = new ArrayList<PhotoModel>();
		photos.add(new Photo500px("title1"));
		photos.add(new Photo500px("title2"));
		photos.add(new Photo500px("title3"));

		adapter.addElements(photos);
	}

	@Test
	public void getCount_shouldReturnListSize() throws Exception {
		assertThat(adapter.getCount(), equalTo(3));
		photos.add(new Photo500px("title"));
		adapter.addElements(photos);
		assertThat(adapter.getCount(), equalTo(4));

	}

	@Test
	public void getItem_shouldReturnObjectAtIndex() throws Exception {
		assertThat(adapter.getItem(0), equalTo(photos.get(0)));
		assertThat(adapter.getItem(1), equalTo(photos.get(1)));
		assertThat(adapter.getItem(2), equalTo(photos.get(2)));
	}



}
