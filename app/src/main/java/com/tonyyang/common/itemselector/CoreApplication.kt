package com.tonyyang.common.itemselector

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder
import com.nostra13.universalimageloader.utils.StorageUtils
import com.orhanobut.logger.Logger
import com.orhanobut.logger.AndroidLogAdapter


class CoreApplication : Application() {

    companion object {

        private lateinit var mInstance: CoreApplication

        fun getContext(): Context {
            return mInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Logger.addLogAdapter(AndroidLogAdapter())
        Stetho.initializeWithDefaults(this)
        val cacheDir = StorageUtils.getCacheDirectory(this)
        val config = ImageLoaderConfiguration.Builder(this)
            .memoryCacheExtraOptions(480, 800) // 默认的设备屏幕尺寸
            .diskCacheExtraOptions(480, 800, null)
            .threadPoolSize(3) // 默认的线程池尺寸
            .threadPriority(Thread.NORM_PRIORITY - 2) // 默认的线程优先级为 3
            .tasksProcessingOrder(QueueProcessingType.FIFO) // 默认的任务处理顺序为先进先出
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024)
            .memoryCacheSizePercentage(13) // 默认的内存缓存大小占比为 13
            .diskCache(UnlimitedDiskCache(cacheDir)) // 默认的磁盘缓存
            .diskCacheSize(50 * 1024 * 1024)
            .diskCacheFileCount(100)
            .diskCacheFileNameGenerator(HashCodeFileNameGenerator()) // 默认的磁盘缓存文件名生成器
            .imageDownloader(CustomImageDownloader(this)) // 默认的图像下载器
            .imageDecoder(BaseImageDecoder(false)) // 默认的图像解码器
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // 默认的展示图像选项
            .writeDebugLogs()
            .build()
        ImageLoader.getInstance().init(config)
    }
}