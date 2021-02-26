package com.hunglv.testdocuments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hunglv.office.common.IOfficeToPicture
import com.hunglv.office.common.IOfficeToPicture.VIEW_CHANGE_END
import com.hunglv.office.constant.EventConstant
import com.hunglv.office.constant.MainConstant
import com.hunglv.office.constant.wp.WPViewConstant
import com.hunglv.office.java.awt.Color
import com.hunglv.office.macro.DialogListener
import com.hunglv.office.res.ResKit
import com.hunglv.office.ss.sheetbar.SheetBar
import com.hunglv.office.system.IControl
import com.hunglv.office.system.IMainFrame
import com.hunglv.office.system.MainControl
import com.hunglv.office.system.beans.pagelist.IPageListViewListener
import com.hunglv.testdocuments.databinding.ActivityFilesViewBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FilesViewActivity : AppCompatActivity(), IMainFrame {
    private lateinit var binding: ActivityFilesViewBinding

    //
    private var appFrame: LinearLayout? = null
    private var applicationType: Byte = -1
    private var bg = Color.BLACK
    private var bottomBar: SheetBar? = null
    private var control: MainControl? = null
    private var fileName: String? = null
    private var filePath: String? = null
    private var gapView: View? = null
    private var isDispose = false
    private var isFromAppActivity = false
    private var isThumb = false
    private var tempFilePath: String? = null
    private var toast: Toast? = null

    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            layoutToolbar.toolBar.title = ""
            setSupportActionBar(layoutToolbar.toolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        this.control = MainControl(this)
        this.appFrame = binding.appFrame

        if (intent != null) {
            this.filePath = intent.getStringExtra("path")
            this.fileName = intent.getStringExtra("name")
            this.isFromAppActivity = intent.getBooleanExtra("fromAppActivity", false)
            binding.layoutToolbar.toolBarTitle.text = this.fileName
        }
        this.toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        createView()
        this.control?.apply {
            openFile(this@FilesViewActivity.filePath)
            setOffictToPicture(object : IOfficeToPicture {
                private var bitmap: Bitmap? = null
                override fun setModeType(modeType: Byte) {

                }

                override fun getModeType(): Byte {
                    return VIEW_CHANGE_END
                }

                override fun getBitmap(
                    visibleWidth: Int,
                    visibleHeight: Int
                ): Bitmap? {
                    if (visibleWidth == 0 || visibleHeight == 0) {
                        return null
                    }
                    if (bitmap == null
                        || bitmap!!.width != visibleWidth
                        || bitmap!!.height != visibleHeight
                    ) {
                        // custom picture size
                        bitmap?.recycle()
                        // 
                        bitmap = createBitmap(
                            (visibleWidth),
                            (visibleHeight),
                            Bitmap.Config.ARGB_8888
                        )
                    }
                    return bitmap
                }

                override fun callBack(bitmap: Bitmap?) {
                    this@FilesViewActivity.saveBitmapToFile(bitmap)
                }

                override fun isZoom(): Boolean {
                    return false
                }

                override fun dispose() {

                }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
            R.id.ic_full_screen_view -> {
            //                hideSystemUI()
            }
            R.id.ic_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                val parse: Uri = Uri.parse(filePath)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_STREAM, parse)
                startActivity(
                    Intent.createChooser(
                        intent,
                        "Share file: "
                    )
                )

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveBitmapToFile(bitmap: Bitmap?) {
        if (bitmap == null) {
            return
        }
        if (tempFilePath == null) {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                tempFilePath = Environment.getExternalStorageDirectory().absolutePath
            }
            val file = File(tempFilePath + File.separatorChar + "tempPic")
            if (!file.exists()) {
                file.mkdir()
            }
            tempFilePath = file.absolutePath
        }
        val file = File(tempFilePath + File.separatorChar + "export_image.jpg")
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
        } finally {
            //bitmap.recycle();
        }
    }

    override fun onBackPressed() {
        val actionValue = this.control?.getActionValue(EventConstant.PG_SLIDESHOW, null)
        if (actionValue == null || !(actionValue as Boolean)){
                this.control?.reader?.abortReader()
            val mainControl = this.control
            if (mainControl == null || !mainControl?.isAutoTest){
                if (this.isFromAppActivity){
                    startActivity(Intent(this, MainActivity::class.java))
                }
                super.onBackPressed()
                return
            }
        }
        fullScreen(false)
        this.control?.actionEvent(EventConstant.PG_SLIDESHOW_END, null)
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    fun getDialogListener(): DialogListener? {
        return null
    }

    private fun createView(){
        val file = this.filePath?.toLowerCase(Locale.getDefault()) ?: return

        if (file.endsWith(MainConstant.FILE_TYPE_DOC) || file.endsWith(MainConstant.FILE_TYPE_DOCX)
            || file.endsWith(MainConstant.FILE_TYPE_TXT)
            || file.endsWith(MainConstant.FILE_TYPE_DOT)
            || file.endsWith(MainConstant.FILE_TYPE_DOTX)
            || file.endsWith(MainConstant.FILE_TYPE_DOTM)
        ) {
            this.applicationType = MainConstant.APPLICATION_TYPE_WP
        } else if (file.endsWith(MainConstant.FILE_TYPE_XLS)
            || file.endsWith(MainConstant.FILE_TYPE_XLSX)
            || file.endsWith(MainConstant.FILE_TYPE_XLT)
            || file.endsWith(MainConstant.FILE_TYPE_XLTX)
            || file.endsWith(MainConstant.FILE_TYPE_XLTM)
            || file.endsWith(MainConstant.FILE_TYPE_XLSM)
        ) {
            applicationType = MainConstant.APPLICATION_TYPE_SS
        } else if (file.endsWith(MainConstant.FILE_TYPE_PPT)
            || file.endsWith(MainConstant.FILE_TYPE_PPTX)
            || file.endsWith(MainConstant.FILE_TYPE_POT)
            || file.endsWith(MainConstant.FILE_TYPE_PPTM)
            || file.endsWith(MainConstant.FILE_TYPE_POTX)
            || file.endsWith(MainConstant.FILE_TYPE_POTM)
        ) {
            applicationType = MainConstant.APPLICATION_TYPE_PPT
        } else if (file.endsWith(MainConstant.FILE_TYPE_PDF)) {
            applicationType = MainConstant.APPLICATION_TYPE_PDF
        } else {
            applicationType = MainConstant.APPLICATION_TYPE_WP
        }
    }

    private fun fileShare(){
        if (this.filePath == null) return
        val arrayList = ArrayList<Uri>()
        arrayList.add(Uri.fromFile(File(this.filePath!!)))
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.putExtra(Intent.EXTRA_STREAM, arrayList)
        intent.type = "application/octet-stream"
        startActivity(Intent.createChooser(intent, "Share with: "))
    }

    override fun onCreateDialog(id: Int): Dialog? {
        return this.control?.getDialog(this, id)
    }

    // region override method
    override fun changeZoom() {

    }

    override fun changePage() {

    }

    override fun completeLayout() {

    }

    override fun error(errorCode: Int) {

    }

    override fun fullScreen(fullscreen: Boolean) {

    }

    override fun getActivity(): Activity {
        return this
    }

    override fun getPageListViewMovingPosition(): Byte {
        return IPageListViewListener.Moving_Horizontal
    }

    override fun getTXTDefaultEncode(): String {
        return "GBK"
    }

    override fun getTopBarHeight(): Int {
        return 0
    }

    override fun getWordDefaultView(): Byte {
        return WPViewConstant.PAGE_ROOT.toByte()
    }

    override fun isChangePage(): Boolean {
        return true
    }

    override fun isDrawPageNumber(): Boolean {
        return true
    }

    override fun isIgnoreOriginalSize(): Boolean {
        return false
    }

    override fun isPopUpErrorDlg(): Boolean {
        return true
    }

    override fun isShowFindDlg(): Boolean {
        return true
    }

    override fun isShowPasswordDlg(): Boolean {
        return true
    }

    override fun isShowProgressBar(): Boolean {
        return true
    }

    override fun isShowTXTEncodeDlg(): Boolean {
        return true
    }

    override fun isShowZoomingMsg(): Boolean {
        return true
    }

    override fun isTouchZoom(): Boolean {
        return true
    }

    override fun isZoomAfterLayoutForWord(): Boolean {
        return true
    }

    fun onCurrentPageChange() {}

    override fun onEventMethod(
        v: View?,
        e1: MotionEvent?,
        e2: MotionEvent?,
        xValue: Float,
        yValue: Float,
        eventMethodType: Byte
    ): Boolean {
        return false
    }

    fun onPagesCountChange() {}

    override fun setFindBackForwardState(state: Boolean) {

    }

    override fun setIgnoreOriginalSize(ignoreOriginalSize: Boolean) {

    }

    override fun updateViewImages(viewList: MutableList<Int>?) {

    }

    override fun showProgressBar(visible: Boolean) {
        setProgressBarIndeterminate(visible)
    }

    override fun updateToolsbarStatus() {
        if (appFrame == null || isDispose) {
            return
        }
        val count = appFrame!!.childCount
        for (i in 0 until count) {
            val v = appFrame!!.getChildAt(i)
//            if (v is AToolsbar) {
//                v.updateStatus()
//            }
        }
    }

    fun getControl(): IControl? {
        return control
    }

    fun getApplicationType(): Int {
        return applicationType.toInt()
    }

    fun getFilePath(): String? {
        return filePath
    }

    override fun doActionEvent(actionID: Int, obj: Any?): Boolean {
        try {
            when (actionID) {
                EventConstant.SYS_ONBACK_ID -> onBackPressed()
                EventConstant.SYS_RESET_TITLE_ID -> title = obj as String?
                EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS -> updateToolsbarStatus()
                EventConstant.APP_SHARE_ID -> fileShare()
                EventConstant.APP_FINDING -> {
                    val trim = (obj as String).trim()
                    if (trim.isEmpty() || !control!!.find.find(trim)) {
                        setFindBackForwardState(false)
                        toast!!.setText(getLocalString("DIALOG_FIND_NOT_FOUND"))
                        toast!!.show()
                    } else {
                        setFindBackForwardState(true)
                    }
                }
                EventConstant.SS_CHANGE_SHEET -> bottomBar!!.setFocusSheetButton((obj as Int? ?: 0))
                EventConstant.APP_DRAW_ID -> {
                    control?.getSysKit()?.calloutManager?.drawingMode =
                        MainConstant.DRAWMODE_CALLOUTDRAW
                    appFrame?.post {
                        control?.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null)
                    }
                }
                EventConstant.APP_BACK_ID -> {
                    control?.getSysKit()?.calloutManager?.drawingMode = MainConstant.DRAWMODE_NORMAL
                }
                EventConstant.APP_PEN_ID -> if (obj as Boolean) {
                    control?.getSysKit()?.calloutManager?.drawingMode =
                        MainConstant.DRAWMODE_CALLOUTDRAW
                    appFrame?.post {
                        control?.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null)
                    }
                } else {
                    control?.getSysKit()?.calloutManager?.drawingMode = MainConstant.DRAWMODE_NORMAL
                }
                EventConstant.APP_ERASER_ID -> if (obj as Boolean) {
                    control?.getSysKit()?.calloutManager?.drawingMode =
                        MainConstant.DRAWMODE_CALLOUTERASE
                } else {
                    control?.getSysKit()?.calloutManager?.drawingMode = MainConstant.DRAWMODE_NORMAL
                }
                /*EventConstant.SYS_HELP_ID -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            resources
                                .getString(R.string.sys_url_wxiwei)
                        )
                    )
                    startActivity(intent)
                }
                EventConstant.APP_FIND_ID -> showSearchBar(true)
                EventConstant.FILE_MARK_STAR_ID -> markFile()
                EventConstant.APP_FIND_BACKWARD -> if (!control!!.find.findBackward()) {
                    searchBar.setEnabled(EventConstant.APP_FIND_BACKWARD, false)
                    toast!!.setText(getLocalString("DIALOG_FIND_TO_BEGIN"))
                    toast!!.show()
                } else {
                    searchBar.setEnabled(EventConstant.APP_FIND_FORWARD, true)
                }
                EventConstant.APP_FIND_FORWARD -> if (!control!!.find.findForward()) {
                    searchBar.setEnabled(EventConstant.APP_FIND_FORWARD, false)
                    toast!!.setText(getLocalString("DIALOG_FIND_TO_END"))
                    toast!!.show()
                } else {
                    searchBar.setEnabled(EventConstant.APP_FIND_BACKWARD, true)
                }
                EventConstant.APP_COLOR_ID -> {
                    val dlg = ColorPickerDialog(this, control)
                    dlg.show()
                    dlg.setOnDismissListener(DialogInterface.OnDismissListener {
                        setButtonEnabled(
                            true
                        )
                    })
                    setButtonEnabled(false)
                }*/
                else -> return false
            }
        } catch (e: Exception) {
            control!!.getSysKit().errorKit.writerLog(e)
        }
        return true
    }

    override fun openFileFinish() {
        gapView = View(applicationContext)
        gapView!!.setBackgroundColor(android.graphics.Color.GRAY)
        appFrame?.addView(
            gapView,
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
        )
        val app = control?.view
        if (app != null) {
            appFrame?.addView(
                app,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun getBottomBarHeight(): Int {
        val sheetBar = this.bottomBar
        if (sheetBar != null){
            return sheetBar.sheetbarHeight
        }
        return 0
    }

    override fun getAppName(): String {
        return getString(R.string.app_name)
    }

    fun destroyEngine() {
        super.onBackPressed()
    }

    override fun getLocalString(resName: String?): String {
        return ResKit.instance().getLocalString(resName)
    }

    override fun setWriteLog(saveLog: Boolean) {
        this.isWriteLog = saveLog
    }

    override fun isWriteLog(): Boolean {
        return this.isWriteLog
    }

    override fun setThumbnail(isThumbnail: Boolean) {
        this.isThumb = isThumbnail
    }

    override fun getViewBackground(): Any {
        return this.bg
    }

    override fun isThumbnail(): Boolean {
        return this.isThumb
    }

    override fun getTemporaryDirectory(): File {
        return getExternalFilesDir(null) ?: filesDir
    }

    override fun dispose() {
        this.isDispose = true
        this.control?.dispose()
        this.control = null

        this.bottomBar = null
        if (this.appFrame != null){
            val childCount = this.appFrame!!.childCount
            for (i in 0 until childCount){
                this.appFrame!!.getChildAt(i)
            }
            this.appFrame = null
        }
    }
    // endregion
}
    
