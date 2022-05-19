package com.example.healthapp.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.healthapp.R
import com.example.healthapp.databinding.ActivityNewRecordBinding
import com.example.healthapp.extension.formatDate
import com.example.healthapp.model.NewAccount
import com.example.healthapp.model.NewRecord
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewRecordActivity : AppCompatActivity() {
    private var _binding: ActivityNewRecordBinding? = null
    private val binding get() = _binding!!

    var day = 0
    var month = 0
    var year = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0

    var date = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewRecordBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val getId = intent.getStringExtra("iIdOfUser").toString()
        binding.idOfUser.text = getId
        binding.inputBirthDate.setOnClickListener {
            openDialogOption(Gravity.CENTER)
        }
        binding.inputSex.setOnClickListener {
            val otherStrings = arrayOf("Nam", "Nữ", "Khác")
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setItems(otherStrings) { dialogInterface: DialogInterface, i: Int ->
                binding.inputSex.text = otherStrings[i]
            }
            val dialog = mBuilder.create();
            dialog.show()
        }
        binding.btnNext.setOnClickListener {
            val idOfUser = intent.getStringExtra("iIdOfUser").toString().toInt()
            val name = binding.inputName.text.toString().trim()
            val birthDay = binding.inputBirthDate.text.toString().trim()
            val sex = binding.inputSex.text.toString().trim()
            val height = binding.inputHeight.text.toString().trim()
            val weight = binding.inputWeight.text.toString().trim()
            if (idOfUser.toString() == "" || name == "" || birthDay == "" || sex == "" || height == "" || weight == "") {
                Toast.makeText(this, "Vui lòng không bỏ trống bất kỳ mục nào", Toast.LENGTH_SHORT)
                    .show()
            }
            if (idOfUser.toString() != "" && name != "" && birthDay != "" && sex != "" && height != "" && weight != "") {
                MySingleton.network.addNewRecord(idOfUser, name, birthDay, sex, height, weight)
                    .enqueue(object : Callback<NewRecord> {
                        override fun onResponse(
                            call: Call<NewRecord>,
                            response: Response<NewRecord>
                        ) {
                        }
                        override fun onFailure(call: Call<NewRecord>, t: Throwable) {}
                    })
                MySingleton.network.editDone(idOfUser,"1").enqueue(object : Callback<NewAccount>{
                    override fun onResponse(
                        call: Call<NewAccount>,
                        response: Response<NewAccount>
                    ) {}
                    override fun onFailure(call: Call<NewAccount>, t: Throwable) {}
                })
                startActivity(Intent(this, HomeActivity::class.java))
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Vui lòng không bỏ trống bất kỳ mục nào", Toast.LENGTH_SHORT)
                    .show()
            }
//            saveIdOfUser(getId)
        }
    }


    private fun runActivityAnother() {
        getSharedPreferences("app", Context.MODE_PRIVATE).edit {
            putBoolean("isComplete", true)
        }
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun openDialogOption(gravity: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_spinner_date_picker)

        val window: Window = dialog.window ?: return
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
            month = cal.get(Calendar.MONTH) + 1
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
                binding.inputBirthDate.text = date
            } else {
                binding.inputBirthDate.text = date
                Toast.makeText(this, date, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun saveIdOfUser(idOfUser: String){
//        val shared1 = getSharedPreferences("cookie1", MODE_PRIVATE)
//        val editor1 = shared1.edit()
//        val idOfUserToSkip = "idOfUserIs$idOfUser"
//        editor1.putString(idOfUserToSkip, idOfUser)
//        editor1.apply()
//        Log.e("id of user", idOfUserToSkip)
//    }
}