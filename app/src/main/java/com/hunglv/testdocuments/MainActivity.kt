package com.hunglv.testdocuments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hunglv.office.constant.MainConstant

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val intent = Intent(this, FilesViewActivity::class.java)
        intent.putExtra("path", "")
        intent.putExtra("name", "")
        intent.putExtra("fromConverterApp", false)
        intent.putExtra("fileType", MainConstant.FILE_TYPE_DOC + "")
        intent.putExtra("fromAppActivity", true)

        startActivity(intent)


    }
}