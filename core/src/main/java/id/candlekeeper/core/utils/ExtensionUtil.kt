package id.candlekeeper.core.utils

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toMultipartForm(): RequestBody {
    return this.toRequestBody(MultipartBody.FORM)
}