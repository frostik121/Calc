package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView() // Установка представлений
    }

    private fun setUpView() {
        binding.buttonClear.setOnClickListener { clearInput() }

        val numberButtons = listOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8, binding.button9
        )

        numberButtons.forEach { button ->
            button.setOnClickListener { addToInputText(button.text.toString()) }
        }

        binding.buttonDot.setOnClickListener { addToInputText(".") }
        binding.buttonBracketLeft.setOnClickListener { addToInputText("(") }
        binding.buttonBracketRight.setOnClickListener { addToInputText(")") }

        // Математические операции с использованием символов ÷ и x
        binding.buttonDivision.setOnClickListener { addToInputText("÷") }
        binding.buttonMultiply.setOnClickListener { addToInputText("x") }
        binding.buttonSubtraction.setOnClickListener { addToInputText("-") }
        binding.buttonAddition.setOnClickListener { addToInputText("+") }

        // Вычисление результата
        binding.buttonEquals.setOnClickListener { showResult() }
        binding.buttonPercent.setOnClickListener { addToInputText("%") }
    }

    private fun clearInput() {
        binding.input.text = ""
        binding.output.text = ""
    }

    private fun addToInputText(value: String) {
        binding.input.append(value)
    }

    private fun getInputExpression(): String {
        return binding.input.text.toString()
    }

    private fun showResult() {
        try {
            // Заменяем символы перед вычислением
            val expression = getInputExpression()
                .replace("÷", "/")
                .replace("x", "*")
                .replace("%", "/100")

            val result = ExpressionBuilder(expression).build().evaluate()
            val formattedResult = DecimalFormat("0.######").format(result).toString()
            binding.output.text = formattedResult
            binding.output.setTextColor(ContextCompat.getColor(this, R.color.neon_green))

        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is ArithmeticException -> {
                Log.e("Calculator", "Ошибка при вычислении результата", e)
                binding.output.text = "Ошибка: деление на ноль"
            }
            else -> {
                Log.e("Calculator", "Неизвестная ошибка", e)
                binding.output.text = "Ошибка"
            }
        }
        binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
    }
}
