package com.lgvalle.beaufitulphotos;

import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.Api500pxService;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Feature;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Photo500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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

	Response<PhotosResponse> validResponse;
	PhotosResponse photosResponse = new PhotosResponse();

	private BeautifulPhotosPresenter presenter;
	private Api500pxService service;
	private ArrayList<Photo500px> photos;
	private BeautifulPhotosScreen screen;
	private PhotosAvailableEvent event;
	private Response invalidResponse;
	private Response emptyResponse;

	@Test
	public void getPhotos_EmptyResponse_ShouldProduceEmptyEventOnBus() {
		doAnswer(createEmptyAnswer()).when(service).getPhotos(anyString(), anyInt(), anyInt(), anyString(), anyInt(), any(Callback.class));
		presenter.needPhotos();
		assertNotNull(event);
		assertNotNull(event.getPhotos());
		assertTrue(event.getPhotos().size() == 0);
	}

	@Test
	public void getPhotos_FailureResponse_ShouldProduceEmptyEventOnBusAndFailureMessageOnScreen() {
		doAnswer(createFailureAnswer()).when(service).getPhotos(anyString(), anyInt(), anyInt(), anyString(), anyInt(), any(Callback.class));
		presenter.needPhotos();

		verify(screen).showError(R.string.service_error);
	}

	@Test
	public void getPhotos_ValidResponse_ShouldProduceValidEventOnBus() {
		doAnswer(createValidAnswer()).when(service).getPhotos(anyString(), anyInt(), anyInt(), anyString(), anyInt(), any(Callback.class));
		presenter.needPhotos();
		assertNotNull(event);
		assertNotNull(event.getPhotos());
		assertThat(event.getPhotos().size(), equalTo(photosResponse.getPhotos().size()));
	}

	@Subscribe
	public void onPhotosAvailableEvent(PhotosAvailableEvent event) {
		this.event = event;
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

		presenter = new BeautifulPhotosPresenterImpl(screen, service, Feature.HighestRated);

		BusHelper.register(this);
	}

	@After
	public void tearDown() {
		BusHelper.unregister(this);
	}

	private Answer createEmptyAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[5];
				callback.onResponse(emptyResponse);
				return null;
			}
		};
	}

	private Answer createFailureAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[5];
				callback.onResponse(invalidResponse);
				return null;
			}
		};
	}

	private Answer createValidAnswer() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback<PhotosResponse> callback = (Callback<PhotosResponse>) invocation.getArguments()[5];
				callback.onResponse(validResponse);
				return null;
			}
		};
	}

	private void initializeMocks() {
		service = mock(Api500pxService.class);
		screen = mock(BeautifulPhotosScreen.class);
		validResponse = mock(Response.class);
		when(validResponse.getResult()).thenReturn(photosResponse);
		when(validResponse.getStatusCode()).thenReturn(HttpStatus.SC_OK);

		invalidResponse = mock(Response.class);
		when(invalidResponse.getStatusCode()).thenReturn(HttpStatus.SC_BAD_GATEWAY);

		emptyResponse = mock(Response.class);
		when(emptyResponse.getResult()).thenReturn(new PhotosResponse());
		when(emptyResponse.getStatusCode()).thenReturn(HttpStatus.SC_OK);
	}
}
