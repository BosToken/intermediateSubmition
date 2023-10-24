package com.example.intermediatesubmition.ui.pages.store

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmition.data.remote.response.Response
import com.example.intermediatesubmition.data.remote.retrofit.ApiConfig
import com.example.intermediatesubmition.data.local.prefrence.UserPreferences
import com.example.intermediatesubmition.data.local.prefrence.dataStore
import com.example.intermediatesubmition.ui.viewmodel.UserViewModel
import com.example.intermediatesubmition.databinding.ActivityStoreBinding
import com.example.intermediatesubmition.ui.component.EditText.MyEditTextDescription
import com.example.intermediatesubmition.ui.factory.UserModelFactory
import com.example.intermediatesubmition.ui.pages.camera.CameraActivity
import com.example.intermediatesubmition.ui.pages.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.intermediatesubmition.ui.pages.login.LoginActivity
import com.example.intermediatesubmition.ui.pages.main.MainActivity
import com.example.intermediatesubmition.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreBinding
    private lateinit var uploadButton : Button
    private var imageStatus: Boolean = false
    private var descriptionStatus: Boolean = false
    private lateinit var descriptionEditText: MyEditTextDescription
    private var currentImageUri: Uri? = null
    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val gallery = binding.button2
        val cameraX = binding.button3
        uploadButton = binding.button
        val pref = UserPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, UserModelFactory(pref)).get(
            UserViewModel::class.java
        )
        descriptionEditText = binding.editTextTextMultiLine

        mainViewModel.getToken().observe(this){
                token ->
            if (token == "null"){
                toLogin()
            }
            else{
                gallery.setOnClickListener {
                    startGallery()
                }
                cameraX.setOnClickListener {
                    startCameraX()
                }
                uploadButton.setOnClickListener {
                    uploadImage(token, descriptionEditText.text.toString())
                }
            }
        }

        checkDescriptionStatus()
    }

    private fun checkDescriptionStatus(){
        setButtonUploadEnable(getDescriptionStatus(), getImageStatus())

        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    setDescriptionStatus(false)
                    setButtonUploadEnable(getDescriptionStatus(), getImageStatus())
                } else{
                    setDescriptionStatus(true)
                    setButtonUploadEnable(getDescriptionStatus(), getImageStatus())
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setDescriptionStatus(condition: Boolean){
        this.descriptionStatus = condition
    }

    private fun getDescriptionStatus(): Boolean{
        return this.descriptionStatus
    }

    private fun setImageStatus(condition: Boolean){
        this.imageStatus = condition
    }

    private fun getImageStatus(): Boolean{
        return this.imageStatus
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadImage(token: String, description: String) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            showLoading(true)

            val descripitonStory = description.toRequestBody("text/plain". toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val client = ApiConfig.getApiService().createStories("Bearer "+token, descripitonStory, multipartBody)
                val intent = Intent(this, MainActivity::class.java)
                client.enqueue(object : Callback<Response>{
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        if (response.isSuccessful){
                            showLoading(false)
                            startActivity(intent)

                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
            catch (e: HttpException){

            }
        } ?: showToast("Mohon Lengkapi Formnya")
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
            checkDescriptionStatus()
        }
    }

    private fun setButtonUploadEnable(condition: Boolean, conditionImage: Boolean){
        uploadButton.isEnabled = condition && conditionImage
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imageView4.setImageURI(it)
            setImageStatus(true)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}