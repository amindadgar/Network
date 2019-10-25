package com.amindadgar.internetconnection

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.textColor
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        responseCodeText.movementMethod = ScrollingMovementMethod()

        button.setOnClickListener { // click the button to show http status code
            responseCodeText.text=""
            val mText:String = input_url.text.toString()
            if (mText.contains("http"))
                openConnection(URL(mText))
            else{
                responseCodeText.textColor = Color.RED
                responseCodeText.text = "Specify a Protocol!"

            }

        }
        showUrlButton.setOnClickListener {
            val mText:String = input_url.text.toString()
            val intent = Intent(this,WebActivity::class.java)
            intent.putExtra("myUrl","https://$mText")
            startActivity(intent)
        }
    }


    fun openConnection(url:URL){
        doAsync {
            val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                httpConnection.requestMethod = "GET"
                httpConnection.readTimeout = 30 * 1000
                if (httpConnection.responseCode == 200) {
                    val inputStream: InputStream = httpConnection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
//                    val bufferedReader = BufferedReader(inputStreamReader)
//                    Log.d("aminConnection", bufferedReader.readLine())
//                    Log.d("AminConnectioY", httpConnection.responseMessage)
                    responseCodeText.textColor = Color.GREEN
                    responseCodeText.text =
                        "Status Code: " + httpConnection.responseCode.toString() +
                                "\nResponse message: " + httpConnection.responseMessage
                }else{
                    val errorStreamReader = InputStreamReader(httpConnection.errorStream)
                    val bufferedReader = BufferedReader(errorStreamReader)
                    responseCodeText.textColor = Color.RED
                    val html:String = bufferedReader.readText()
                    responseCodeText.text = "Error!\n"+ getTitle(html)
                }
            }catch (ex:Exception){
                responseCodeText.textColor = Color.RED
                responseCodeText.text = "Exception: "+ ex.message
                ex.printStackTrace()
            }
            httpConnection.disconnect()
        }
    }
    fun getTitle(html:String):String{
        val HtmlTitleStartIndex:Int = html.indexOf("<title>")
        val HtmlTitleEndIndex:Int = html.indexOf("</title>")
        return html.substring(HtmlTitleStartIndex+7,HtmlTitleEndIndex)
    }
}
