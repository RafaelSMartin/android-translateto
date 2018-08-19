package com.ticktalk.translateto.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

/**
 * Created by indogroup on 12/20/17.
 */

public class AppUtilImpl implements AppUtil {

    private Context context;

    public AppUtilImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getFileNameFromUri(String uriPath) {
        String result = null;
        Uri uri = Uri.parse(uriPath);
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
