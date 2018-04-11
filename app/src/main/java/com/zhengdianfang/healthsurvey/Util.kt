package com.zhengdianfang.healthsurvey

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.support.v4.content.FileProvider
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.Question
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.defaultSharedPreferences
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern


/**
 * Created by dfgzheng on 05/04/2018.
 */
object Util: AnkoLogger {

    const val SELECT_PHOTO = 0x000001
    const val OPEN_CAMERA = 0x000002
    /**
     * 手机号号段校验，
     * 第1位：1；
     * 第2位：{3、4、5、6、7、8}任意数字；
     * 第3—11位：0—9任意数字
     * @param value
     * @return
     */
    fun isTelPhoneNumber(value: String?): Boolean {
        if (value != null && value.length == 11) {
            val pattern = Pattern.compile("^1[3|4|5|6|7|8][0-9]\\d{8}$")
            val matcher = pattern.matcher(value)
            return matcher.matches()
        }
        return false
    }


    fun getUnquieid(phone: String): String {
        val time = System.currentTimeMillis().toString().substring(0, 10)
        return Des4.encode(phone + time)
    }

    fun saveQuestionCache(context: Context?, quetions: MutableList<Question>?) {

        val edit = context?.defaultSharedPreferences?.edit()
        quetions?.forEach {
            edit?.putString(it.qid, WebService.gson.toJson(it))
        }
        edit?.apply()
    }


    fun getIntentCaptureCompat(context: Context, file: File): Intent {
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val mUri = getUriFromFile(context, file)
        mIntent.addCategory(Intent.CATEGORY_DEFAULT)
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        return mIntent
    }

    fun getIntentImageChooser(): Intent {
        val i = Intent()
        i.action = Intent.ACTION_GET_CONTENT
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return i
    }

    fun compressBitmap(file: File, sizeLimit: Long) {
        val baos = ByteArrayOutputStream()
        var quality = 100
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().size / 1024 > sizeLimit) {
            // 清空baos
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            quality -= 10
        }

        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(baos.toByteArray())
        fileOutputStream.flush()
        debug("compress file size ${file.length()}")
    }

    private fun getUriFromFile(context: Context, file: File): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getUriFromFileForN(context, file)
        } else {
            Uri.fromFile(file)
        }
    }

    private fun getUriFromFileForN(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
    }

    fun getTakePhotoFilePath(context: Context): File {
        return File("${context.cacheDir.absolutePath}${File.separator}${System.currentTimeMillis()}.jpg")
    }


    @TargetApi(19)
    fun getFileAbsolutePath(context: Context?, fileUri: Uri?): String? {

        if (context == null || fileUri == null)
            return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(fileUri)) {
                val id = DocumentsContract.getDocumentId(fileUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(fileUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(fileUri)) fileUri.lastPathSegment else getDataColumn(context, fileUri, null, null)
        } else if ("file".equals(fileUri.scheme, ignoreCase = true)) {
            return fileUri.path
        }// File
        return null
    }

    fun getRealPathBelowVersion(context: Context?, uri: Uri?): String {
        var filePath = ""
        if (context != null && uri != null) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)

            val loader = CursorLoader(context, uri, projection, null, null, null)
            val cursor = loader.loadInBackground()

            if (cursor != null) {
                cursor.moveToFirst()
                filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
                cursor.close()
            }
            if (filePath == null) {
                filePath = uri.path

            }
        }
        return filePath
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}
