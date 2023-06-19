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

    fun onOpertorClick(view: View) {
        if(!stateerror && lastNumeric){
            binding.datatv.append((view as Button).text)
            lastdot = false
            lastNumeric = false
            OnEqual()
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

    fun OnEqual(){
        if(lastNumeric && !stateerror) {

            val txt = binding.datatv.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                binding.result.visibility = View.VISIBLE
                binding.result.text = '=' + result.toString()

            } catch (ex:ArithmeticException){
                Log.e("evaluate error", ex.toString())
                binding.result.text = "error"
                stateerror = true
                lastNumeric = false
            }
        }
    }
}