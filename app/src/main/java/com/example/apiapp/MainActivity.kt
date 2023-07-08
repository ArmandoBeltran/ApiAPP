package com.example.apiapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin   : Button
    lateinit var tilEmail    : TextInputEditText
    lateinit var tilPassword : TextInputEditText

    var clave = ""
    var valor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tilEmail = findViewById(R.id.login_user)
        tilPassword = findViewById(R.id.login_password)

        btnLogin = findViewById(R.id.login_button)

        btnLogin.setOnClickListener {
            val apiService = ApiClient.getClient()

            val body : LoginRequest = LoginRequest(tilEmail.text.toString(),tilPassword.text.toString())

            val call = apiService.login(body)
            call.enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful) {
                        val loginResponse = response.body()
                        clave = "token"
                        valor = loginResponse?.token!!
                        escribirDatosAlmacenamientoCache(this, clave, valor)
                        Toast.makeText(applicationContext, valor, Toast.LENGTH_LONG).show()
                        Toast.makeText(applicationContext, "Todo OK", Toast.LENGTH_LONG).show()
                    }else{
                        //Manejar el error de respuesta
                        Toast.makeText(applicationContext, "Error OK", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de red", Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    fun escribirDatosAlmacenamientoCache(context: Context, clave: String, valor: String) {
        val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(clave, valor)
        editor.apply()
    }

    fun leerDatosAlmacenamientoCache(context: Context, clave: String): String? {
        val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        return sharedPreferences.getString(clave, null)
    }
}