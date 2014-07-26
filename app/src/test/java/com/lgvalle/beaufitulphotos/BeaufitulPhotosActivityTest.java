package com.lgvalle.beaufitulphotos;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.*;

/**
 * Created by lgvalle on 26/07/14.
 */
@Config(emulateSdk = 18, qualifiers = "v10")
@RunWith(RobolectricTestRunner.class)
public class BeaufitulPhotosActivityTest {

	private BeautifulPhotosActivity activity;
	private ActivityController<BeautifulPhotosActivity> controller;
	private SlidingUpPanelLayout slidingPanel;

	@Before
	public void setUp() throws Exception {
		controller = Robolectric.buildActivity(BeautifulPhotosActivity.class).create().start().resume().visible();
		activity = controller.get();
		slidingPanel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
	}

	@Test
	public void shouldHaveCollapsedSlidingPanel() {
		assertNotNull(slidingPanel);
		assertFalse(slidingPanel.isPanelExpanded());
	}

	@Test
	public void shouldHaveDetailsFragmentLoaded() {
		Fragment galleryFragment = activity.getSupportFragmentManager().findFragmentByTag(BeautifulPhotosActivity.FRAGMENT_DETAILS_TAG);
		assertNotNull(galleryFragment);
	}

	@Test
	public void shouldHaveGalleryFragmentLoaded() {
		Fragment galleryFragment = activity.getSupportFragmentManager().findFragmentByTag(BeautifulPhotosActivity.FRAGMENT_GALLERY_TAG);
		assertNotNull(galleryFragment);
	}

	@Test
	public void shouldHaveVisibleActionBar() {
		ActionBar actionBar = activity.getSupportActionBar();
		assertNotNull(actionBar);
		assertTrue(actionBar.isShowing());
	}


	@After
	public void tearDown() throws Exception {
		controller = controller.pause().stop().destroy();
	}
}
