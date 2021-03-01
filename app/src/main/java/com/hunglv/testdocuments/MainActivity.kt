package com.hunglv.testdocuments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
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

        val text = findViewById<TextView>(R.id.text)
        text?.setOnClickListener {
            navigateToDocumentViewer()
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

        val intent = Intent(this, FilesViewActivity::class.java)
        intent.putExtra("path", "/storage/emulated/0/Zip/test3.docx")
        intent.putExtra("name", "test3.docx")
        intent.putExtra("fromConverterApp", false)
        intent.putExtra("fileType", MainConstant.FILE_TYPE_DOCX + "")
        intent.putExtra("fromAppActivity", true)

        startActivity(intent)
    }


}