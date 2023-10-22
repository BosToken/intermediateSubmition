package com.example.intermediatesubmition.ui.pages.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.intermediatesubmition.data.remote.response.Response
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.databinding.ActivityRegisterBinding
import com.example.intermediatesubmition.ui.component.Button.MyRegisterButton
import com.example.intermediatesubmition.ui.component.EditText.MyEditTextEmail
import com.example.intermediatesubmition.ui.component.EditText.MyEditTextName
import com.example.intermediatesubmition.ui.component.EditText.MyEditTextPassword
import com.example.intermediatesubmition.ui.component.InputLayout.MyPasswordInputLayout
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerBtn : MyRegisterButton
    private lateinit var emailEditText : MyEditTextEmail
    private lateinit var passwordText : MyEditTextPassword
    private lateinit var namedText : MyEditTextName
    private lateinit var layoutPassword : MyPasswordInputLayout
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null

    private var nameCheck: Boolean = false
    private var emailCheck: Boolean = false
    private var passwordCheck: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        namedText = binding.editTextTextName
        emailEditText = binding.editTextTextEmailAddress
        passwordText = binding.editTextTextPassword
        layoutPassword = binding.myTextInputLayout2
        registerBtn = binding.button

        setButtonRegisterEnable(getNamecheck(), getEmailcheck(), getPasswordcheck())

        registerBtn.setOnClickListener{
            setName(namedText.text.toString())
            setEmail(emailEditText.text.toString())
            setPassword(passwordText.text.toString())

            if(!(getName() == null && getPassword() == null && getEmail() == null)){
                register(getName().toString(), getEmail().toString(), getPassword().toString())
            }
        }
        buttonEnableCondition()
    }

    private fun buttonEnableCondition(){

        namedText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setNameCheck(namedText.getCheckChar())
                setButtonRegisterEnable(getNamecheck(), getEmailcheck(), getPasswordcheck())
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setEmailCheck(emailEditText.getCheckChar())
                setButtonRegisterEnable(getNamecheck(), getEmailcheck(), getPasswordcheck())
            }
            override fun afterTextChanged(s: Editable) {
            }
        })


        passwordText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setPasswordCheck(passwordText.getCheckChar())
                setButtonRegisterEnable(getNamecheck(), getEmailcheck(), getPasswordcheck())
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

    }

    private fun register(name: String, email: String, password: String){
        showLoading(true)
        val intent = Intent(this, LoginActivity::class.java)
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    showLoading(false)
                    startActivity(intent)
                }
                else{
                    showLoading(false)
                    showToast(response.message()).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                showLoading(false)
                showToast(t.message.toString())
            }
        })
    }
    private fun setNameCheck(condition: Boolean){
        this.nameCheck = condition
    }

    private fun getNamecheck(): Boolean{
        return this.nameCheck
    }

    private fun setEmailCheck(condition: Boolean){
        this.emailCheck = condition
    }

    private fun getEmailcheck(): Boolean{
        return this.emailCheck
    }

    private fun setPasswordCheck(condition: Boolean){
        this.passwordCheck = condition
    }

    private fun getPasswordcheck(): Boolean{
        return this.passwordCheck
    }

    private fun setName(name: String){
        this.name = name
    }

    private fun getName(): String?{
        return this.name
    }

    private fun setEmail(email: String){
        this.email = email
    }

    private fun getEmail(): String?{
        return this.email
    }

    private fun setPassword(password: String){
        this.password = password
    }

    private fun getPassword(): String?{
        return this.password
    }

    private fun setButtonRegisterEnable(nameCondition: Boolean, emailCondition: Boolean, passwordCondition: Boolean){
        registerBtn.isEnabled = nameCondition && emailCondition && passwordCondition
    }

    private fun showToast(response: String): Toast {
        return Toast.makeText(this, response, Toast.LENGTH_SHORT)
    }

    private fun showLoading(isLoading: Boolean) {
        val visible = View.VISIBLE
        val gone = View.GONE
        if (isLoading) {
            binding.progressBar.visibility = visible
            binding.button.isEnabled = false
        } else {
            binding.progressBar.visibility = gone
            binding.button.isEnabled = true
        }
    }

}