package com.wcsm.healthyfinance.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wcsm.healthyfinance.data.model.Bill
import com.wcsm.healthyfinance.data.model.CategoryAndTotal
import com.wcsm.healthyfinance.data.model.HistoryItemType
import com.wcsm.healthyfinance.ui.components.BillContainer
import com.wcsm.healthyfinance.ui.components.MyBottomNavigationBar
import com.wcsm.healthyfinance.ui.components.PieChartScreen
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.InvestimentPrimary
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.util.formatTimestamp
import com.wcsm.healthyfinance.ui.util.toBRL
import com.wcsm.healthyfinance.ui.util.toBrazilianDateFormat
import com.wcsm.healthyfinance.ui.viewmodels.DetailViewModel
import com.wcsm.healthyfinance.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    detailViewModel: DetailViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val initialBill = backStackEntry.arguments?.getString("billType")

    val historyShown by remember { mutableStateOf(initialBill) }

    val userValues by homeViewModel.userValues.collectAsState()
    val userBills by homeViewModel.userBills.collectAsState()

    val chartChanged by detailViewModel.chartChanged.collectAsState()
    val showGraphic by detailViewModel.showGraphic.collectAsState()

    var balance by rememberSaveable { mutableStateOf(0.0) }
    var incomes by rememberSaveable { mutableStateOf(0.0) }
    var expenses by rememberSaveable { mutableStateOf(0.0) }
    var investments by rememberSaveable { mutableStateOf(0.0) }

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember {
        mutableStateOf("")
    }

    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = BackgroundContainer,
        cursorColor = Primary,
        focusedBorderColor = Color.White,
        selectionColors = TextSelectionColors(
            Primary, Color.Transparent
        ),
    )

    LaunchedEffect(userValues) {
        incomes = userValues["INCOME"] ?: 0.0
        expenses = userValues["EXPENSE"] ?: 0.0
        investments = userValues["INVESTMENT"] ?: 0.0
        balance = incomes - (expenses + investments)
    }

    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            homeViewModel.updateUserValues(selectedDate)
        } else {
            homeViewModel.updateUserValues()
            detailViewModel.sendChartChange()
        }
    }

    LaunchedEffect(key1 = true) {
        when(historyShown) {
            "INCOME" -> detailViewModel.sendType(HistoryItemType.INCOME)
            "EXPENSE" -> detailViewModel.sendType(HistoryItemType.EXPENSE)
            "INVESTMENT" -> detailViewModel.sendType(HistoryItemType.INVESTMENT)
        }
    }

    val historyType by detailViewModel.historyType.collectAsState()

    LaunchedEffect(historyType) {
        detailViewModel.sendChartChange()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "E!CONTROL",
                color = Primary,
                fontSize = 32.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Saldo",
                    color = Primary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                val balanceColor = if(balance == 0.0) {
                    InvestimentPrimary
                } else if(balance < 0) {
                    ExpensePrimary
                } else {
                    Primary
                }
                Text(
                    text = balance.toBRL(),
                    color = balanceColor,
                    fontSize = 18.sp
                )
            }
            Divider()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BackgroundContainer),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(showDatePickerDialog) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePickerDialog = false
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                datePickerState
                                    .selectedDateMillis?.let { millis ->
                                        selectedDate = millis.toBrazilianDateFormat()
                                    }
                                showDatePickerDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            )
                        ) {
                            Text(
                                text = "Escolher data"
                            )
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            headlineContentColor = Primary,
                            weekdayContentColor = Primary,
                            currentYearContentColor = Primary,
                            selectedYearContainerColor = Primary,
                            disabledDayContentColor = Color.Red,
                            selectedDayContainerColor = Primary,
                            todayContentColor = Primary,
                            todayDateBorderColor = Primary
                        )
                    )
                }
            }

            OutlinedTextField(
                value = if(selectedDate.length > 3) selectedDate.substring(3)
                else selectedDate,
                label = {
                    Text(
                        text = "Filtro por mês",
                        color = Color.Gray
                    )
                },
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .onFocusEvent {
                        if (it.isFocused) {
                            showDatePickerDialog = true
                            focusManager.clearFocus(force = true)
                        }
                    },
                textStyle = TextStyle(color = Color.LightGray),
                colors = outlinedTextFieldColors,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Calendar icon",
                        tint = Color.White
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.TouchApp,
                        contentDescription = "Touch icon",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                readOnly = true
            )

            if(selectedDate.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear icon",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .clickable { selectedDate = "" },
                    tint = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            BillContainer(
                modifier = Modifier.padding(4.dp),
                title = "Receita",
                isSelected = historyType == HistoryItemType.INCOME,
                showRadioButton = true,
                isRadioButtonSelected =  historyType == HistoryItemType.INCOME,
                value = incomes,
                onClick = {
                    detailViewModel.sendType(HistoryItemType.INCOME)
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            BillContainer(
                modifier = Modifier.padding(4.dp),
                title = "Gastos",
                isSelected = historyType == HistoryItemType.EXPENSE,
                showRadioButton = true,
                isRadioButtonSelected =  historyType == HistoryItemType.EXPENSE,
                value = expenses,
                onClick = {
                    detailViewModel.sendType(HistoryItemType.EXPENSE)
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            BillContainer(
                modifier = Modifier.padding(4.dp),
                title = "Investimento",
                isSelected = historyType == HistoryItemType.INVESTMENT,
                isRadioButtonSelected =  historyType == HistoryItemType.INVESTMENT,
                showRadioButton = true,
                value = investments,
                onClick = {
                    detailViewModel.sendType(HistoryItemType.INVESTMENT)
                }
            )
        }

        var filteredBillsBySelectedDate = mutableListOf<Bill>()
        if(selectedDate.isNotEmpty()) {
            val filteredDate =  if(selectedDate.length > 3) selectedDate.substring(3) else ""
            if(filteredDate.isNotEmpty()) {
                userBills.map { bill ->
                    val formattedBillDate = formatTimestamp(bill.date)
                    val billDate = if(formattedBillDate.length > 3) formattedBillDate.substring(3)
                    else ""
                    if(billDate.isNotEmpty()) {
                        if(filteredDate == billDate) {
                            filteredBillsBySelectedDate.add(bill)
                        }
                    }
                }
            }
        } else {
            filteredBillsBySelectedDate = userBills.toMutableList()
        }

        var bills = emptyList<Bill>()
        val categoryTotals = mutableListOf<CategoryAndTotal>()

        if(historyType != null) {

            bills = filteredBillsBySelectedDate.filter { bill ->
                bill.billCategory.type == historyType.toString()
            }

            val filteredBills = filteredBillsBySelectedDate.filter { bill ->
                bill.billCategory.type == historyType.toString()
            }

            val groupedBills = filteredBills.groupBy { it.billCategory.name }
            groupedBills.forEach { (categoryName, bills) ->
                val totalValue = bills.sumOf { it.value }
                categoryTotals.add(CategoryAndTotal(categoryName, totalValue))
            }

            val totalValue = when(historyType) {
                HistoryItemType.INCOME -> incomes
                HistoryItemType.EXPENSE -> expenses
                HistoryItemType.INVESTMENT -> investments
                else -> 0.0
            }

            if(totalValue != 0.0) {
                val categoryTotalsWithPercentages = detailViewModel.calculateBillPercentages(
                    totalValue = totalValue,
                    categoryTotals = categoryTotals
                )

                AnimatedVisibility(visible = showGraphic) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PieChartScreen(historyType!!, chartChanged, categoryTotalsWithPercentages, selectedDate)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Detalhes",
                color = Color.White,
            )

            Icon(
                imageVector = if(showGraphic) Icons.Filled.KeyboardArrowUp
                else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Arrow icon",
                modifier = Modifier.clickable {
                    detailViewModel.sendShowGraphic(!showGraphic)
                },
                tint = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BackgroundContainer)
                .padding(top = 8.dp, bottom = 12.dp)
        ) {
            if(categoryTotals.isNotEmpty()) {

                items(categoryTotals) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                    ) {

                        val color = when(historyType) {
                            HistoryItemType.INCOME -> Primary
                            HistoryItemType.EXPENSE -> ExpensePrimary
                            HistoryItemType.INVESTMENT -> InvestimentPrimary
                            else -> Color.Transparent
                        }

                        RowItem(bills, it, color, selectedDate, detailViewModel)
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(2.dp)
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        MyBottomNavigationBar(navController)
    }
}

@Composable
fun RowItem(
    bills: List<Bill>,
    categoryAndTotal: CategoryAndTotal,
    color: Color,
    selectedDate: String,
    detailViewModel: DetailViewModel
) {
    val expandedItems by detailViewModel.expandedItems.collectAsState()
    val isItemExpanded = expandedItems[categoryAndTotal.name] ?: false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                detailViewModel.toggleItemExpansion(categoryAndTotal.name)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = categoryAndTotal.name,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = if(isItemExpanded) Icons.Default.KeyboardArrowDown
                    else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Arrow icon",
                    tint = color
                )

                Text(
                    text = categoryAndTotal.total.toBRL(),
                    color = color,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            if(isItemExpanded) {
                val categoryItems = bills.filter {
                    it.billCategory.name == categoryAndTotal.name
                }

                val billsToShow = detailViewModel.billsToShow(
                    bills = categoryItems,
                    date = selectedDate
                )

                val billsCount = billsToShow.size

                val interval1 = listOf(0, 1)
                val interval2 = listOf(2, 3)
                val itemHeight = when(billsCount) {
                    in interval1 -> 60
                    in interval2 -> 124
                    else -> 172
                }

                LazyColumn(
                    modifier = Modifier.height(itemHeight.dp)
                ) {
                    items(billsToShow) {
                        Divider()

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    it.description,
                                    color = color
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    it.value.toBRL(),
                                    color = color,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    it.date.toDate().time.toBrazilianDateFormat(),
                                    color = color
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}