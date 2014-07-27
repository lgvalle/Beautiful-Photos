package com.lgvalle.beaufitulphotos.utils;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

/**
 * Created by lgvalle on 21/04/14.
 * <p/>
 * Otto Bus helper
 * <p/>
 * Creates a single app bus
 * It has convenience methods to register and unregister objects from bus.
 * <p/>
 * It can post events from threads other than mainthread.
 */
public class BusHelper {
	private static final Handler mainThread = new Handler(Looper.getMainLooper());
	private static final Bus bus;

	/**
	 * Hide constructor
	 */
	private BusHelper() {}

	/**
	 * Lazy bus creation
	 */
	static {
		bus = new Bus();
	}

	/**
	 * Post in bus.
	 * If current thread is main thread, post.
	 * If not, get main thread and post.
	 *
	 * @see <a href=https://github.com/square/otto/issues/38#issuecomment-11014611>Jacke post about Otto and Threads</a>
	 */
	public static void post(final Object event) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			bus.post(event);
		} else {
			mainThread.post(new Runnable() {
				@Override
				public void run() {
					post(event);
				}
			});
		}
	}

	public static void register(Object cls) {
		bus.register(cls);
	}

	public static void unregister(Object cls) {
		bus.unregister(cls);
	}
}
