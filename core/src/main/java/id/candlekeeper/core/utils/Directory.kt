package id.candlekeeper.core.utils

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import java.io.File

fun destinationDownload(context: Context): File {
    val result = File(context.getExternalFilesDir("")?.absolutePath, "/file.zip")
    Log.d("destinationDownload", result.absolutePath)
    return result
}

fun destinationUnzip(context: Context, prefManager: PrefManager): String {
    //dicoba bikin path sendiri di aplikasi
    val packageSkin = prefManager.spPackageNameFF + "/files/dragon2017/assets/"

    return if (isAboveAndroid11()) {
        context.getExternalFilesDir("")!!.absolutePath
    } else {
        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/"
    }
}

fun uriPermission(operation: Int): Uri? {
    return when (operation) {
        1 -> {
            return DocumentsContract.buildDocumentUri(
                "com.android.externalstorage.documents",
                "primary:Android/data"
            )
        }
        2 -> {
            return DocumentsContract.buildTreeDocumentUri(
                "com.android.externalstorage.documents",
                "primary:Android/data"
            )
        }
        else -> {
            null
        }
    }
}

fun docFileOperation(operation: Int, application: Application, prefManager: PrefManager): DocumentFile? {
    return when (operation) {
        1 -> {
            DocumentFile.fromTreeUri(
                application,
                "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fid.candlekeeper.skinpackff%2Ffiles%2F${prefManager.spPackageNameFF}".toUri()
            )
        }
        2 -> {
            DocumentFile.fromTreeUri(
                application,
                "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata".toUri()
            )
        }
        else -> {
            null
        }
    }
}