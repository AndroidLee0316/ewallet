package com.pasc.lib.glide.manager;

import android.support.annotation.NonNull;

/**
 * A {@link Lifecycle} implementation for tracking and notifying
 * listeners of {@link android.app.Application} lifecycle events.
 *
 * <p> Since there are essentially no {@link android.app.Application} lifecycle events, this class
 * simply defaults to notifying new listeners that they are started. </p>
 */
class ApplicationLifecycle implements Lifecycle {
  @Override
  public void addListener(@NonNull LifecycleListener listener) {
    listener.onStart();
  }

  @Override
  public void removeListener(@NonNull LifecycleListener listener) {
    // Do nothing.
  }
}
