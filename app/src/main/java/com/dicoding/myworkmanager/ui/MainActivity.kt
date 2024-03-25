package com.dicoding.myworkmanager.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.dicoding.myworkmanager.R
import com.dicoding.myworkmanager.databinding.ActivityMainBinding
import com.dicoding.myworkmanager.utils.MyWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var workManager: WorkManager

    private lateinit var binding: ActivityMainBinding

    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        workManager = WorkManager.getInstance(this)

        binding.BTOneTimeTask.setOnClickListener(this)
        binding.btnPeriodicTask.setOnClickListener(this)
        binding.btnCancelTask.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val city = binding.ETCity.text.toString().trim()
        when(v?.id){
            R.id.BTOneTimeTask -> startOneTimeTask(city)
            R.id.btnPeriodicTask -> startPeriodicTask(city)
            R.id.btnCancelTask -> cancelPeriodicTask()
        }
    }

    // Function untuk menjalankan task sekali saja
    private fun startOneTimeTask(city: String) {
        binding.TVStatus.text = getString(R.string.status)

        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, city)  // disini akan menyiapkan data ke workManager
            .build()

        // Constraint digunakan untuk memberikan syarat kapan task ini dieksekusi
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // untuk mengecek akses apakah terhubung ke internet
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java) // dari code ini untuk membuat task jalan sekali, lalu tidak lupa memanggil class yang tertanggung jawab setelahnya
            .setInputData(data) // disini data dari putString akan di kirim ke workManager
            .setConstraints(constraints) // disini syarat pengeksekusianya di tentukan dari constrain yaitu jika terhubung ke internet
            .build()

        workManager.enqueue(oneTimeWorkRequest)

        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id) // WorkInfo digunakan untuk mengetahui status task yang dieksekusi
            .observe(this@MainActivity) { workInfo-> // berjalan secara live data dengan observe
                val status = workInfo.state.name
                binding.TVStatus.append("\n + $status")

                /*
                    Anda dapat membaca status secara live dengan menggunakan getWorkInfoByIdLiveData.
                    Anda juga bisa memberikan aksi pada state tertentu dengan mengambil data state dan
                    membandingkannya dengan konstanta yang bisa didapat di WorkInfo.State.
                    Misalnya, pada kode di atas kita mengatur tombol Cancel task aktif jika task
                    dalam state ENQUEUED.
                 */

            }
    }

    // Function untuk menjalankan task secara periodic
    private fun startPeriodicTask(city: String) {
        binding.TVStatus.text = getString(R.string.status)

        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, city)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES) // set 15 menit, dari code ini untuk mmebuat secara periodic (batas minila 15 sama dengan jobScheduler)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(periodicWorkRequest)

        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.TVStatus.append("\n + $status")
                binding.btnCancelTask.isEnabled = false

                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    binding.btnCancelTask.isEnabled = true
                }
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id) // ini untuk membatalkan task yang berjalan enqueue dari workManager
        /*
        code diatas membatalkan berdasarkan id request, lalu ada dengan tag yang kelebihan bisa membatalkan sekaligus jika misal

        periodicWorkRequest = periodicWorkRequest.Builder(MyWorker:class.java, 15, TimeUnit.MINUTES)
            .addTag(TAGnya)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .addTag(TAGnya)
            .build()

        workManager.cancelWorkById(TAGnya)

        maka task periodicWorkRequest dan oneTimeWorkRequest akan berhenti karena memilki tag yang sama dan di batalkan

         */
    }
}


/*  Berapa kondisi untuk constrain yang bisa di pakai

    1. setRequiredNetworkType, ketika bernilai CONNECTED berarti dia harus terhubung ke koneksi internet, apa pun jenisnya. Bila kita ingin memasang ketentuan bahwa job hanya akan berjalan ketika perangkat terhubung ke network Wi-fi, maka kita perlu memberikan nilai UNMETERED.
    2. setRequiresDeviceIdle, menentukan apakah task akan dijalankan ketika perangkat dalam keadaan sedang digunakan atau tidak. Secara default, parameter ini bernilai false. Bila kita ingin task dijalankan ketika perangkat dalam kondisi tidak digunakan, maka kita beri nilai true.
    3. setRequiresCharging, menentukan apakah task akan dijalankan ketika baterai sedang diisi atau tidak. Nilai true akan mengindikasikan bahwa task hanya berjalan ketika baterai sedang diisi. Kondisi ini dapat digunakan bila task yang dijalankan akan memakan waktu yang lama.
    4. setRequiresStorageNotLow, menentukan apakah task yang dijalankan membutuhkan ruang storage yang tidak sedikit. Secara default, nilainya bersifat false.
    5. Dan ketentuan lainnya yang bisa kita gunakan.
 */


/*
    Selain menjalankan single task seperti pada latihan, Anda juga bisa membuat task-chaining,
    baik secara paralel maupun sekuensial. Berikut adalah contohnya:

    workManager
      .beginWith(workA1, workA2, workA3)
      .then(workB)
      .then(workC1, workC2)
      .enqueue()

 */

