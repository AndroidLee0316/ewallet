package com.pasc.lib.glide.request.transition;

import android.view.View;

/**
 * A {@link Transition} that accepts an interface that can apply an animation like a {@link
 * android.view.ViewPropertyAnimator} or a {@link android.animation.ObjectAnimator} that can be used
 * to transition a resource into a {@link View}.
 *
 * @param <R> The type of the resource that will be transitioned into a view.
 */
public class ViewPropertyTransition<R> implements Transition<R> {

  private final Animator animator;

  /**
   * Constructor for a view property animation that takes an {@link Animator}
   * interface that can apply a transition to a view.
   *
   * @param animator The animator to use.
   */
  // Public API.
  @SuppressWarnings("WeakerAccess")
  public ViewPropertyTransition(Animator animator) {
    this.animator = animator;
  }

  /**
   * Always applies the {@link Animator} given in the constructor to the
   * given view and returns {@code false} because the animator cannot put the new resource on the
   * view.
   *
   * @param current {@inheritDoc}
   * @param adapter {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public boolean transition(R current, ViewAdapter adapter) {
    final View view = adapter.getView();
    if (view != null) {
      animator.animate(adapter.getView());
    }
    return false;
  }

  /**
   * An interface that allows an animation to be applied on or started from an {@link
   * View}.
   */
  public interface Animator {
    /**
     * Starts an animation on the given {@link View}.
     *
     * @param view The view to transition.
     */
    void animate(View view);
  }

}
