package com.example.healthapp.dialog

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.example.healthapp.R
import com.example.healthapp.activity.LoginActivity
import com.example.healthapp.databinding.FragmentLayoutBottomSheetProfileBinding
import com.example.healthapp.model.BaseAccount
import com.example.healthapp.model.BaseNewRecord
import com.example.healthapp.mySingleton.MySingleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentLayoutBottomSheetProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLayoutBottomSheetProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        requireView().viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as? BottomSheetDialog
            val bottomSheet =
                dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val height = (requireContext().resources.displayMetrics.heightPixels * 0.96).toInt()
            bottomSheet?.let {
                BottomSheetBehavior.from<FrameLayout>(bottomSheet).apply {
                    isDraggable = true
                    state = BottomSheetBehavior.STATE_COLLAPSED
                    setPeekHeight(height, true)
                    this.expandedOffset = 1
                    this.isHideable = false
                }
            }
        }
        loadData(idUser)

        binding.btnLabelDone.setOnClickListener {
            dismiss()
        }
        binding.btnStartIdMedical.setOnClickListener {
            findNavController().navigate(R.id.idMedicalFragment)
        }
        binding.btnLogOut.setOnClickListener {
            destroyCookie()
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialog

    private fun destroyCookie(){
        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val editor = shared?.edit()
        editor?.remove("idOfUser")
        editor?.apply()
        val i = Intent(requireContext(), LoginActivity::class.java)
        context?.startActivity(i)
        activity?.finish()
    }

    private fun loadData(idUser: Int){
        MySingleton.network.getDataNewRecord(idUser).enqueue(object : Callback<BaseNewRecord>{
            override fun onResponse(call: Call<BaseNewRecord>, response: Response<BaseNewRecord>) {
                response.body()?.resultNewRecord?.forEach {
                    binding.tvNameUser.text = it.name
                }
            }
            override fun onFailure(call: Call<BaseNewRecord>, t: Throwable) {}
        })
    }
}