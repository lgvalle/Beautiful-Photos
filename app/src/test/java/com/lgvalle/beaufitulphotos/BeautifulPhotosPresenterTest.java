package com.lgvalle.beaufitulphotos;

import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import retrofit.Callback;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by lgvalle on 23/07/14.
 */

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class BeautifulPhotosPresenterTest {

	@Captor
	private ArgumentCaptor<Callback<PhotosResponse>> cb;

	private BeautifulPhotosPresenter presenter;
	private ApiService500px service;


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		initializeMocks();
		presenter = new BeautifulPhotosPresenterImpl();
	}


	private void initializeMocks() {
		service = mock(ApiService500px.class);
		  /*
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Callback callback = (Callback) invocation.getArguments()[0];
				callback.success();
				return null;
			}
		}).when(service).get(any(Callback.class));
		*/
	}
}
