package com.lgvalle.beaufitulphotos;

import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Photo500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import retrofit.Callback;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by lgvalle on 23/07/14.
 */

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class BeautifulPhotosPresenterTest {

	PhotosResponse photosResponse = new PhotosResponse();

	private BeautifulPhotosPresenter presenter;
	private ApiService500px service;
	private ArrayList<Photo500px> photos;
	private BeautifulPhotosScreen screen;
	private PhotosAvailableEvent event;

	@After
	public void tearDown() {
		BusHelper.unregister(this);
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		initializeMocks();
		photos = new ArrayList<Photo500px>();
		photos.add(new Photo500px("title1"));
		photos.add(new Photo500px("title2"));
		photos.add(new Photo500px("title3"));
		photosResponse.setPhotos(photos);

		presenter = new BeautifulPhotosPresenterImpl(screen, service);

		BusHelper.register(this);
	}

	@Test
	public void getPhotos_ValidResponse_ShouldProduceValidEventOnBus() {
		doAnswer(createValidAnswer()).when(service).getPhotosPopular(anyString(), anyInt(), anyInt(), anyInt(), any(Callback.class));
		presenter.needPhotos("");

		assertNotNull(event.getPhotos());
		assertThat(event.getPhotos().size(), equalTo(photosResponse.getPhotos().size()));
	}

	@Test
	public void getPhotos_EmptyResponse_ShouldProduceEmptyEventOnBus() {
		doAnswer(createEmptyAnswer()).when(service).getPhotosPopular(anyString(), anyInt(), anyInt(), anyInt(), any(Callback.class));
		presenter.needPhotos("");

		assertNotNull(event.getPhotos());
		assertTrue(event.getPhotos().size() == 0);
	}

	@Test
	public void getPhotos_FailureResponse_ShouldProduceEmptyEventOnBusAndFailureMessageOnScreen() {
		doAnswer(createFailureAnswer()).when(service).getPhotosPopular(anyString(), anyInt(), anyInt(), anyInt(), any(Callback.class));
		presenter.needPhotos("");

		//assertNull(event.getPhotos());
		verify(screen).showError(R.string.service_error);
	}

	@Subscribe
	public void onPhotosAvailableEvent(PhotosAvailableEvent event) {
		this.event = event;
	}



	private void initializeMocks() {
		service = mock(ApiService500px.class);
		screen = mock(BeautifulPhotosScreen.class);
	}

	private Answer createValidAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[4];
				callback.success(photosResponse, null);
				return null;
			}
		};
	}

	private Answer createEmptyAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[4];
				callback.success(new PhotosResponse(), null);
				return null;
			}
		};
	}

	private Answer createFailureAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[4];
				callback.failure(null);
				return null;
			}
		};
	}
}
