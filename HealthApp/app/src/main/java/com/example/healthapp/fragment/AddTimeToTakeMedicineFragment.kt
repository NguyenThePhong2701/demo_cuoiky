package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.AdapterBrowserFragment
import com.example.healthapp.Adapter.AdapterDetailMedicine
import com.example.healthapp.R
import com.example.healthapp.broadcast.MyBroadcastReceiver
import com.example.healthapp.databinding.FragmentAddTimeToTakeMedicineBinding
import com.example.healthapp.model.*
import com.example.healthapp.mySingleton.MySingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("UNREACHABLE_CODE")
class AddTimeToTakeMedicineFragment : Fragment(), AdapterBrowserFragment.OnItemClickListener,
    AdapterDetailMedicine.OnItemClickListener4 {
    private var _binding: FragmentAddTimeToTakeMedicineBinding? = null
    private val binding get() = _binding!!
    private var list = ArrayList<TimeToTakeMedicine>()
    private var pi: PendingIntent? = null
    private var sec: Int = 0
    var hour = 0
    var minutes = 0
    var saveHour = 0
    var saveMinutes = 0
    var time = ""

    private var arr2: String = ""
    private var arr3: String = ""
    private var arr4: String = ""
    private var arr5: String = ""

    private var tvvvv: TextView? = null
    private val list1 = mutableListOf<RecordMedical>()
    private val list2 = mutableListOf<DetailMedicine>()
    private val list3 = mutableListOf<DetailMedicine>()
    private val arr = ArrayList<String>()
    private val arr1 = ArrayList<String>()

    private var dialog_: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTimeToTakeMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal = Calendar.getInstance()
        val getHour = cal.get(Calendar.HOUR_OF_DAY)
        val getMinutes = cal.get(Calendar.MINUTE)
        val time = "$getHour : $getMinutes"
        binding.getTimeNow.text = time

        binding.getTimeNow.setOnClickListener {
            openDialogTimePicker(Gravity.CENTER)
        }

        binding.btnSaveAlarm.setOnClickListener {
            saveAlarm()
        }

        binding.backFragment.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.chooseRecordMedical.setOnClickListener {
            openDialogRecordMedical(Gravity.BOTTOM)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun openDialogTimePicker(gravity: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_time_to_take_medicine)

        val window: Window? = dialog.window
        if (window === null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val btnOk: TextView = dialog.findViewById(R.id.labelBtnOk)
        val timePicker: TimePicker = dialog.findViewById(R.id.timePicker)
        timePicker.setIs24HourView(true)
        val cal = Calendar.getInstance()

        if (saveHour == 0 && saveMinutes == 0) {
            hour = cal.get(Calendar.HOUR_OF_DAY)
            minutes = cal.get(Calendar.MINUTE)
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                hour = hourOfDay
                minutes = minute
                time = "$hour : $minutes"
                saveHour = hour
                saveMinutes = minutes
            }
        } else {
            timePicker.hour = saveHour
            timePicker.minute = saveMinutes
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                saveHour = hourOfDay
                saveMinutes = minute
                time = "$saveHour : $saveMinutes"
            }
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
            if (time === "") {
                hour = cal.get(Calendar.HOUR_OF_DAY)
                minutes = cal.get(Calendar.MINUTE)

                time = "$hour : $minutes"
                binding.getTimeNow.text = time
            } else {
                binding.getTimeNow.text = time
            }
            Toast.makeText(requireContext(), time, Toast.LENGTH_SHORT).show()
        }

        dialog.show()

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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setAlarm1(
        nameMedicine: String,
        sec: Int,
        titleAlarm: String,
        numberDrug: String,
        size: Int
    ) {
        val am: AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(requireContext(), MyBroadcastReceiver::class.java)

        i.putExtra("nameMedicine", nameMedicine)
        i.putExtra("sec", sec.toString())
        i.putExtra("titleAlarm", titleAlarm)
        i.putExtra("numberDrug", numberDrug)
        i.putExtra("iSize", size.toString())

        pi = PendingIntent.getBroadcast(requireContext(), sec, i, 0)
        am.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (sec), pi
        )
        Toast.makeText(requireContext(), "Alarm $sec + $size", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveAlarm() {
        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        val cal1 = Calendar.getInstance()
        val getHour1 = cal1.get(Calendar.HOUR_OF_DAY)
        val getMinutes1 = cal1.get(Calendar.MINUTE)
        val getSecond1 = cal1.get(Calendar.SECOND)
        val getMillisecond = cal1.get(Calendar.MILLISECOND)

        val secOfNow =
            (getHour1 * 60 * 60) + (getMinutes1 * 60) + (getSecond1) + (getMillisecond / 1000)
        if (saveHour == 0 && saveMinutes == 0){
            saveHour = getHour1
            saveMinutes = getMinutes1
        }
        var secOfChosen = (saveHour * 60 * 60) + (saveMinutes * 60)

        if (secOfChosen < secOfNow) {
            secOfChosen = (24 * 60 * 60) + (saveHour * 60 * 60) + (saveMinutes * 60)
            sec = (secOfChosen - secOfNow) * 1000
        } else if (secOfChosen > secOfNow) {
            sec = (secOfChosen - secOfNow) * 1000
        }
        val titleAlarm = binding.edtTitleAlarm.text.toString().trim()
        val selectedTime = "$saveHour : $saveMinutes"
        if (list == null) {
            list.add(
                TimeToTakeMedicine(
                    arr5,
                    selectedTime,
                    saveHour.toString(),
                    saveMinutes.toString(),
                    binding.edtTitleAlarm.text.toString(),
                    arr3,
                    "1"
                )
            )
            val mPrefs: SharedPreferences =
                requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE)
            val mEditor = mPrefs.edit()
            val gson = Gson()
            val json = gson.toJson(list)
            mEditor.putString("tag1${idUser.toString()}", json).apply()
        } else {
            val jsonTime = getDataInArrayList("tag1${idUser.toString()}")
            jsonTime.forEach {
                list.add(it)
            }
            val mPrefs: SharedPreferences =
                requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE)
            list.add(
                TimeToTakeMedicine(
                    arr5,
                    selectedTime,
                    saveHour.toString(),
                    saveMinutes.toString(),
                    binding.edtTitleAlarm.text.toString(),
                    arr3,
                    "1"
                )
            )
            val mEditor = mPrefs.edit()
            val gson = Gson()
            val json = gson.toJson(list)
            mEditor.putString("tag1${idUser.toString()}", json).apply()
        }
        val size = list.size - 1
        setAlarm1(arr5, sec, titleAlarm, arr3, size)
        findNavController().navigateUp()
    }

    private fun openDialogRecordMedical(gravity: Int) {
        list3.clear()
        arr.clear()
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_list)

        val window: Window? = dialog.window
        if (window === null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val rc: RecyclerView = dialog.findViewById(R.id.recyclerview)
        val searchView: SearchView = dialog.findViewById(R.id.inputSearch1)
        val getArrNameDiseases: TextView = dialog.findViewById(R.id.getNameDiseasesAndMedicine)
        val btnClose = dialog.findViewById<TextView>(R.id.btnCloseDialog)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        MySingleton.network.getRecordMedical(idUser).enqueue(object : Callback<BaseRecordMedical> {
            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<BaseRecordMedical>,
                response: Response<BaseRecordMedical>
            ) {
                list1.clear()
                response.body()?.resultMedical?.forEach {
                    list1.add(it)
                }
                rc.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rc.adapter =
                    AdapterBrowserFragment(
                        list1,
                        dialog.context,
                        this@AddTimeToTakeMedicineFragment
                    )
            }

            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getRecordMedical(idUser)
                        .enqueue(object : Callback<BaseRecordMedical> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseRecordMedical>,
                                response: Response<BaseRecordMedical>
                            ) {
                                list1.clear()
                                response.body()?.resultMedical?.forEach {
                                    if (query != null) {
                                        if (it.date.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list1.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterBrowserFragment(
                                        list1,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
                        })


                } else {
                    MySingleton.network.getRecordMedical(idUser)
                        .enqueue(object : Callback<BaseRecordMedical> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseRecordMedical>,
                                response: Response<BaseRecordMedical>
                            ) {
                                list1.clear()
                                response.body()?.resultMedical?.forEach {
                                    if (query != null) {
                                        if (it.date.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list1.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterBrowserFragment(
                                        list1,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
                        })
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getRecordMedical(idUser)
                        .enqueue(object : Callback<BaseRecordMedical> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseRecordMedical>,
                                response: Response<BaseRecordMedical>
                            ) {
                                list1.clear()
                                response.body()?.resultMedical?.forEach {
                                    if (newText != null) {
                                        if (it.date.toLowerCase()
                                                .contains(newText.toLowerCase())
                                        ) {
                                            list1.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterBrowserFragment(
                                        list1,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getRecordMedical(idUser)
                        .enqueue(object : Callback<BaseRecordMedical> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseRecordMedical>,
                                response: Response<BaseRecordMedical>
                            ) {
                                list1.clear()
                                response.body()?.resultMedical?.forEach {
                                    if (newText != null) {
                                        if (it.date.toLowerCase()
                                                .contains(newText.toLowerCase())
                                        ) {
                                            list1.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterBrowserFragment(
                                        list1,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
                        })
                }
                return false
            }
        })

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
        dialog_ = dialog
    }

    override fun onItemClicked(position: Int) {
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        val myModel = list1[position]
        val gIdRecord = myModel.id

        dialog_?.dismiss()
        openDialogDetailMedicine(gIdRecord.toInt(), Gravity.BOTTOM)
    }

    private fun openDialogDetailMedicine(idRecord: Int, gravity: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_list)

        val window: Window? = dialog.window
        if (window === null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val rc: RecyclerView = dialog.findViewById(R.id.recyclerview)
        val searchView: SearchView = dialog.findViewById(R.id.inputSearch1)
        val getArrNameDiseases: TextView = dialog.findViewById(R.id.getNameDiseasesAndMedicine)
        tvvvv = getArrNameDiseases
        val btnClose = dialog.findViewById<TextView>(R.id.btnCloseDialog)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        MySingleton.network.getDetailMedicine(idUser, idRecord)
            .enqueue(object : Callback<BaseDetailMedicine> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BaseDetailMedicine>,
                    response: Response<BaseDetailMedicine>
                ) {
                    list2.clear()
                    response.body()?.resultDetailMedicine?.forEach {
                        list2.add(it)
                    }
                    rc.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                    rc.adapter =
                        AdapterDetailMedicine(
                            list2,
                            dialog.context,
                            this@AddTimeToTakeMedicineFragment
                        )
                }

                override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
            })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getDetailMedicine(idUser, idRecord)
                        .enqueue(object : Callback<BaseDetailMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDetailMedicine>,
                                response: Response<BaseDetailMedicine>
                            ) {
                                list2.clear()
                                response.body()?.resultDetailMedicine?.forEach {
                                    if (query != null) {
                                        if (it.name_medicine.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list2.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDetailMedicine(
                                        list2,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDetailMedicine(idUser, idRecord)
                        .enqueue(object : Callback<BaseDetailMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDetailMedicine>,
                                response: Response<BaseDetailMedicine>
                            ) {
                                list2.clear()
                                response.body()?.resultDetailMedicine?.forEach {
                                    if (query != null) {
                                        if (it.name_medicine.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list2.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDetailMedicine(
                                        list2,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
                        })
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getDetailMedicine(idUser, idRecord)
                        .enqueue(object : Callback<BaseDetailMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDetailMedicine>,
                                response: Response<BaseDetailMedicine>
                            ) {
                                list2.clear()
                                response.body()?.resultDetailMedicine?.forEach {
                                    if (newText != null) {
                                        if (it.name_medicine.toLowerCase()
                                                .contains(newText.toLowerCase())
                                        ) {
                                            list2.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDetailMedicine(
                                        list2,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDetailMedicine(idUser, idRecord)
                        .enqueue(object : Callback<BaseDetailMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDetailMedicine>,
                                response: Response<BaseDetailMedicine>
                            ) {
                                list2.clear()
                                response.body()?.resultDetailMedicine?.forEach {
                                    if (newText != null) {
                                        if (it.name_medicine.toLowerCase()
                                                .contains(newText.toLowerCase())
                                        ) {
                                            list2.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDetailMedicine(
                                        list2,
                                        dialog.context,
                                        this@AddTimeToTakeMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
                        })
                }
                return false
            }
        })
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
        dialog_ = dialog
    }

    override fun onItemClicked4(position: Int) {
        list3.clear()
        val myModel = list2[position]
        val idUser = myModel.id_of_user
        val nameMedicine = myModel.name_medicine
        val numberMedicine = myModel.number_medicine
        val timeMedicine = myModel.time_medicine
        val noticeMedicine = myModel.notice_medicine

        list3.add(
            DetailMedicine(
                idUser,
                nameMedicine,
                numberMedicine,
                timeMedicine,
                noticeMedicine
            )
        )
        loadDataOfDetailMedicine(position)
        Log.e("arrr", arr.toString())
        Toast.makeText(requireContext(), nameMedicine, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("WrongConstant")
    private fun loadDataOfDetailMedicine(position: Int) {
        list3.forEach {
            arr.add(it.name_medicine)
            arr1.add(it.name_medicine + " : " + it.number_medicine + " viên/lần ! " + it.notice_medicine + "\n")
        }

        arr2 = arr1.toString().substring(0, arr1.toString().length - 1)
        arr3 = arr2.substring(1, arr1.toString().length - 2)

        arr4 = arr.toString().substring(0, arr.toString().length - 1)
        arr5 = arr4.substring(1, arr.toString().length - 1)
        binding.tvDetailMedicine.text =
            "Đặt báo thức cho loại thuốc: $arr5"
        binding.titleTest.text = arr3
    }

}