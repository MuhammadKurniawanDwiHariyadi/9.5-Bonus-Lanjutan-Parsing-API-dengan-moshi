package com.dicoding.myworkmanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.myworkmanager.BuildConfig
import com.dicoding.myworkmanager.R
import com.dicoding.myworkmanager.moshi.Response
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.DecimalFormat

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var resultStatus: Result? = null

    /*
        Metode doWork adalah metode yang akan dipanggil ketika WorkManager berjalan.
        Kode di dalamnya akan dijalankan di background thread secara otomatis

        Function ini juga mengembalikan status berbentuk result dari function getCurrectWeather
        Result.success(), Result.failure() dan Result.retry()
     */
    override fun doWork(): Result {
        val dataCity = inputData.getString(EXTRA_CITY)
        return getCurrentWeather(dataCity)
    }

    // function ini di awali dengan pengambilan data dari REST API, disini menggunakan LoopJ untuk pengambilan datanya
    private fun getCurrentWeather(city: String?): Result {

        Log.d(TAG, "getCurrentWeather: Mulai.....")

        Looper.prepare()
        val client = SyncHttpClient() // ini untuk berjalan secara synchronus, guna melakukan post.Request URL API
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$APP_ID"

        Log.d(TAG, "getCurrentWeather: $url")

        client.post(url, object : AsyncHttpResponseHandler() { // clien bersifat syncronus menerima 2 paramater URL dan asynchronus untuk berjalan secara
            // selain post(), ada juga get(), put(), head() dan delete()

            override fun onSuccess(statusCode: Int, headers: Array<Header?>?, responseBody: ByteArray) {
                val result = String(responseBody)

                Log.d(TAG, result)

                try {

//                    val responseObject = JSONObject(result)
//                    val currentWeather: String = responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
//                    val description: String = responseObject.getJSONArray("weather").getJSONObject(0).getString("description")
//                    val tempInKelvin = responseObject.getJSONObject("main").getDouble("temp")
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                    val jsonAdapter = moshi.adapter(Response::class.java)
                    val response = jsonAdapter.fromJson(result)

                    response?.let {
                        val currentWeather = it.weatherList[0].main
                        val description = it.weatherList[0].description
                        val tempInKelvin = it.main.temperature

                        val tempInCelsius = tempInKelvin - 273
                        val temperature: String = DecimalFormat("##.##").format(tempInCelsius)

                        val title = "Current Weather in $city"
                        val message = "$currentWeather, $description with $temperature celsius"

                        showNotification(title, message) // memanggil notifikasi
                        Log.d(TAG, "onSuccess: Selesai.....")

                        resultStatus = Result.success() // result yang di kembalikan ke doWork untuk return value
                    }
                } catch (e: Exception) {

                    showNotification("Get Current Weather Not Success", e.message)
                    Log.d(TAG, "onSuccess: Gagal.....")
                    resultStatus = Result.failure() // result yang di kembalikan ke doWork untuk return value

                }

            }

            override fun onFailure(statusCode: Int, headers: Array<Header?>?, responseBody: ByteArray?, error: Throwable) {
                Log.d(TAG, "onFailure: Gagal.....")
                // ketika proses gagal, maka jobFinished diset dengan parameter true. Yang artinya job perlu di reschedule
                showNotification("Get Current Weather Failed", error.message)
                resultStatus = Result.failure() // result yang di kembalikan ke doWork untuk return value
            }
        })
        return resultStatus as Result // kesimpulanya ada disini

        /*
            Khusus di WorkManager Anda perlu menjalankan proses secara synchronous supaya bisa
            mendapatkan result success. Selain itu WorkManager sudah berjalan di background thread,
            jadi aman saja menjalankan secara langsung. Namun jika Anda ingin menggunakan LoopJ
            di Activity, maka gunakanlah AsyncHttpClient supaya tidak terjadi error NetworkOnMainThread.
         */
    }


    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_active)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }


    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val APP_ID = BuildConfig.APP_ID
        const val EXTRA_CITY = "city"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

}

/*
    Parsing Json di sini menggunakan Moshi

    Yang perlu diperhatikan adalah pada kode moshi.adapter pastikan Anda memberikan class yang
    sesuai dengan hasil JSON yang diberikan. Dalam kasus ini adalah kelas Response yang merupakan
    induk dari semua kelas.

    Catatan:
    Apabila response JSON dimulai dengan kurung siku / [ ] yang berarti ia berupa JSON Array.
    Maka pada moshi.adapter argumen yang dimasukkan adalah Array<Response>.
 */