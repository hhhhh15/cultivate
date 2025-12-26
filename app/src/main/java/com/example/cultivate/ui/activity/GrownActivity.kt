package com.example.cultivate.ui.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cultivate.databinding.GrownfragmentBinding

class GrownActivity : AppCompatActivity() {

    private lateinit var binding: GrownfragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GrownfragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}