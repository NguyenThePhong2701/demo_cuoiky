package com.example.healthapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.healthapp.activity.HomeActivity
import com.example.healthapp.activity.LoginActivity
import com.example.healthapp.activity.NewRecordActivity
import com.example.healthapp.databinding.ActivityMainBinding
import com.example.healthapp.model.BaseAccount
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var getIdUser = 0
    private var idUser = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("app", Context.MODE_PRIVATE)
        val isComplete = sharedPreferences?.getBoolean("isComplete", false) ?: false

        val shared = getSharedPreferences("cookie", MODE_PRIVATE)
        val channel = shared.getString("idOfUser", null)
//        idUser = channel.toString().toInt()

        if (isComplete) {
            if (channel == null) {
                startLoginActivity()
            } else {
                MySingleton.network.getAccountFromId(channel.toString().toInt())
                    .enqueue(object : Callback<BaseAccount> {
                        override fun onResponse(
                            call: Call<BaseAccount>,
                            response: Response<BaseAccount>
                        ) {
                            response.body()?.resultAccount?.forEach {
                                getIdUser = it.done.toString().toInt()
                            }
                            if (getIdUser == 1){
                                startHomeActivity()
                            }else{
                                finish()
                                startNewRecordActivity(channel.toString().toInt())
                            }
                        }

                        override fun onFailure(call: Call<BaseAccount>, t: Throwable) {}
                    })
            }
        }

        binding.btnNext.setOnClickListener {
            getSharedPreferences("app", Context.MODE_PRIVATE)?.edit {
                putBoolean("isComplete", true)
            }
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun startHomeActivity() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun startNewRecordActivity(idUser: Int){
        val i = Intent(this, NewRecordActivity::class.java)
        i.putExtra("iIdOfUser", idUser.toString())
        startActivity(i)
    }

}