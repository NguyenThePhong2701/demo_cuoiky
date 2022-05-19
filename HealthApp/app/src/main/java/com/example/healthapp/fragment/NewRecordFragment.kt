package com.example.healthapp.fragment

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
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.healthapp.R
import com.example.healthapp.activity.HomeActivity
import com.example.healthapp.databinding.FragmentNewRecordBinding
import com.example.healthapp.extension.formatDate
import com.example.healthapp.model.NewRecord
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewRecordFragment : Fragment() {
    private var _binding: FragmentNewRecordBinding? = null
    private val binding get() = _binding!!

    var day = 0
    var month = 0
    var year = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0

    var date = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputBirthDate.setOnClickListener {
            openDialogOption(Gravity.CENTER)
        }
        binding.inputSex.setOnClickListener {
            val otherStrings = arrayOf("Nam", "Nữ", "Khác")
            val mBuilder = AlertDialog.Builder(requireContext())
            mBuilder.setItems(otherStrings) { dialogInterface: DialogInterface, i: Int ->
                binding.inputSex.setText(otherStrings[i])
            }
            val dialog = mBuilder.create();
            dialog.show()
        }
        binding.btnNext.setOnClickListener {
            val name = binding.inputName.text.toString()
            val birthDay = binding.inputBirthDate.text.toString()
            val sex = binding.inputSex.text.toString()
            val height = binding.inputHeight.text.toString()
            val weight = binding.inputWeight.text.toString()
//            MySingleton.network.addNewRecord(name, birthDay, sex, height, weight)
//                .enqueue(object : Callback<NewRecord> {
//                    override fun onResponse(call: Call<NewRecord>, response: Response<NewRecord>) {}
//
//                    override fun onFailure(call: Call<NewRecord>, t: Throwable) {}
//                })
//            runActivityAnother()
        }

    }

    private fun runActivityAnother() {
        activity?.getSharedPreferences("app", Context.MODE_PRIVATE)?.edit {
            putBoolean("isComplete", true)
        }
        startActivity(Intent(requireContext(), HomeActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun openDialogOption(gravity: Int) {
        val dialog = Dialog(requireContext())
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
                month = cal.get(Calendar.MONTH)
                year = cal.get(Calendar.YEAR)

                date = "$day/$month/$year"
                binding.inputBirthDate.text = date
            } else {
                binding.inputBirthDate.text = date
                Toast.makeText(requireContext(), date, Toast.LENGTH_SHORT).show()
            }
        }
    }

}