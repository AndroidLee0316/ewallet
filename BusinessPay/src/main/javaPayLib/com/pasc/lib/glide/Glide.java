package com.pasc.lib.glide;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.pasc.lib.glide.gifdecoder.GifDecoder;
import com.pasc.lib.glide.load.DecodeFormat;
import com.pasc.lib.glide.load.ResourceDecoder;
import com.pasc.lib.glide.load.data.InputStreamRewinder;
import com.pasc.lib.glide.load.engine.Engine;
import com.pasc.lib.glide.load.engine.bitmap_recycle.ArrayPool;
import com.pasc.lib.glide.load.engine.bitmap_recycle.BitmapPool;
import com.pasc.lib.glide.load.engine.cache.MemoryCache;
import com.pasc.lib.glide.load.engine.prefill.BitmapPreFiller;
import com.pasc.lib.glide.load.engine.prefill.PreFillType;
import com.pasc.lib.glide.load.model.AssetUriLoader;
import com.pasc.lib.glide.load.model.ByteArrayLoader;
import com.pasc.lib.glide.load.model.ByteBufferEncoder;
import com.pasc.lib.glide.load.model.ByteBufferFileLoader;
import com.pasc.lib.glide.load.model.DataUrlLoader;
import com.pasc.lib.glide.load.model.FileLoader;
import com.pasc.lib.glide.load.model.GlideUrl;
import com.pasc.lib.glide.load.model.MediaStoreFileLoader;
import com.pasc.lib.glide.load.model.ResourceLoader;
import com.pasc.lib.glide.load.model.StreamEncoder;
import com.pasc.lib.glide.load.model.StringLoader;
import com.pasc.lib.glide.load.model.UnitModelLoader;
import com.pasc.lib.glide.load.model.UriLoader;
import com.pasc.lib.glide.load.model.UrlUriLoader;
import com.pasc.lib.glide.load.model.stream.HttpGlideUrlLoader;
import com.pasc.lib.glide.load.model.stream.HttpUriLoader;
import com.pasc.lib.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.pasc.lib.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.pasc.lib.glide.load.model.stream.UrlLoader;
import com.pasc.lib.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.pasc.lib.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.pasc.lib.glide.load.resource.bitmap.BitmapEncoder;
import com.pasc.lib.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.pasc.lib.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.pasc.lib.glide.load.resource.bitmap.Downsampler;
import com.pasc.lib.glide.load.resource.bitmap.ResourceBitmapDecoder;
import com.pasc.lib.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.pasc.lib.glide.load.resource.bitmap.UnitBitmapDecoder;
import com.pasc.lib.glide.load.resource.bitmap.VideoDecoder;
import com.pasc.lib.glide.load.resource.bytes.ByteBufferRewinder;
import com.pasc.lib.glide.load.resource.drawable.ResourceDrawableDecoder;
import com.pasc.lib.glide.load.resource.drawable.UnitDrawableDecoder;
import com.pasc.lib.glide.load.resource.file.FileDecoder;
import com.pasc.lib.glide.load.resource.gif.ByteBufferGifDecoder;
import com.pasc.lib.glide.load.resource.gif.GifDrawable;
import com.pasc.lib.glide.load.resource.gif.GifDrawableEncoder;
import com.pasc.lib.glide.load.resource.gif.GifFrameResourceDecoder;
import com.pasc.lib.glide.load.resource.gif.StreamGifDecoder;
import com.pasc.lib.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.pasc.lib.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.pasc.lib.glide.load.resource.transcode.DrawableBytesTranscoder;
import com.pasc.lib.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.pasc.lib.glide.manager.ConnectivityMonitorFactory;
import com.pasc.lib.glide.manager.RequestManagerRetriever;
import com.pasc.lib.glide.request.RequestOptions;
import com.pasc.lib.glide.request.target.ImageViewTargetFactory;
import com.pasc.lib.glide.request.target.Target;
import com.pasc.lib.glide.util.Preconditions;
import com.pasc.lib.glide.util.Util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A singleton to present a simple static interface for building requests with
 * {@link RequestBuilder} and maintaining an {@link Engine}, {@link BitmapPool},
 * {@link com.pasc.lib.glide.load.engine.cache.DiskCache} and {@link MemoryCache}.
 */
public class Glide implements ComponentCallbacks2 {
  private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
  private static final String TAG = "Glide";
  private static volatile Glide glide;
  private static volatile boolean isInitializing;

