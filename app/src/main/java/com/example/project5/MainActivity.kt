package com.example.project5

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.project5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        /*
        each radio button updates viewModel's source or new language.
         */
        binding.srcEnglish.setOnClickListener { viewModel.setSource("en") }
        binding.srcGerman.setOnClickListener { viewModel.setSource("de") }
        binding.srcSpanish.setOnClickListener { viewModel.setSource("es") }
        binding.srcAuto.setOnClickListener { viewModel.setSource("auto") }
        binding.tranEnglish.setOnClickListener { viewModel.setNew("en") }
        binding.tranGerman.setOnClickListener { viewModel.setNew("de") }
        binding.tranSpanish.setOnClickListener { viewModel.setNew("es") }

        /*
        whenever the input text is updated, the model translates the text. if auto is chosen,
        it also updates the language if another language is more likely.
         */
        viewModel.textChangeListener = { text ->
            binding.translatedText.text = text
            if (viewModel.auto) {
                viewModel.updateLanguage()
            }
        }
    }

}