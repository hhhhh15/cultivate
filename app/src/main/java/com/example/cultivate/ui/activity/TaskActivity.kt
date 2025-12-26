package com.example.cultivate.ui.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cultivate.databinding.TttaskactivityBinding

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: TttaskactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TttaskactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
