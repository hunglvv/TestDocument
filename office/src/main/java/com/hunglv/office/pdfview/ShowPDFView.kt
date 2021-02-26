package com.hunglv.office.pdfview

import com.davemorrissey.labs.subscaleview.decoder.DecoderFactory
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder

class ShowPDFView constructor(pdfView: PDFView): DecoderFactory<ImageRegionDecoder> {
    private val mPdfView = pdfView
    override fun make(): ImageRegionDecoder {
        if (mPdfView.getFile() == null){
            throw NullPointerException()
        }
        return PDFRegionDecoder(mPdfView, mPdfView.getFile(), mPdfView.getmScale(),0,8)
    }
}