package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.*
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentAddRecordMedicalBinding
import com.example.healthapp.extension.formatDate
import com.example.healthapp.model.*
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class AddRecordMedicalFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    AdapterDiseases.OnItemClickListener1, AdapterMedicine.OnItemClickListener2,
    AdapterMedicineOfUser.OnItemClickListener3, AdapterDetailMedicine.OnItemClickListener4 {
    private var _binding: FragmentAddRecordMedicalBinding? = null
    private val binding get() = _binding!!

    private var list = mutableListOf<Diseases>()
    private var list1 = mutableListOf<Medicine>()
    private var list2 = mutableListOf<Medicine>()
    private var list3 = mutableListOf<Medicine>()
    private var list4 = mutableListOf<DetailMedicine>()


    var day = 0
    var month = 0
    var year = 0
    private var hour = 0
    var minute = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    private var saveHour = 0
    var saveMinute = 0

    var date = ""
    var time = ""
    var time1 = ""

//    private var lastIndex = 0

    private val arr = ArrayList<String>()
    private val arr1 = ArrayList<String>()
    private val arr2 = ArrayList<String>()
    private val arr3 = ArrayList<String>()
    private val arr4 = ArrayList<Int>()

    private var tvvvv: TextView? = null
    private var tvvvv1: TextView? = null

    private var edtNumberMedicine: String = ""
    private var edtTimeMedicine: String = ""
    private var edtNoticeMedicine: String = ""


    private var simpleSwitch: Switch? = null
    private var switchOfDetailMedicine: Switch? = null

    private var dialog_: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecordMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet", "UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hiddenAlarmLayout()
        switchOfDetailMedicine = binding.switchOfDetailMedicine

        binding.btnPickDate.setOnClickListener {
            openFeedbackDialog(Gravity.CENTER)
        }

        binding.btnPickTime.setOnClickListener {
            pickTime()
        }

        binding.btnAddRecord.setOnClickListener {
            addRecordMedical()
        }

        binding.backFragment.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.inputDiseases.setOnClickListener {
            openDialogListDiseases(Gravity.BOTTOM)
        }

        binding.inputMedicine.setOnClickListener {
            openDialogListMedicine(Gravity.BOTTOM)
        }



        switchOfDetailMedicine?.setOnClickListener {
            if (switchOfDetailMedicine?.isChecked == true) {
                openDialogDetailMedicine(Gravity.BOTTOM)
            }
        }
    }

    // pick time
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getTimeCalendar() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pickTime() {
        getTimeCalendar()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute
        getTimeCalendar()
        time = "$saveHour : $saveMinute"
        binding.tvPickTime.text = time
    }
    // end pick time

    // dialog date picker option
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun openFeedbackDialog(gravity: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_spinner_date_picker)

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
        val datePicker: DatePicker = dialog.findViewById(R.id.datePicker)
        val cal = Calendar.getInstance()

        datePicker.formatDate("dmy")
        dialog.setCancelable(false)
        dialog.show()


        if (saveDay == 0 && saveMonth == 0 && saveYear == 0) {
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)
            datePicker.init(
                year,
                month,
                day
            ) { _, year, monthOfYear, dayOfMonth ->
                saveDay = dayOfMonth
                saveMonth = monthOfYear + 1
                saveYear = year
                date = "$saveDay/$saveMonth/$saveYear"
            }
        } else {
            datePicker.init(
                saveYear,
                saveMonth,
                saveDay
            ) { _, year, monthOfYear, dayOfMonth ->
                saveDay = dayOfMonth
                saveMonth = monthOfYear + 1
                saveYear = year
                date = "$saveDay/$saveMonth/$saveYear"
            }
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
            if (date === "") {
                day = cal.get(Calendar.DAY_OF_MONTH)
                month = cal.get(Calendar.MONTH) + 1
                year = cal.get(Calendar.YEAR)

                date = "$day/$month/$year"
                binding.tvPickDate.text = date
            } else {
                binding.tvPickDate.text = date
            }
        }
    }

    private fun openDialogListDiseases(gravity: Int) {
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

        tvvvv = getArrNameDiseases
        tvvvv?.text = arr.toString()
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        MySingleton.network.getDiseases(idUser).enqueue(object : Callback<BaseDiseases> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseDiseases>, response: Response<BaseDiseases>) {
                list.clear()
                response.body()?.resultDiseases?.forEach {
                    list.add(it)
                }
                rc.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rc.adapter =
                    AdapterDiseases(list, dialog.context, this@AddRecordMedicalFragment)
            }

            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getDiseases(idUser)
                        .enqueue(object : Callback<BaseDiseases> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDiseases>,
                                response: Response<BaseDiseases>
                            ) {
                                list.clear()
                                response.body()?.resultDiseases?.forEach {
                                    if (query != null) {
                                        if (it.name_diseases.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list.add(it)
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
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDiseases(idUser)
                        .enqueue(object : Callback<BaseDiseases> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDiseases>,
                                response: Response<BaseDiseases>
                            ) {
                                list.clear()
                                response.body()?.resultDiseases?.forEach {
                                    list.add(it)
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getDiseases(idUser)
                        .enqueue(object : Callback<BaseDiseases> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDiseases>,
                                response: Response<BaseDiseases>
                            ) {
                                list.clear()
                                response.body()?.resultDiseases?.forEach {
                                    if (newText != null) {
                                        if (it.name_diseases.toLowerCase()
                                                .contains(newText.toLowerCase())
                                        ) {
                                            list.add(it)
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
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDiseases(idUser)
                        .enqueue(object : Callback<BaseDiseases> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDiseases>,
                                response: Response<BaseDiseases>
                            ) {
                                list.clear()
                                response.body()?.resultDiseases?.forEach {
                                    list.add(it)
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                }
                return true
            }
        })

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun openDialogListMedicine(gravity: Int) {
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

        tvvvv = getArrNameDiseases
        tvvvv?.text = arr1.toString()

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        MySingleton.network.getMedicine(idUser).enqueue(object : Callback<BaseMedicine> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseMedicine>, response: Response<BaseMedicine>) {
                list1.clear()
                response.body()?.resultMedicine?.forEach {
                    list1.add(it)
                }
                rc.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rc.adapter =
                    AdapterMedicine(list1, dialog.context, this@AddRecordMedicalFragment)
            }

            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getMedicine(idUser)
                        .enqueue(object : Callback<BaseMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseMedicine>,
                                response: Response<BaseMedicine>
                            ) {
                                list1.clear()
                                response.body()?.resultMedicine?.forEach {
                                    if (query != null) if (it.name_medicine.toLowerCase(Locale.ROOT)
                                            .contains(query.toLowerCase(Locale.ROOT))
                                    ) {
                                        list1.add(it)
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterMedicine(
                                        list1,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getMedicine(idUser)
                        .enqueue(object : Callback<BaseMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseMedicine>,
                                response: Response<BaseMedicine>
                            ) {
                                list1.clear()
                                response.body()?.resultMedicine?.forEach {
                                    list1.add(it)
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterMedicine(
                                        list1,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getMedicine(idUser)
                        .enqueue(object : Callback<BaseMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseMedicine>,
                                response: Response<BaseMedicine>
                            ) {
                                list1.clear()
                                response.body()?.resultMedicine?.forEach {
                                    if (newText != null) {
                                        if (it.name_medicine.toLowerCase()
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
                                    AdapterMedicine(
                                        list1,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getMedicine(idUser)
                        .enqueue(object : Callback<BaseMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseMedicine>,
                                response: Response<BaseMedicine>
                            ) {
                                list1.clear()
                                response.body()?.resultMedicine?.forEach {
                                    if (newText != null) {
                                        if (it.name_medicine.toLowerCase()
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
                                    AdapterMedicine(
                                        list1,
                                        dialog.context,
                                        this@AddRecordMedicalFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
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
    }

    override fun onItemClicked1(position: Int) {
        val myModel = list[position]
        arr.add(myModel.name_diseases)

        binding.inputDiseases.text = arr.toString()
        tvvvv?.text = arr.toString()
        Toast.makeText(requireContext(), arr.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked2(position: Int) {
        val myModel = list1[position]
        list2.add(myModel)
        arr1.add(myModel.name_medicine)

        binding.inputMedicine.text = arr1.toString()
        tvvvv?.text = arr1.toString()

        Log.e("notice", list1.toString())
        Log.e("notice", list2.toString())
        Toast.makeText(requireContext(), arr1.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun hiddenAlarmLayout() {
    }

    private fun addRecordMedical() {
        var lastIndex = 0
        var indexOfRecord = 0
        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        val diseases = binding.inputDiseases.text.toString().trim()
        val medicine = binding.inputMedicine.text.toString().trim()
        val nameDoctor = binding.edtNameDoctor.text.toString().trim()
        val numberPhoneOfDoctor = binding.edtNumberPhoneOfDoctor.text.toString().trim()

        MySingleton.network.addRecordMedical(
            channel.toString().toInt(),
            date,
            time,
            diseases,
            medicine,
            nameDoctor,
            numberPhoneOfDoctor
        ).enqueue(object :
            Callback<RecordMedical> {
            override fun onResponse(
                call: Call<RecordMedical>,
                response: Response<RecordMedical>
            ) {
            }

            override fun onFailure(call: Call<RecordMedical>, t: Throwable) {}
        })
        if (edtNumberMedicine == "") {
            findNavController().navigateUp()
        } else {
            MySingleton.network.getRecordMedical(idUser)
                .enqueue(object : Callback<BaseRecordMedical> {
                    @SuppressLint("WrongConstant")
                    override fun onResponse(
                        call: Call<BaseRecordMedical>,
                        response: Response<BaseRecordMedical>
                    ) {
                        response.body()?.resultMedical?.forEach {
                            arr4.add(it.id)
                        }
                        if (lastIndex == null) {
                            lastIndex = 0
                        } else {
                            lastIndex = arr4.last().toInt()
                        }
                        Log.e("index", lastIndex.toString())
                        addDetailMedicine(idUser, lastIndex)
                    }

                    override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
                })
            findNavController().navigateUp()
        }
        Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("WrongConstant")
    private fun openDialogDetailMedicine(gravity: Int) {

        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_detail_medicine)

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

        val btnChoose = dialog.findViewById<TextView>(R.id.btnChooseMedicine)
        val btnDone = dialog.findViewById<TextView>(R.id.btnDone)
        val btnClose = dialog.findViewById<TextView>(R.id.btnClose)
        val getArrNameDiseases: TextView = dialog.findViewById(R.id.btnChooseMedicine)

        tvvvv1 = getArrNameDiseases
        tvvvv1?.text = arr2.toString()

        btnChoose.setOnClickListener {
            openDialogListMedicineOfUser(gravity)
            dialog.dismiss()
        }

        btnClose.setOnClickListener {
            switchOfDetailMedicine?.isChecked = false
            dialog.dismiss()
        }

        btnDone.setOnClickListener {
            val nameMedicine = arr2.toString()
            val numberMedicine = dialog.findViewById<EditText>(R.id.numberMedicine)
            val timeMedicine = dialog.findViewById<EditText>(R.id.timeMedicine)
            val noticeMedicine = dialog.findViewById<EditText>(R.id.noticeMedicine)

            val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
            val channel = shared?.getString("idOfUser", null)
            val idUser = channel.toString().toInt()

            edtNumberMedicine = numberMedicine.text.toString()
            edtTimeMedicine = timeMedicine.text.toString()
            edtNoticeMedicine = noticeMedicine.text.toString()
            if (edtNumberMedicine == "" || edtTimeMedicine == "" || edtNoticeMedicine == "") {
                dialog.dismiss()
            } else {
                list4.add(
                    DetailMedicine(
                        idUser,
                        nameMedicine,
                        edtNumberMedicine,
                        edtTimeMedicine,
                        edtNoticeMedicine
                    )
                )
                binding.recyclerview11.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                binding.recyclerview11.adapter =
                    AdapterDetailMedicine(list4, dialog.context, this@AddRecordMedicalFragment)
                dialog.dismiss()
            }
            switchOfDetailMedicine?.isChecked = false
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    @SuppressLint("WrongConstant")
    private fun openDialogListMedicineOfUser(gravity: Int) {
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

        tvvvv1 = getArrNameDiseases
        tvvvv1?.text = arr2.toString()
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        rc.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rc.adapter =
            AdapterMedicineOfUser(list2, dialog.context, this@AddRecordMedicalFragment)

        btnClose.setOnClickListener {
//            openDialogDetailMedicine(gravity)
            dialog.dismiss()
        }
        dialog_ = dialog
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onItemClicked3(position: Int) {
        val myModel = list2[position]
        list3.add(myModel)
        arr2.clear()
        arr2.add(myModel.name_medicine)
//        binding.inputMedicine.text = arr1.toString()
        tvvvv1?.text = arr2.toString()
        openDialogDetailMedicine(Gravity.BOTTOM)
    }

    override fun onItemClicked4(position: Int) {
        Toast.makeText(requireContext(), "Yeah", Toast.LENGTH_SHORT).show()
    }

    private fun addDetailMedicine(id_user: Int, id_record: Int) {
        list4.forEach {
            MySingleton.network.addDetailMedicine(
                id_user,
                id_record,
                it.name_medicine,
                it.number_medicine,
                it.time_medicine,
                it.notice_medicine
            ).enqueue(object : Callback<DetailMedicine> {
                override fun onResponse(
                    call: Call<DetailMedicine>,
                    response: Response<DetailMedicine>
                ) {
                }

                override fun onFailure(call: Call<DetailMedicine>, t: Throwable) {}
            })
        }
    }
}
