package com.hunglv.testdocuments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hunglv.office.constant.MainConstant
import com.hunglv.office.officereader.AppActivity
import com.hunglv.office.system.FileKit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (haveStorage()) {
//            navigateToDocumentViewer()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }

    }

    private fun haveStorage() =
        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (haveStorage()) {
//            navigateToDocumentViewer()
        }
    }

    private fun navigateToDocumentViewer() {

//            val intent = Intent()
//            intent.setClass(this, AppActivity::class.java)
//            intent.putExtra(MainConstant.INTENT_FILED_FILE_PATH, "/storage/emulated/0/Zip/test3.docx")
//            startActivityForResult(intent, RESULT_FIRST_USER)


    }

    fun onSelectedFile(view: View) {
        if (view.id == R.id.btn_test6) {
            intent = Intent(this, AppActivity::class.java)
            intent.putExtra(
                MainConstant.INTENT_FILED_FILE_PATH,
                "/storage/emulated/0/Zip/test3.docx"
            )
            startActivityForResult(intent, RESULT_FIRST_USER)
        } else {
            val intent = Intent(this, FilesViewActivity::class.java)
            when (view.id) {
                R.id.btn_test1 -> {
                    intent.putExtra("path", "/storage/emulated/0/Zip/test1.xls")
                    intent.putExtra("name", "test1.xls")
                }
                R.id.btn_test2 -> {
                    intent.putExtra("path", "/storage/emulated/0/Zip/test2.pptx")
                    intent.putExtra("name", "test2.pptx")
                }
                R.id.btn_test3 -> {
                    intent.putExtra("path", "/storage/emulated/0/Zip/test3.docx")
                    intent.putExtra("name", "test3.docx")
                }
                R.id.btn_test4 -> {
                    intent.putExtra("path", "/storage/emulated/0/Zip/test4.xlsx")
                    intent.putExtra("name", "test4.xlsx")
                }
                R.id.btn_test5 -> {
                    intent.putExtra("path", "/storage/emulated/0/Zip/test5.doc")
                    intent.putExtra("name", "test5.doc")
                }
            }
            intent.putExtra("fromConverterApp", false)
            intent.putExtra("fromAppActivity", true)
            startActivity(intent)
        }
    }

}