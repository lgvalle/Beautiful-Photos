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

import static org.junit.Assert.assertTrue;

/**
 * Created by luis.gonzalez on 23/07/14.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PhotoAdapterTest {

	private GalleryItemRenderer renderer;
	private RendererAdapter adapter;
	private List<PhotoModel> photos;

	@Before
	public void setUp() throws Exception {
		renderer = new GalleryItemRenderer();
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(Robolectric.application), renderer, Robolectric.application);

		photos = new ArrayList<PhotoModel>();
		photos.add(new Photo500px());
		photos.add(new Photo500px());
		photos.add(new Photo500px());

		adapter.addElements(photos);
	}

	@Test
	public void testGetCount() throws Exception {
		assertTrue(adapter.getCount() == photos.size());
	}

}
