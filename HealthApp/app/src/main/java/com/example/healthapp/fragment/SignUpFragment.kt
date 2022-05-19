package com.example.healthapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthapp.databinding.FragmentSignUpBinding
import com.example.healthapp.model.NewAccount
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backSignIn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRegister.setOnClickListener {
            registerAccount()
            findNavController().navigateUp()
        }
    }

    private fun registerAccount() {
        val username = binding.inputUsername.text.toString().trim()
        val password1 = binding.inputPassword1.text.toString().trim()
        val password2 = binding.inputPassword2.text.toString().trim()

        if (username == "" || password1 == "" || password2 == "") {
            Toast.makeText(requireContext(), "Không được bỏ trống các mục trên", Toast.LENGTH_SHORT)
                .show()
        } else if (username != "" && password1 != "" && password2 != "") {
            if (password1 == password2) {
                MySingleton.network.addAccount(username, password1, "0")
                    .enqueue(object : Callback<NewAccount> {
                        override fun onResponse(
                            call: Call<NewAccount>,
                            response: Response<NewAccount>
                        ) {
                            Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<NewAccount>, t: Throwable) {}
                    })
            } else {
                Toast.makeText(requireContext(), "Hai mật khẩu không khớp", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}