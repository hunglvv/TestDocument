package com.hunglv.office.pdfview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File

class PDFView : SubsamplingScaleImageView {
    private var hashMap: HashMap<Int, View>? = null
    private var mScale = 1.0f
    private var mFile: File? = null


    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.mScale = 8.0f
        setMinimumTileDpi(120)
        setMinimumScaleType(4)
    }

    constructor(context: Context?) : super(context) {
        this.mScale = 8.0f
        setMinimumTileDpi(120)
        setMinimumScaleType(4)
    }

    fun getFile() = mFile

    fun getmScale() = mScale

    fun clearHashMap() {
        hashMap?.clear()
    }

    fun getView(i: Int): View? {
        if (hashMap == null) {
            hashMap = HashMap()
        }
        val view = hashMap!![i]

        if (view != null) {
            return view
        }
        val someView = view?.findViewById<View>(i)
        if (someView != null) {
            hashMap!![i] = someView
        }
        return someView
    }

    fun fromAsset(str: String): PDFView{
        val fileUtils = FileUtils.INSTANCE
        this.mFile = fileUtils.fileFromAsset(context, str)
        return this
    }

    fun fromFile(file: File): PDFView{
        this.mFile = file
        return this
    }

    fun fromFile(str: String): PDFView{
        this.mFile = File(str)
        return this
    }

    fun show(){
        if (mFile == null) return
        val uri = ImageSource.uri(mFile!!.absolutePath)
        setRegionDecoderFactory(ShowPDFView(this))
        setImage(uri)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycle()
    }

}