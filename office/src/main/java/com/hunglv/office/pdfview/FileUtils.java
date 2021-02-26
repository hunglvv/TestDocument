package com.hunglv.office.pdfview;

import android.content.Context;

import com.hunglv.office.fc.openxml4j.opc.PackagingURIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlin.io.ByteStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class FileUtils {
    public static final FileUtils INSTANCE = new FileUtils();

    private FileUtils(){

    }

    public final File fileFromAsset(Context context, String str) throws IOException{

        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "assetFileName");
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, str + "-pdfview.pdf");
        if (StringsKt.contains(str, PackagingURIHelper.FORWARD_SLASH_STRING, false)) {
            file.getParentFile().mkdirs();
        }
        InputStream open = context.getAssets().open(str);
        Intrinsics.checkExpressionValueIsNotNull(open, "context.assets.open(assetFileName)");
        ByteStreamsKt.copyTo(open, new FileOutputStream(file),  8 * 1024);
        return file;

    }
}
