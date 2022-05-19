package com.example.healthapp.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthapp.R
import com.example.healthapp.activity.HomeActivity
import com.example.healthapp.activity.NewRecordActivity
import com.example.healthapp.databinding.FragmentSignInBinding
import com.example.healthapp.model.BaseAccount
import com.example.healthapp.model.NewAccount
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<NewAccount>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = binding.inputUsername.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()
        MySingleton.network.getAccount().enqueue(object : Callback<BaseAccount> {
            override fun onResponse(call: Call<BaseAccount>, response: Response<BaseAccount>) {
                response.body()?.resultAccount?.forEach {
                    if (it.username == username && it.password == password) {
                        if (it.done == "1"){
                            saveIdOfUser(it.id.toString())
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                        }else{
                            val i = Intent(requireContext(), NewRecordActivity::class.java)
                            i.putExtra("iIdOfUser", it.id.toString())
                            saveIdOfUser(it.id.toString())
                            startActivity(i)
                        }
                        activity?.finish()
                    }
                    else {
                        binding.tvErr.text = "Tài khoản hoặc mật khẩu không chính xác"
                    }
                }
            }

            override fun onFailure(call: Call<BaseAccount>, t: Throwable) {}
        })

    }
    private fun saveIdOfUser(idOfUser: String){
        val shared = activity?.getSharedPreferences("cookie", MODE_PRIVATE)
        val editor = shared?.edit()
        editor?.putString("idOfUser", idOfUser)
        editor?.apply()
    }

    private fun checkDone(){
    }
}