package com.jortega.currencycomponents

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.jortega.currencycomponents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            rg.setOnCheckedChangeListener { _, checkedId ->
                btnValue.setOnClickListener {
                    val selectedRb: RadioButton = findViewById(checkedId)
                    tvFormattedValue.notDecimals = false
                    when (selectedRb) {
                        rb1 -> {
                            tvCleanValue.text = "${cet1.getCleanDouble()}"
                            tvFormattedValue.showFormattedValue(cet1.getCleanDouble())
                        }
                        rb2 -> {
                            tvCleanValue.text = "${cet2.getCleanDouble()}"
                            tvFormattedValue.showFormattedValue(cet2.getCleanDouble())
                        }
                        rb3 -> {
                            tvCleanValue.text = "${cet3.getCleanDouble()}"
                            tvFormattedValue.showFormattedValue(cet3.getCleanDouble())
                        }
                        rb4 -> {
                            tvCleanValue.text = "${cet4.getCleanDouble()}"
                            tvFormattedValue.showFormattedValue(cet4.getCleanDouble())
                        }
                        rb5 -> {
                            tvCleanValue.text = "${cet5.getCleanInt()}"
                            tvFormattedValue.notDecimals = true
                            tvFormattedValue.showFormattedValue(cet5.getCleanInt())
                        }
                        rb6 -> {
                            tvCleanValue.text = "${cet6.getCleanDouble()}"
                            tvFormattedValue.showFormattedValue(cet6.getCleanDouble())
                        }
                        else -> {}
                    }
                }
            }

            rb1.doFunction(cet1)
            rb2.doFunction(cet2)
            rb3.doFunction(cet3)
            rb4.doFunction(cet4, true)
            rb5.doFunction(cet5)
            rb6.doFunction(cet6)
        }
    }

    private fun RadioButton.doFunction(cet: CurrencyEditText, clear: Boolean = false) {
        this.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cet.apply {
                    isEnabled = true
                    focus()
                }
                imm.showSoftInput(cet, InputMethodManager.SHOW_IMPLICIT)
            } else {
                cet.disable(clear)
            }
        }
    }
}