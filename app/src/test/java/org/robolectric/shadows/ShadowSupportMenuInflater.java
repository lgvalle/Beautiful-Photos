package org.robolectric.shadows;

import android.support.v7.internal.view.SupportMenuInflater;
import android.view.Menu;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Created by lgvalle on 26/07/14.
 */
@Implements(SupportMenuInflater.class)
public class ShadowSupportMenuInflater extends ShadowMenuInflater {
	@Implementation
	public void inflate(int menuRes, Menu menu) {
		super.inflate(menuRes, menu);
	}
}
