package com.pasc.lib.glide.load.resource.drawable;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.pasc.lib.glide.load.engine.Initializable;
import com.pasc.lib.glide.load.engine.Resource;
import com.pasc.lib.glide.load.resource.gif.GifDrawable;
import com.pasc.lib.glide.util.Preconditions;

/**
 * Simple wrapper for an Android {@link Drawable} which returns a
 * {@link ConstantState#newDrawable() new drawable}
 * based on it's {@link ConstantState state}.
 *
 * <b>Suggested usages only include {@code T}s where the new drawable is of the same or descendant
 * class.</b>
 *
 * @param <T> type of the wrapped {@link Drawable}
 */
public abstract class DrawableResource<T extends Drawable> implements Resource<T>,
    Initializable {
  protected final T drawable;

  public DrawableResource(T drawable) {
    this.drawable = Preconditions.checkNotNull(drawable);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  @Override
  public final T get() {
    @Nullable ConstantState state = drawable.getConstantState();
    if (state == null) {
      return drawable;
    }
    // Drawables contain temporary state related to how they're being displayed
    // (alpha, color filter etc), so return a new copy each time.
    // If we ever return the original drawable, it's temporary state may be changed
    // and subsequent copies may end up with that temporary state. See #276.
    return (T) state.newDrawable();
  }

  @Override
  public void initialize() {
    if (drawable instanceof BitmapDrawable) {
      ((BitmapDrawable) drawable).getBitmap().prepareToDraw();
    } else if (drawable instanceof GifDrawable) {
      ((GifDrawable) drawable).getFirstFrame().prepareToDraw();
    }
  }
}
