package com.azhar.poiandroid

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.poi.xslf.usermodel.SlideLayout
import org.apache.poi.xslf.usermodel.XMLSlideShow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //permission
        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions,0)

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        btnDocx.setOnClickListener {
            createDocx(path, inputText.text.toString().trim())
        }
        btnXlsx.setOnClickListener{
            createXlsx(path, inputText.text.toString().trim())
        }
        btnPptx.setOnClickListener{
            createPptx(path, inputText.text.toString().trim())
        }
    }

    private fun createDocx(path: File, message: String) {
        try {
            val document = XWPFDocument()

            val outputStream = FileOutputStream(File(path,"/poi.docx"))

            val paragraph = document.createParagraph()
            val run = paragraph.createRun()
            run.setText(message)

            document.write(outputStream)
            outputStream.close()
            Toast.makeText(this@MainActivity,
                    "poi.docx was successfully created", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun createXlsx(path: File, message: String) {
        try {
            val workbook = XSSFWorkbook()

            val outputStream = FileOutputStream(File(path, "/poi.xlsx"))

            val sheet = workbook.createSheet("Sheet 1")
            val row = sheet.createRow(2)
            val cell = row.createCell(1)
            cell.setCellValue(message)

            workbook.write(outputStream)
            outputStream.close()
            Toast.makeText(this@MainActivity,
                    "poi.xlsx was successfully created", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun createPptx(path: File, message: String) {
        try {
            val slideShow = XMLSlideShow()

            val outputStream = FileOutputStream(File(path, "/poi.pptx"))

            val slideMaster = slideShow.slideMasters[0]
            val titleLayout = slideMaster.getLayout(SlideLayout.TITLE)
            val slide = slideShow.createSlide(titleLayout)
            val title = slide.getPlaceholder(0)
            title.text = message

            slideShow.write(outputStream)
            outputStream.close()
            Toast.makeText(this@MainActivity,
                    "poi.pptx was successfully created", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}