package com.tonyyang.common.itemselector

import android.content.Context
import android.net.Uri
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import java.io.InputStream
import android.support.v7.content.res.AppCompatResources
import com.tonyyang.common.itemselector.util.ImageUtils

/**
 * @author tonyyang
 */
class CustomImageDownloader(context: Context): BaseImageDownloader(context) {

    companion object {
        const val URL_SCHEMA_ANDROID_RESOURCE = "android.resource"
    }

    override fun getStreamFromOtherSource(imageUri: String?, extra: Any?): InputStream? {
        val context = CoreApplication.getContext()
        val uri = Uri.parse(imageUri)
        if (URL_SCHEMA_ANDROID_RESOURCE == uri.scheme) {
            val uriSplit = uri.toString().split("/")
            val resourceName = uriSplit[uriSplit.lastIndex]
            val resourceId = context.resources.getIdentifier(
                resourceName, "drawable", context.packageName)
            val drawable = AppCompatResources.getDrawable(context, resourceId)
            return ImageUtils.drawable2InputStream(drawable)
        }
        return super.getStreamFromOtherSource(imageUri, extra)
    }
}