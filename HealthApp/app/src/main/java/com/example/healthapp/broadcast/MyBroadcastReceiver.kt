package com.example.healthapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.healthapp.activity.AlarmOnActivity
import com.example.healthapp.service.MyService

class MyBroadcastReceiver : BroadcastReceiver (){
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, AlarmOnActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val nameMedicine = intent?.getStringExtra("nameMedicine")
        val sec = intent?.getStringExtra("sec")
        val titleAlarm = intent?.getStringExtra("titleAlarm")
        val numberDrug = intent?.getStringExtra("numberDrug")
        val size = intent?.getStringExtra("iSize")
        i.putExtra("iNameMedicine", nameMedicine.toString())
        i.putExtra("iSec", sec.toString())
        i.putExtra("titleAlarm",titleAlarm.toString())
        i.putExtra("iNumberDrug", numberDrug.toString())
        i.putExtra("iSize",size.toString())
        context?.startActivity(i)

        val i1 = Intent(context, MyService::class.java)
        val sec1 = intent?.getStringExtra("key")
        i1.putExtra("key", sec1.toString())
        context?.startService(i1)
    }
}