  private final Engine engine;
  private final BitmapPool bitmapPool;
  private final MemoryCache memoryCache;
  private final BitmapPreFiller bitmapPreFiller;
  private final GlideContext glideContext;
  private final Registry registry;
  private final ArrayPool arrayPool;
  private final RequestManagerRetriever requestManagerRetriever;
  private final ConnectivityMonitorFactory connectivityMonitorFactory;
  private final List<RequestManager> managers = new ArrayList<>();
  private MemoryCategory memoryCategory = MemoryCategory.NORMAL;

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context A context.
   * @see #getPhotoCacheDir(Context, String)
   */
  @Nullable
  public static File getPhotoCacheDir(@NonNull Context context) {
    return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context   A context.
   * @param cacheName The name of the subdirectory in which to store the cache.
   * @see #getPhotoCacheDir(Context)
   */
  @Nullable
  public static File getPhotoCacheDir(@NonNull Context context, @NonNull String cacheName) {
    File cacheDir = context.getCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }

  /**
   * Get the singleton.
   *
   * @return the singleton
   */
  @NonNull
  public static Glide get(@NonNull Context context) {
//    if (glide == null) {
//      synchronized (Glide.class) {
//        if (glide == null) {
//          checkAndInitializeGlide(context);
//        }
//      }
//    }
      // 安全部门扫描出来的
      synchronized (Glide.class) {
        if (glide == null) {
          checkAndInitializeGlide(context);
        }
    }

    return glide;
  }

  private static void checkAndInitializeGlide(@NonNull Context context) {
    // In the thread running initGlide(), one or more classes may call Glide.get(context).
    // Without this check, those calls could trigger infinite recursion.
    if (isInitializing) {
//      throw new IllegalStateException("You cannot call Glide.get() in registerComponents(),"
//          + " use the provided Glide instance instead");
      return;
    }
    isInitializing = true;
    initializeGlide(context);
    isInitializing = false;
  }

  /**
   * @deprecated Use {@link #init(Context, GlideBuilder)} to get a singleton compatible with
   * Glide's generated API.
   *
   * <p>This method will be removed in a future version of Glide.
   */
  @VisibleForTesting
  @Deprecated
  public static synchronized void init(Glide glide) {
    if (Glide.glide != null) {
      tearDown();
    }
    Glide.glide = glide;
  }

  @VisibleForTesting
  public static synchronized void init(@NonNull Context context, @NonNull GlideBuilder builder) {
    if (Glide.glide != null) {
      tearDown();
    }
    initializeGlide(context, builder);
  }

  @VisibleForTesting
  public static synchronized void tearDown() {
    if (glide != null) {
      glide.getContext()
          .getApplicationContext()
          .unregisterComponentCallbacks(glide);
      glide.engine.shutdown();
    }
    glide = null;
  }

  private static void initializeGlide(@NonNull Context context) {
    initializeGlide(context, new GlideBuilder());
  }

  @SuppressWarnings("deprecation")
  private static void initializeGlide(@NonNull Context context, @NonNull GlideBuilder builder) {
    Context applicationContext = context.getApplicationContext();
    Glide glide = builder.build(applicationContext);
    applicationContext.registerComponentCallbacks(glide);
    Glide.glide = glide;
  }



  Glide(
      @NonNull Context context,
      @NonNull Engine engine,
      @NonNull MemoryCache memoryCache,
      @NonNull BitmapPool bitmapPool,
      @NonNull ArrayPool arrayPool,
      @NonNull RequestManagerRetriever requestManagerRetriever,
      @NonNull ConnectivityMonitorFactory connectivityMonitorFactory,
      int logLevel,
      @NonNull RequestOptions defaultRequestOptions,
      @NonNull Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions) {
    this.engine = engine;
    this.bitmapPool = bitmapPool;
    this.arrayPool = arrayPool;
    this.memoryCache = memoryCache;
    this.requestManagerRetriever = requestManagerRetriever;
    this.connectivityMonitorFactory = connectivityMonitorFactory;

    DecodeFormat decodeFormat = defaultRequestOptions.getOptions().get(Downsampler.DECODE_FORMAT);
    bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, decodeFormat);

    final Resources resources = context.getResources();

    registry = new Registry();
    registry.register(new DefaultImageHeaderParser());

    Downsampler downsampler = new Downsampler(registry.getImageHeaderParsers(),
        resources.getDisplayMetrics(), bitmapPool, arrayPool);
    ByteBufferGifDecoder byteBufferGifDecoder =
        new ByteBufferGifDecoder(context, registry.getImageHeaderParsers(), bitmapPool, arrayPool);
    ResourceDecoder<ParcelFileDescriptor, Bitmap> parcelFileDescriptorVideoDecoder =
        VideoDecoder.parcel(bitmapPool);
    ByteBufferBitmapDecoder byteBufferBitmapDecoder = new ByteBufferBitmapDecoder(downsampler);
    StreamBitmapDecoder streamBitmapDecoder = new StreamBitmapDecoder(downsampler, arrayPool);
    ResourceDrawableDecoder resourceDrawableDecoder =
        new ResourceDrawableDecoder(context);
    ResourceLoader.StreamFactory resourceLoaderStreamFactory =
        new ResourceLoader.StreamFactory(resources);
    ResourceLoader.UriFactory resourceLoaderUriFactory =
        new ResourceLoader.UriFactory(resources);
    ResourceLoader.FileDescriptorFactory resourceLoaderFileDescriptorFactory =
        new ResourceLoader.FileDescriptorFactory(resources);
    ResourceLoader.AssetFileDescriptorFactory resourceLoaderAssetFileDescriptorFactory =
        new ResourceLoader.AssetFileDescriptorFactory(resources);
    BitmapEncoder bitmapEncoder = new BitmapEncoder(arrayPool);

    BitmapBytesTranscoder bitmapBytesTranscoder = new BitmapBytesTranscoder();
    GifDrawableBytesTranscoder gifDrawableBytesTranscoder = new GifDrawableBytesTranscoder();

    ContentResolver contentResolver = context.getContentResolver();

    registry
        .append(ByteBuffer.class, new ByteBufferEncoder())
        .append(InputStream.class, new StreamEncoder(arrayPool))
        /* Bitmaps */
        .append(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
        .append(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder)
        .append(
            Registry.BUCKET_BITMAP,
            ParcelFileDescriptor.class,
            Bitmap.class,
            parcelFileDescriptorVideoDecoder)
        .append(
            Registry.BUCKET_BITMAP,
            AssetFileDescriptor.class,
            Bitmap.class,
            VideoDecoder.asset(bitmapPool))
        .append(Bitmap.class, Bitmap.class, UnitModelLoader.Factory.getInstance())
        .append(
            Registry.BUCKET_BITMAP, Bitmap.class, Bitmap.class, new UnitBitmapDecoder())
        .append(Bitmap.class, bitmapEncoder)
        /* BitmapDrawables */
        .append(
            Registry.BUCKET_BITMAP_DRAWABLE,
            ByteBuffer.class,
            BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder))
        .append(
            Registry.BUCKET_BITMAP_DRAWABLE,
            InputStream.class,
            BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, streamBitmapDecoder))
        .append(
            Registry.BUCKET_BITMAP_DRAWABLE,
            ParcelFileDescriptor.class,
            BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, parcelFileDescriptorVideoDecoder))
        .append(BitmapDrawable.class, new BitmapDrawableEncoder(bitmapPool, bitmapEncoder))
        /* GIFs */
        .append(
            Registry.BUCKET_GIF,
            InputStream.class,
            GifDrawable.class,
            new StreamGifDecoder(registry.getImageHeaderParsers(), byteBufferGifDecoder, arrayPool))
        .append(Registry.BUCKET_GIF, ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder)
        .append(GifDrawable.class, new GifDrawableEncoder())
        /* GIF Frames */
        // Compilation with Gradle requires the type to be specified for UnitModelLoader here.
        .append(
            GifDecoder.class, GifDecoder.class, UnitModelLoader.Factory.getInstance())
        .append(
            Registry.BUCKET_BITMAP,
            GifDecoder.class,
            Bitmap.class,
            new GifFrameResourceDecoder(bitmapPool))
        /* Drawables */
        .append(Uri.class, Drawable.class, resourceDrawableDecoder)
        .append(
            Uri.class, Bitmap.class, new ResourceBitmapDecoder(resourceDrawableDecoder, bitmapPool))
        /* Files */
        .register(new ByteBufferRewinder.Factory())
        .append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory())
        .append(File.class, InputStream.class, new FileLoader.StreamFactory())
        .append(File.class, File.class, new FileDecoder())
        .append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory())
        // Compilation with Gradle requires the type to be specified for UnitModelLoader here.
        .append(File.class, File.class, UnitModelLoader.Factory.getInstance())
        /* Models */
        .register(new InputStreamRewinder.Factory(arrayPool))
        .append(int.class, InputStream.class, resourceLoaderStreamFactory)
        .append(
            int.class,
            ParcelFileDescriptor.class,
            resourceLoaderFileDescriptorFactory)
        .append(Integer.class, InputStream.class, resourceLoaderStreamFactory)
        .append(
            Integer.class,
            ParcelFileDescriptor.class,
            resourceLoaderFileDescriptorFactory)
        .append(Integer.class, Uri.class, resourceLoaderUriFactory)
        .append(
            int.class,
            AssetFileDescriptor.class,
            resourceLoaderAssetFileDescriptorFactory)
        .append(
            Integer.class,
            AssetFileDescriptor.class,
            resourceLoaderAssetFileDescriptorFactory)
        .append(int.class, Uri.class, resourceLoaderUriFactory)
        .append(String.class, InputStream.class, new DataUrlLoader.StreamFactory())
        .append(String.class, InputStream.class, new StringLoader.StreamFactory())
        .append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
        .append(
            String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
        .append(Uri.class, InputStream.class, new HttpUriLoader.Factory())
        .append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()))
        .append(
            Uri.class,
            ParcelFileDescriptor.class,
            new AssetUriLoader.FileDescriptorFactory(context.getAssets()))
        .append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context))
        .append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context))
        .append(
            Uri.class,
            InputStream.class,
            new UriLoader.StreamFactory(contentResolver))
        .append(
            Uri.class,
            ParcelFileDescriptor.class,
             new UriLoader.FileDescriptorFactory(contentResolver))
        .append(
            Uri.class,
            AssetFileDescriptor.class,
            new UriLoader.AssetFileDescriptorFactory(contentResolver))
        .append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory())
        .append(URL.class, InputStream.class, new UrlLoader.StreamFactory())
        .append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context))
        .append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory())
        .append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory())
        .append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory())
        .append(Uri.class, Uri.class, UnitModelLoader.Factory.getInstance())
        .append(Drawable.class, Drawable.class, UnitModelLoader.Factory.getInstance())
        .append(Drawable.class, Drawable.class, new UnitDrawableDecoder())
        /* Transcoders */
        .register(
            Bitmap.class,
            BitmapDrawable.class,
            new BitmapDrawableTranscoder(resources))
        .register(Bitmap.class, byte[].class, bitmapBytesTranscoder)
        .register(
            Drawable.class,
            byte[].class,
            new DrawableBytesTranscoder(
                bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder))
        .register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);

    ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
    glideContext =
        new GlideContext(
            context,
            arrayPool,
            registry,
            imageViewTargetFactory,
            defaultRequestOptions,
            defaultTransitionOptions,
            engine,
            logLevel);
  }

  /**
   * Returns the {@link BitmapPool} used to
   * temporarily store {@link Bitmap}s so they can be reused to avoid garbage
   * collections.
   *
   * <p> Note - Using this pool directly can lead to undefined behavior and strange drawing errors.
   * Any {@link Bitmap} added to the pool must not be currently in use in any other
   * part of the application. Any {@link Bitmap} added to the pool must be removed
   * from the pool before it is added a second time. </p>
   *
   * <p> Note - To make effective use of the pool, any {@link Bitmap} removed from
   * the pool must eventually be re-added. Otherwise the pool will eventually empty and will not
   * serve any useful purpose. </p>
   *
   * <p> The primary reason this object is exposed is for use in custom
   * {@link ResourceDecoder}s and
   * {@link com.pasc.lib.glide.load.Transformation}s. Use outside of these classes is not generally
   * recommended. </p>
   */
  @NonNull
  public BitmapPool getBitmapPool() {
    return bitmapPool;
  }

  @NonNull
  public ArrayPool getArrayPool() {
    return arrayPool;
  }

  /**
   * @return The context associated with this instance.
   */
  @NonNull
  public Context getContext() {
    return glideContext.getBaseContext();
  }

  ConnectivityMonitorFactory getConnectivityMonitorFactory() {
    return connectivityMonitorFactory;
  }

  @NonNull
  GlideContext getGlideContext() {
    return glideContext;
  }

  /**
   * Pre-fills the {@link BitmapPool} using the given
   * sizes.
   *
   * <p> Enough Bitmaps are added to completely fill the pool, so most or all of the Bitmaps
   * currently in the pool will be evicted. Bitmaps are allocated according to the weights of the
   * given sizes, where each size gets (weight / prefillWeightSum) percent of the pool to fill.
   * </p>
   *
   * <p> Note - Pre-filling is done asynchronously using and
   * {@link android.os.MessageQueue.IdleHandler}. Any currently running pre-fill will be cancelled
   * and replaced by a call to this method. </p>
   *
   * <p> This method should be used with caution, overly aggressive pre-filling is substantially
   * worse than not pre-filling at all. Pre-filling should only be started in onCreate to avoid
   * constantly clearing and re-filling the
   * {@link BitmapPool}. Rotation should be carefully
   * considered as well. It may be worth calling this method only when no saved instance state
   * exists so that pre-filling only happens when the Activity is first created, rather than on
   * every rotation. </p>
   *
   * @param bitmapAttributeBuilders The list of
   * {@link PreFillType.Builder Builders} representing
   * individual sizes and configurations of {@link Bitmap}s to be pre-filled.
   */
  @SuppressWarnings("unused") // Public API
  public void preFillBitmapPool(@NonNull PreFillType.Builder... bitmapAttributeBuilders) {
    bitmapPreFiller.preFill(bitmapAttributeBuilders);
  }

  /**
   * Clears as much memory as possible.
   *
   * @see android.content.ComponentCallbacks#onLowMemory()
   * @see ComponentCallbacks2#onLowMemory()
   */
  public void clearMemory() {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be cleared before bitmap pool to clear re-pooled Bitmaps too. See #687.
    memoryCache.clearMemory();
    bitmapPool.clearMemory();
    arrayPool.clearMemory();
  }

  /**
   * Clears some memory with the exact amount depending on the given level.
   *
   * @see ComponentCallbacks2#onTrimMemory(int)
   */
  public void trimMemory(int level) {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
    memoryCache.trimMemory(level);
    bitmapPool.trimMemory(level);
    arrayPool.trimMemory(level);
  }

  /**
   * Clears disk cache.
   *
   * <p>
   *     This method should always be called on a background thread, since it is a blocking call.
   * </p>
   */
  // Public API.
  @SuppressWarnings({"unused", "WeakerAccess"})
  public void clearDiskCache() {
    Util.assertBackgroundThread();
    engine.clearDiskCache();
  }

  /**
   * Internal method.
   */
  @NonNull
  public RequestManagerRetriever getRequestManagerRetriever() {
    return requestManagerRetriever;
  }

  /**
   * Adjusts Glide's current and maximum memory usage based on the given {@link MemoryCategory}.
   *
   * <p> The default {@link MemoryCategory} is {@link MemoryCategory#NORMAL}.
   * {@link MemoryCategory#HIGH} increases Glide's maximum memory usage by up to 50% and
   * {@link MemoryCategory#LOW} decreases Glide's maximum memory usage by 50%. This method should be
   * used to temporarily increase or decrease memory usage for a single Activity or part of the app.
   * Use {@link GlideBuilder#setMemoryCache(MemoryCache)} to put a permanent memory size if you want
   * to change the default. </p>
   *
   * @return the previous MemoryCategory used by Glide.
   */
  @SuppressWarnings("WeakerAccess") // Public API
  @NonNull
  public MemoryCategory setMemoryCategory(@NonNull MemoryCategory memoryCategory) {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
    memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
    bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
    MemoryCategory oldCategory = this.memoryCategory;
    this.memoryCategory = memoryCategory;
    return oldCategory;
  }

  @NonNull
  private static RequestManagerRetriever getRetriever(@Nullable Context context) {
    // Context could be null for other reasons (ie the user passes in null), but in practice it will
    // only occur due to errors with the Fragment lifecycle.
    Preconditions.checkNotNull(
        context,
        "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
            + "returns null (which usually occurs when getActivity() is called before the Fragment "
            + "is attached or after the Fragment is destroyed).");
    return Glide.get(context).getRequestManagerRetriever();
  }

  /**
   * Begin a load with Glide by passing in a context.
   *
   * <p> Any requests started using a context will only have the application level options applied
   * and will not be started or stopped based on lifecycle events. In general, loads should be
   * started at the level the result will be used in. If the resource will be used in a view in a
   * child fragment, the load should be started with {@link #with(android.app.Fragment)}} using that
   * child fragment. Similarly, if the resource will be used in a view in the parent fragment, the
   * load should be started with {@link #with(android.app.Fragment)} using the parent fragment. In
   * the same vein, if the resource will be used in a view in an activity, the load should be
   * started with {@link #with(Activity)}}. </p>
   *
   * <p> This method is appropriate for resources that will be used outside of the normal fragment
   * or activity lifecycle (For example in services, or for notification thumbnails). </p>
   *
   * @param context Any context, will not be retained.
   * @return A RequestManager for the top level application that can be used to start a load.
   * @see #with(Activity)
   * @see #with(android.app.Fragment)
   * @see #with(Fragment)
   * @see #with(FragmentActivity)
   */
  @NonNull
  public static RequestManager with(@NonNull Context context) {
    return getRetriever(context).get(context);
  }

  /**
   * Begin a load with Glide that will be tied to the given {@link Activity}'s lifecycle
   * and that uses the given {@link Activity}'s default options.
   *
   * @param activity The activity to use.
   * @return A RequestManager for the given activity that can be used to start a load.
   */
  @NonNull
  public static RequestManager with(@NonNull Activity activity) {
    return getRetriever(activity).get(activity);
  }

  /**
   * Begin a load with Glide that will tied to the give
   * {@link FragmentActivity}'s lifecycle and that uses the given
   * {@link FragmentActivity}'s default options.
   *
   * @param activity The activity to use.
   * @return A RequestManager for the given FragmentActivity that can be used to start a load.
   */
  @NonNull
  public static RequestManager with(@NonNull FragmentActivity activity) {
    return getRetriever(activity).get(activity);
  }

  /**
   * Begin a load with Glide that will be tied to the given {@link android.app.Fragment}'s lifecycle
   * and that uses the given {@link android.app.Fragment}'s default options.
   *
   * @param fragment The fragment to use.
   * @return A RequestManager for the given Fragment that can be used to start a load.
   */
  @NonNull
  public static RequestManager with(@NonNull android.app.Fragment fragment) {
    return getRetriever(fragment.getActivity()).get(fragment);
  }

  /**
   * Begin a load with Glide that will be tied to the given
   * {@link Fragment}'s lifecycle and that uses the given
   * {@link Fragment}'s default options.
   *
   * @param fragment The fragment to use.
   * @return A RequestManager for the given Fragment that can be used to start a load.
   */
  @NonNull
  public static RequestManager with(@NonNull Fragment fragment) {
    return getRetriever(fragment.getActivity()).get(fragment);
  }

  /**
   * Begin a load with Glide that will be tied to the lifecycle of the {@link Fragment},
   * {@link android.app.Fragment}, or {@link Activity} that contains the View.
   *
   * <p>A {@link Fragment} or {@link android.app.Fragment} is assumed to contain a View if the View
   * is a child of the View returned by the {@link Fragment#getView()}} method.
   *
   * <p>This method will not work if the View is not attached. Prefer the Activity and Fragment
   * variants unless you're loading in a View subclass.
   *
   * <p>This method may be inefficient aways and is definitely inefficient for large hierarchies.
   * Consider memoizing the result after the View is attached or again, prefer the Activity and
   * Fragment variants whenever possible.
   *
   * <p>When used in Applications that use the non-support {@link android.app.Fragment} classes,
   * calling this method will produce noisy logs from {@link android.app.FragmentManager}. Consider
   * avoiding entirely or using the {@link Fragment}s from the support library instead.
   *
   * <p>If the support {@link FragmentActivity} class is used, this method will only attempt to
   * discover support {@link Fragment}s. Any non-support {@link android.app.Fragment}s attached
   * to the {@link FragmentActivity} will be ignored.
   *
   * @param view The view to search for a containing Fragment or Activity from.
   * @return A RequestManager that can be used to start a load.
   */
  @NonNull
  public static RequestManager with(@NonNull View view) {
    return getRetriever(view.getContext()).get(view);
  }

  @NonNull
  public Registry getRegistry() {
    return registry;
  }

  boolean removeFromManagers(@NonNull Target<?> target) {
    synchronized (managers) {
      for (RequestManager requestManager : managers) {
        if (requestManager.untrack(target)) {
          return true;
        }
      }
    }

    return false;
  }

  void registerRequestManager(RequestManager requestManager) {
    synchronized (managers) {
      if (managers.contains(requestManager)) {
        throw new IllegalStateException("Cannot register already registered manager");
      }
      managers.add(requestManager);
    }
  }

  void unregisterRequestManager(RequestManager requestManager) {
    synchronized (managers) {
      if (!managers.contains(requestManager)) {
        throw new IllegalStateException("Cannot unregister not yet registered manager");
      }
      managers.remove(requestManager);
    }
  }

  @Override
  public void onTrimMemory(int level) {
    trimMemory(level);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    // Do nothing.
  }

  @Override
  public void onLowMemory() {
    clearMemory();
  }
}
