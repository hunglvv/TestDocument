package com.hunglv.office.pdfview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder
import java.io.File

class PDFRegionDecoder : ImageRegionDecoder {
    private var backgroundColorPdf = Color.TRANSPARENT
    private var view: PDFView? = null
    private var scale = 0f
    private var file: File? = null

    constructor(pdfView: PDFView, file2: File?, f: Float, i: Int) {
        this.view = pdfView
        this.file = file2
        this.scale = f
        this.backgroundColorPdf = i
    }

    constructor(pdfView: PDFView, file2: File?, f: Float, i: Int, i2: Int){
        this.view = pdfView
        this.file = file2
        this.scale = f
        this.backgroundColorPdf =  if (i2 and 8 != 0) -1 else i
    }

    override fun init(context: Context?, uri: Uri): Point {
        return Point()
    }

    override fun decodeRegion(sRect: Rect, sampleSize: Int): Bitmap {
        return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun recycle() {

    }
}