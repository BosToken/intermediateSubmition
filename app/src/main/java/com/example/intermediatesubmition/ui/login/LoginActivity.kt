package com.example.intermediatesubmition.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.data.api.response.LoginResponse
import com.example.intermediatesubmition.data.api.retrofit.ApiConfig
import com.example.intermediatesubmition.data.database.UserPreferences
import com.example.intermediatesubmition.data.database.dataStore
import com.example.intermediatesubmition.data.model.UserViewModel
import com.example.intermediatesubmition.databinding.ActivityLoginBinding
import com.example.intermediatesubmition.di.component.Button.MyLoginButton
import com.example.intermediatesubmition.di.component.EditText.MyEditTextEmail
import com.example.intermediatesubmition.di.component.EditText.MyEditTextPassword
import com.example.intermediatesubmition.di.component.InputLayout.MyPasswordInputLayout
import com.example.intermediatesubmition.di.component.InputLayout.MyTextInputLayout
import com.example.intermediatesubmition.helper.UserModelFactory
import com.example.intermediatesubmition.ui.main.MainActivity
import com.example.intermediatesubmition.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var layoutEmail: MyTextInputLayout
    private lateinit var layoutPassword: MyPasswordInputLayout

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginBtn : MyLoginButton
    private lateinit var emailEditText : MyEditTextEmail
    private lateinit var passwordText : MyEditTextPassword
    private lateinit var imageView: ImageView
    private var email: String? = null
    private var password: String? = null

    private var emailCheck: Boolean = false
    private var passwordCheck: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutEmail = binding.myTextInputLayout
        layoutPassword = binding.myTextInputLayout1

        imageView = binding.imageView2
        loginBtn = binding.button
        emailEditText = binding.editTextTextEmailAddress
        passwordText = binding.editTextTextPassword
        val signupButton = binding.signUp

        setButtonLoginEnable(getEmailcheck(), getPasswordcheck())

        loginBtn.setOnClickListener{
            setEmail(emailEditText.text.toString())
            setPassword(passwordText.text.toString())

            if (!(getEmail().toString() == null && getPassword().toString() == null)){
                loginCheck(getEmail().toString(), getPassword().toString())
            }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonEnableCondition()
        playAnimation()
    }

    private fun buttonEnableCondition(){

        emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setEmailCheck(emailEditText.getCheckChar())
                setButtonLoginEnable(getEmailcheck(), getPasswordcheck())
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        passwordText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setPasswordCheck(passwordText.getCheckChar())
                setButtonLoginEnable(getEmailcheck(), getPasswordcheck())
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
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

    private fun loginCheck(email: String, password: String){
        showLoading(true)
        val pref = UserPreferences.getInstance(application.dataStore)
        val userViewModel = ViewModelProvider(this, UserModelFactory(pref)).get(UserViewModel::class.java)
        val intent = Intent(this, MainActivity::class.java)
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    val token = responseBody?.loginResult?.token.toString()
                    showLoading(false)
                    if (token != null){
                        userViewModel.saveToken(token)
                        startActivity(intent)
                    }
                }
                else{
                    showLoading(false)
                    showToast(response.message()).show()
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                showToast(t.message.toString())
            }
        })

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

    private fun setButtonLoginEnable(emailCondition: Boolean, passwordCondition: Boolean){
        loginBtn.isEnabled = emailCondition && passwordCondition
    }

    private fun showToast(response: String): Toast{
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(layoutEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(layoutPassword, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(loginBtn, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }

        AnimatorSet().apply {
            playSequentially(together, login)
            start()
        }
    }
}