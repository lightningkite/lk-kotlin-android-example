package com.lightningkite.kotlin.android.example.image.loading

import android.graphics.Color
import android.util.Log
import android.view.View
import com.lightningkite.kotlin.android.example.R
import lk.android.activity.access.ActivityAccess
import lk.android.image.loading.image.lambdaBitmap
import lk.android.mighty.view.ViewGenerator
import lk.android.ui.thread.UIThread
import lk.anko.extensions.anko
import lk.kotlin.jvm.utils.async.Async
import lk.kotlin.jvm.utils.async.invokeOn
import lk.kotlin.jvm.utils.random.random
import lk.kotlin.okhttp.thenOnFailure
import lk.kotlin.okhttp.thenOnSuccess
import okhttp3.Request
import org.jetbrains.anko.*

class ImageLoadingVG() : ViewGenerator {

    override fun invoke(access: ActivityAccess): View = access.context.anko().scrollView {

        isFillViewport = true

        verticalLayout {

            textView {
                textResource = R.string.lets_load_an_image
            }.lparams(matchParent, wrapContent) { margin = dip(8) }

            imageView {
                backgroundColor = Color.LTGRAY
                Request.Builder()
                        .url("http://picsum.photos/${(200..600).random()}/${(200..600).random()}")
                        .lambdaBitmap()
                        .thenOnSuccess(UIThread) {
                            setImageBitmap(it)
                        }
                        .thenOnFailure(UIThread) {
                            Log.e("ImageLoadingVG", "It failed to load. $it")
                        }
                        .invokeOn(Async)
            }.lparams(matchParent, dip(300))

        }.lparams(matchParent, wrapContent)
    }
}