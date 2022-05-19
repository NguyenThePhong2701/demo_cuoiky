package com.example.healthapp.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthapp.broadcast.MyBroadcastReceiver
import com.example.healthapp.databinding.FragmentDetailAlarmTimeBinding
import com.example.healthapp.model.TimeToTakeMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailAlarmTimeFragment : Fragment() {
    private var _binding: FragmentDetailAlarmTimeBinding? = null
    private val binding get() = _binding!!

    private var gHour = 0
    private var gMinutes = 0
    private var sec = 0
    private var gTitleAlarm = ""
    private var gPosition = 0
    private var gStatus = ""
    private var nameMedicine = ""
    private var numberDrug = ""
    var pi: PendingIntent? = null
    private var idUser = 0
    private var list = ArrayList<TimeToTakeMedicine>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAlarmTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        idUser = channel.toString().toInt()

        nameMedicine = arguments?.getString("iNameMedicine").toString()
        gHour = arguments?.getString("iHour").toString().toInt()
        gMinutes = arguments?.getString("iMinutes").toString().toInt()
        gTitleAlarm = arguments?.getString("iTitleAlarm").toString()
        numberDrug = arguments?.getString("iNumberDrug").toString()
        gPosition = arguments?.getString("iPosition").toString().toInt()
        gStatus = arguments?.getString("iStatus").toString()


        binding.tvGetNameMedicine.text = nameMedicine
        binding.tvGetTime.text = "$gHour : $gMinutes"
        binding.tvGetTitleAlarm.text = gTitleAlarm
        binding.tvGetNumberDrug.text = numberDrug
        checkStatus(gStatus)

        binding.btnTurnOfAlarm.setOnClickListener {
            turnOfAlarm(gPosition)
        }

        binding.btnTurnOnAlarm.setOnClickListener {
            turnOnAlarm(gPosition, gTitleAlarm)
        }

        binding.btnDeleteAlarm.setOnClickListener {
            deleteAlarm(gPosition)
        }
    }


    private fun checkStatus(status: String) {
        if (status == "1") {
            binding.btnTurnOnAlarm.visibility = View.INVISIBLE
        } else {
            binding.btnTurnOfAlarm.visibility = View.INVISIBLE
        }
    }

    private fun getDataInArrayList(key: String): ArrayList<TimeToTakeMedicine> {
        val mPrefs: SharedPreferences = requireContext().getSharedPreferences(
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

    private fun turnOfAlarm(position: Int) {
        setStatusTo0(position)
        val am = activity?.getSystemService(ALARM_SERVICE) as AlarmManager?
        val i = Intent(requireContext(), MyBroadcastReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            requireContext(), gHour.toInt(), i, 0
        )
        am?.cancel(pi)
        findNavController().navigateUp()
        Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
    }

    private fun setStatusTo0(position: Int) {

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
            requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val mEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        mEditor.putString("tag1${idUser.toString()}", json).apply()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun turnOnAlarm(position: Int, titleAlarm: String) {
        setStatusTo1(position)
        val cal1 = Calendar.getInstance()
        val getHour1 = cal1.get(Calendar.HOUR_OF_DAY)
        val getMinutes1 = cal1.get(Calendar.MINUTE)
        val getSecond1 = cal1.get(Calendar.SECOND)
        val getMillisecond = cal1.get(Calendar.MILLISECOND)

        val secOfNow =
            (getHour1 * 60 * 60) + (getMinutes1 * 60) + (getSecond1) + (getMillisecond / 1000)
        var secOfChosen = (gHour * 60 * 60) + (gMinutes * 60)

        if (secOfChosen < secOfNow) {
            secOfChosen = (24 * 60 * 60) + (gHour * 60 * 60) + (gMinutes * 60)
            sec = (secOfChosen - secOfNow) * 1000
        } else if (secOfChosen > secOfNow) {
            sec = (secOfChosen - secOfNow) * 1000
        }

        setAlarm(sec, position)
        findNavController().navigateUp()
        Toast.makeText(requireContext(), gTitleAlarm, Toast.LENGTH_SHORT).show()
    }

    private fun setStatusTo1(position: Int) {
        val jsonTime = getDataInArrayList("tag1${idUser.toString()}")
        val a = ArrayList<TimeToTakeMedicine>()

        jsonTime.forEach {
            list.add(it)
        }

        a.add(jsonTime[position])
        a.forEach {
            it.status = "1"
        }

        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val mEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        mEditor.putString("tag1${idUser.toString()}", json).apply()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setAlarm(sec: Int, position: Int) {
        val titleAlarm = arguments?.getString("iTitleAlarm").toString()
        val am: AlarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val i = Intent(requireContext(), MyBroadcastReceiver::class.java)
        i.putExtra("nameMedicine", nameMedicine)
        i.putExtra("key", sec.toString())
        i.putExtra("titleAlarm", titleAlarm)
        i.putExtra("numberDrug", numberDrug)
        i.putExtra("iSize", position)

        pi = PendingIntent.getBroadcast(requireContext(), sec, i, 0)
        am.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (sec), pi
        )
        Toast.makeText(requireContext(), "Alarm $sec", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAlarm(position: Int) {
//        setStatusTo0(position)
        val jsonTime = getDataInArrayList("tag1${idUser.toString()}")
        jsonTime.forEach {
            list.add(it)
        }
        list.removeAt(position)
        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val mEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        mEditor.putString("tag1${idUser.toString()}", json).apply()
        findNavController().navigateUp()
    }

}