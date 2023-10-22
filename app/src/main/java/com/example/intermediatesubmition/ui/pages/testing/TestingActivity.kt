package com.example.intermediatesubmition.ui.pages.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intermediatesubmition.databinding.ActivityTestingBinding

class TestingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}