package com.furkansoyleyici.tipapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.furkansoyleyici.tipapplication.ui.widgets.BillInputTextField
import com.furkansoyleyici.tipapplication.ui.widgets.RoundIconButton
import com.furkansoyleyici.tipapplication.util.CalculateUtils


@Preview(showBackground = true)
@Composable
fun MainPage(
    modifier: Modifier = Modifier
) {
    val totalPerPersonState = remember { mutableDoubleStateOf(0.0) }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TotalPerPersonCard(totalPerPersonState = totalPerPersonState.value)
        BillTipCard(totalPerPersonState = totalPerPersonState)
    }
}

@Composable
private fun TotalPerPersonCard(
    totalPerPersonState: Double = 0.0,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(CornerSize(20.dp))),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val formatTotalPerPerson = "%.2f".format(totalPerPersonState)

            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$$formatTotalPerPerson",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun BillTipCard(
    modifier: Modifier = Modifier,
    totalPerPersonState: MutableState<Double>
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val totalBillState = remember { mutableStateOf("") }
    val validState = remember(totalBillState.value) { totalBillState.value.trim().isNotEmpty() }
    val splitByState = remember { mutableIntStateOf(1) }
    val sliderPositionsState = remember { mutableFloatStateOf(0f) }
    val tipPercentage = (sliderPositionsState.floatValue * 100).toInt()
    val tipAmountState = remember { mutableDoubleStateOf(0.0) }

    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            BillInputTextField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                imeAction = ImeAction.Done,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    totalPerPersonState.value = CalculateUtils.calculateTotalPerPerson(
                        totalBill = totalBillState.value.toDouble(),
                        splitBy = splitByState.intValue,
                        tipPercentage = tipPercentage
                    )
                    keyboardController?.hide()
                }
            )
            BillSplitRow(
                validState = validState,
                splitByState = splitByState,
                totalPerPersonState = totalPerPersonState,
                totalBillState = totalBillState,
                tipPercentage = tipPercentage
            )

            TipContent(
                sliderPositionsState = sliderPositionsState,
                validState = validState,
                totalBillState = totalBillState,
                tipPercentage = tipPercentage,
                tipAmountState = tipAmountState,
                totalPerPersonState = totalPerPersonState,
                splitByState = splitByState
            )
        }
    }
}

@Composable
fun BillSplitRow(
    modifier: Modifier = Modifier,
    validState: Boolean,
    splitByState: MutableState<Int>,
    totalPerPersonState: MutableState<Double>,
    totalBillState: MutableState<String>,
    tipPercentage: Int

) {
    val range = IntRange(start = 1, endInclusive = 100)

    if (validState) {
        Row(
            modifier = modifier.padding(3.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Split",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(120.dp))
            Row(
                modifier = Modifier.padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.End
            ) {
                RoundIconButton(
                    imageVector = Icons.Default.Remove,
                    backgroundColor = Color.LightGray,
                    onClick = {
                        splitByState.value =
                            if (splitByState.value > 1) splitByState.value - 1 else 1
                        totalPerPersonState.value = CalculateUtils.calculateTotalPerPerson(
                            totalBillState.value.toDouble(),
                            splitByState.value,
                            tipPercentage
                        )
                    }
                )
                Text(
                    text = "${splitByState.value}",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                    ),
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 20.dp, end = 20.dp)
                )
                RoundIconButton(
                    imageVector = Icons.Default.Add,
                    backgroundColor = Color.LightGray,
                    onClick = {
                        if (splitByState.value < range.last) {
                            splitByState.value = splitByState.value + 1
                            totalPerPersonState.value = CalculateUtils.calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitByState.value,
                                tipPercentage
                            )
                        }

                    })
            }

        }
    }
}

@Composable
fun TipContent(
    sliderPositionsState: MutableState<Float>,
    validState: Boolean,
    totalBillState: MutableState<String>,
    tipPercentage: Int,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    splitByState: MutableState<Int>

) {
    if (validState) {
        TipRow(tipAmountState = tipAmountState)
        TipBar(
            totalBillState = totalBillState,
            sliderPositionsState = sliderPositionsState,
            tipPercentage = tipPercentage,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState,
            splitByState = splitByState
        )
    }
}

@Composable
private fun TipRow(
    modifier: Modifier = Modifier,
    tipAmountState: MutableState<Double>
) {
    Row(
        modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp)

    ) {
        Text(
            text = "Tip",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier.align(alignment = Alignment.CenterVertically)
        )
        Spacer(modifier = modifier.width(180.dp))
        Text(
            text = "${tipAmountState.value}",
            style = TextStyle(
                Color.Black,
                fontSize = 20.sp
            ),
            modifier = modifier.align(Alignment.CenterVertically)

        )

    }
}

@Composable
private fun TipBar(
    modifier: Modifier = Modifier,
    totalBillState: MutableState<String>,
    sliderPositionsState: MutableState<Float>,
    tipPercentage: Int,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    splitByState: MutableState<Int>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$tipPercentage%")

        Slider(
            modifier = modifier.padding(horizontal = 15.dp),
            value = sliderPositionsState.value,
            onValueChange = {
                sliderPositionsState.value = it
                if (totalBillState.value.isNotEmpty()) {
                    val bill = totalBillState.value.toDoubleOrNull() ?: 0.0
                    tipAmountState.value = bill * sliderPositionsState.value
                    totalPerPersonState.value = CalculateUtils.calculateTotalPerPerson(
                        bill,
                        splitByState.value,
                        (sliderPositionsState.value * 100).toInt()
                    )
                }
            }, onValueChangeFinished = {
                tipAmountState.value =
                    CalculateUtils.calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)
                totalPerPersonState.value = CalculateUtils.calculateTotalPerPerson(
                    totalBillState.value.toDouble(),
                    splitByState.value,
                    tipPercentage
                )
            }
        )
    }
}

