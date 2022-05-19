package com.example.healthapp.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.R
import com.example.healthapp.broadcast.MyBroadcastReceiver
import com.example.healthapp.databinding.ActivityAlarmOnBinding
import com.example.healthapp.model.TimeToTakeMedicine
import com.example.healthapp.service.MyService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmOnActivity : AppCompatActivity() {
    private var _binding: ActivityAlarmOnBinding? = null
    private val binding get() = _binding!!
    private var gTime = ""
    private var list = ArrayList<TimeToTakeMedicine>()
    private var position = 0
    private var nameMedicine = ""
    private var numberDrug = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlarmOnBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        gTime = intent.getStringExtra("iSec").toString()
        nameMedicine = intent.getStringExtra("iNameMedicine").toString()
        binding.tvTitleAlarm.text = intent.getStringExtra("iNumberDrug").toString()

        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.alarm_tone)
        mp.start()
        startService()


        binding.btnStopAlarm.setOnClickListener {
            position = intent.getStringExtra("iSize").toString().toInt()
            mp.stop()
//            finish()
            turnOfAlarm(position)
        }
    }

    private fun startService() {
        val i = Intent(this, MyService::class.java)
//        val key = intent.getStringExtra("key")
//        i.putExtra("key", key)
        startService(i)
        Toast.makeText(this, "ran", Toast.LENGTH_SHORT).show()
    }

    private fun turnOfAlarm(position: Int) {
        setStatusTo0(position)
        val am = getSystemService(ALARM_SERVICE) as AlarmManager?
        val i = Intent(this, MyBroadcastReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            this, gTime.toInt(), i, 0
        )
        am?.cancel(pi)
//        finish()
        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show()
    }

    private fun setStatusTo0(position: Int) {
        val shared = getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        val jsonTime = getDataInArrayList("tag1${idUser.toString()}")
        val a = ArrayList<TimeToTakeMedicine>()

        jsonTime.forEach {
            list.add(it)
        }
        a.add(jsonTime[position])
        a.forEach {
            it.status = "0"
        }
        val mPrefs: SharedPreferences =
            getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val mEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        mEditor.putString("tag1${idUser.toString()}", json).apply()
    }

    private fun getDataInArrayList(key: String): ArrayList<TimeToTakeMedicine> {
        val mPrefs: SharedPreferences = getSharedPreferences(
            "shared preferences",
            Context.MODE_PRIVATE
        )
        val emptyList = Gson().toJson(ArrayList<TimeToTakeMedicine>())
        return Gson().fromJson(
            mPrefs.getString(key, emptyList),
            object : TypeToken<ArrayList<TimeToTakeMedicine>>() {
            }.type
        )
    }

}