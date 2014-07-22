package com.lgvalle.beaufitulphotos.interfaces;

import java.io.Serializable;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Every photo object need to implement this interface.
 * It Let extend this app to many sources, not just 500px
 */
public interface PhotoModel extends Serializable {

	String getSmallUrl();
	String getLargeUrl();
	String getTitle();
	String getAuthorName();
}
