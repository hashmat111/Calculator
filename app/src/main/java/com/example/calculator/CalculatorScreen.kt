package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Stack

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display Area
        DisplaySection(input = input, result = result)

        // Buttons Section
        ButtonGrid(
            onButtonClick = { symbol ->
                when (symbol) {
                    "=" -> {
                        result = evalExpression(input)
                        // Do not update the input with the result to prevent issues with further calculations
                    }
                    "C" -> {
                        input = ""
                        result = ""
                    }
                    else -> input += symbol
                }
            }
        )
    }
}

@Composable
fun DisplaySection(input: String, result: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = input,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = result,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ButtonGrid(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        listOf("C", "+/-", "%", "/"),
        listOf("7", "8", "9", "x"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        buttons.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { symbol ->
                    CalculatorButton(
                        symbol = symbol,
                        modifier = Modifier.weight(
                            if (symbol == "0") 2f else 1f,
                            fill = true
                        ),
                        onClick = { onButtonClick(symbol) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(symbol: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val backgroundColor = when (symbol) {
        "C", "+/-", "%" -> Color.Gray
        "/", "x", "-", "+", "=" -> Color(0xFFFF9500)
        else -> Color.DarkGray
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = symbol,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

fun evalExpression(expression: String): String {
    return try {
        // Replace 'x' with '*' for multiplication in the expression
        val exp = ExpressionBuilder(expression.replace("x", "*")).build()
        val result = exp.evaluate()
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}