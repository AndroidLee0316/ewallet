package com.pasc.lib.glide.load;

/**
 * Options for setting the value of {@link android.graphics.Bitmap#getConfig()} for
 * {@link android.graphics.Bitmap}s returned by {@link com.pasc.lib.glide.load.ResourceDecoder}s.
 *
 * <p> Note - In some cases it may not be possible to obey the requested setting, not all
 * {@link com.pasc.lib.glide.load.resource.bitmap.Downsampler}s support setting formats and certain
 * images may not be able to be loaded as certain configurations. Therefore this class represents a
 * preference rather than a requirement. </p>
 */
public enum DecodeFormat {
  /**
   * Bitmaps returned by the {@link com.pasc.lib.glide.load.ResourceDecoder}.
   * should return {@link android.graphics.Bitmap.Config#ARGB_8888} for
   * {@link android.graphics.Bitmap#getConfig()} when possible.
   *
   * <p>On Android O+, this format will will use ARGB_8888 only when it's not possible to use
   * {@link android.graphics.Bitmap.Config#HARDWARE}.
   *
   * <p> GIF images decoded by {@link android.graphics.BitmapFactory} currently use an internal
   * hidden format that is returned as null from {@link android.graphics.Bitmap#getConfig()}. Since
   * we cannot force {@link android.graphics.BitmapFactory} to always return our desired config,
   * this setting is a preference, not a promise.
   */
  PREFER_ARGB_8888,

  /**
   * Identical to {@link #PREFER_ARGB_8888} but prevents Glide from using {@link
   * android.graphics.Bitmap.Config#HARDWARE} on Android O+.
   *
   * @deprecated If you must disable hardware bitmaps, set
   * {@link com.pasc.lib.glide.load.resource.bitmap.Downsampler#ALLOW_HARDWARE_CONFIG} to false
   * instead.
   */
  @Deprecated
  PREFER_ARGB_8888_DISALLOW_HARDWARE,

  /**
   * Bitmaps decoded from image formats that support and/or use alpha (some types of PNGs, GIFs etc)
   * should return {@link android.graphics.Bitmap.Config#ARGB_8888} for
   * {@link android.graphics.Bitmap#getConfig()}. Bitmaps decoded from formats that don't support or
   * use alpha should return {@link android.graphics.Bitmap.Config#RGB_565} for
   * {@link android.graphics.Bitmap#getConfig()}.
   *
   * <p>On Android O+, this format will will use ARGB_8888 only when it's not possible to use
   * {@link android.graphics.Bitmap.Config#HARDWARE}.
   */
  PREFER_RGB_565;

  /**
   * The default value for DecodeFormat.
   */
  public static final DecodeFormat DEFAULT = PREFER_ARGB_8888_DISALLOW_HARDWARE;
}
