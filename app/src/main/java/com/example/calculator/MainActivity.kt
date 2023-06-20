package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateerror = false
    var lastdot = false

    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onallclearClick(view: View) {
        binding.datatv.text = ""
        binding.result.text=""
        stateerror=false
        lastdot = false
        lastNumeric= false
        binding.result.visibility = View.GONE
    }

    fun onEqualClick(view: View) {
        OnEqual()
        binding.datatv.text= binding.result.text.toString().drop( 1)
    }

    fun onDigitClick(view: View) {
        if(stateerror){
            binding.datatv.text = (view as Button).text
            stateerror = false
        } else{
            binding.datatv.append((view as Button).text)
        }
        lastNumeric = true
        OnEqual()
    }

    fun onOperatorClick(view: View) {
        if (!stateerror && lastNumeric) {
            val operator = (view as Button).text.toString()
            val expressionText = binding.datatv.text.toString()

            if (operator == "%") {
                try {
                    val expression = ExpressionBuilder(expressionText)
                        .build()

                    val result = expression.evaluate()
                    val percentage = result / 100

                    binding.result.visibility = View.VISIBLE
                    binding.result.text = "=" + percentage.toBigDecimal().stripTrailingZeros().toPlainString()

                    lastNumeric = true
                    lastdot = false
                } catch (ex: Exception) {
                    binding.result.text = "Error"
                    stateerror = true
                    lastNumeric = false
                    Log.e("expression error", ex.toString())
                }
            } else {
                binding.datatv.append(operator)
                lastNumeric = false
                lastdot = false
            }
        }
    }



    fun onBackClick(view: View) {
        binding.datatv.text = binding.datatv.text.toString().dropLast(1)
        try {
            val lastChar = binding.datatv.text.toString().last()

            if (lastChar.isDigit()) {
                OnEqual()
            }
        }catch (e:Exception){
            binding.result.text =""
            binding.result.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {
        binding.datatv.text = ""
        lastNumeric = false
    }
    fun OnEqual() {
        if (lastNumeric && !stateerror) {
            val txt = binding.datatv.text.toString()

            // Handle percentage operator
            val modifiedText = txt.replace("%", "/100*")

            expression = ExpressionBuilder(modifiedText).build()
            try {
                var result = expression.evaluate()

                // Check if the result is a whole number
                val roundedResult = if (result % 1 == 0.0) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }

                // Check if the expression contains a percentage calculation
                if (txt.contains("%")) {
                    // Extract the percentage value from the expression
                    val percentageValue = txt.substring(txt.indexOf("%") - 2, txt.indexOf("%") - 1).toDouble()

                    // Calculate the actual percentage result
                    result = result * percentageValue / 100
                }

                binding.result.visibility = View.VISIBLE
                binding.result.text = "= $roundedResult"

            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.result.text = "error"
                stateerror = true
                lastNumeric = false
            }
        }
    }




}