package com.wcsm.healthyfinance.ui.view

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wcsm.healthyfinance.data.model.HistoryItemType
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.components.BarChartScreen
import com.wcsm.healthyfinance.ui.components.BillContainer
import com.wcsm.healthyfinance.ui.components.CircularLoading
import com.wcsm.healthyfinance.ui.components.MyBottomNavigationBar
import com.wcsm.healthyfinance.ui.components.PrimaryButton
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.InvestimentPrimary
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.util.toBRL
import com.wcsm.healthyfinance.ui.util.toBrazilianDateFormat
import com.wcsm.healthyfinance.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
    homeViewModel: HomeViewModel = hiltViewModel(),
    exitApp: () -> Unit = {}
) {
    val userDeletedString = backStackEntry.arguments?.getString("userDeleted") ?: "false"
    val userDeleted = userDeletedString == "true"

    var showFinishAppDialog by remember { mutableStateOf(false) }
    var showDeleteBillDialog by remember { mutableStateOf(false) }

    val isSignout by homeViewModel.isSignout.collectAsState()

    val isScreenLoading by homeViewModel.isScreenLoading.collectAsState()
    val showGraphic by homeViewModel.showGraphic.collectAsState()
    val userValues by homeViewModel.userValues.collectAsState()
    val userBills by homeViewModel.userBills.collectAsState()
    val isHistoricEmpty by homeViewModel.isHistoricEmpty.collectAsState()
    val billToBeDeleted by homeViewModel.billToBeDeleted.collectAsState()

    var balance by rememberSaveable { mutableStateOf(0.0) }
    var incomes by rememberSaveable { mutableStateOf(0.0) }
    var expenses by rememberSaveable { mutableStateOf(0.0) }
    var investments by rememberSaveable { mutableStateOf(0.0) }

    var incomePercentage by rememberSaveable { mutableStateOf(0.0f) }
    var expensePercentage by rememberSaveable { mutableStateOf(0.0f) }
    var investmentPercentage by rememberSaveable { mutableStateOf(0.0f) }

    var isUserWithoutValues by rememberSaveable { mutableStateOf(true) }
    var isReversedBillsHistoric by remember { mutableStateOf(false) }

    val percentages by homeViewModel.percentages.collectAsState()

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

    if(userDeleted) {
        navController.popBackStack(
            route = Screen.Welcome.route,
            inclusive = false
        )
    }

    LaunchedEffect(isHistoricEmpty) {
        if(isHistoricEmpty) {
            isUserWithoutValues = true
        }
    }

    LaunchedEffect(userValues) {
        incomes = userValues["INCOME"] ?: 0.0
        expenses = userValues["EXPENSE"] ?: 0.0
        investments = userValues["INVESTMENT"] ?: 0.0

        balance = incomes - (expenses + investments)

        homeViewModel.updateUserValues(selectedDate)
    }

    LaunchedEffect(selectedDate) {
        homeViewModel.updateUserValues(selectedDate)
    }

    LaunchedEffect(percentages) {
        incomePercentage = percentages["INCOME"]?.toFloat() ?: 0.0f
        expensePercentage = percentages["EXPENSE"]?.toFloat() ?: 0.0f
        investmentPercentage = percentages["INVESTMENT"]?.toFloat() ?: 0.0f

        if(incomePercentage != 0.0f || expensePercentage != 0.0f || investmentPercentage != 0.0f) {
            isUserWithoutValues = false
        }
    }

    BackHandler {
        showFinishAppDialog = true
    }

    LaunchedEffect(isSignout) {
        if(isSignout) {
            navController.navigate(Screen.Login.route)
        }
    }

    if(showFinishAppDialog) {
        AlertDialog(
            onDismissRequest = { showFinishAppDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout icon"
                )
            },
            title = { Text("Encerrar Sessão") },
            text = { Text("Deseja sair do app ou encerrar sua sessão?") },
            confirmButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "Sair do App"
                ) {
                    showFinishAppDialog = false
                    exitApp()
                }
            },
            dismissButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "Encerrar Sessão"
                ) {
                    showFinishAppDialog = false
                    homeViewModel.signOut()
                }
            },
            containerColor = BackgroundColor,
            iconContentColor = Primary,
            titleContentColor = Primary,
            textContentColor = Color.White
        )
    }

    if(showDeleteBillDialog) {
        if(billToBeDeleted != null) {
            val billDescription = "Descrição: ${billToBeDeleted!!.description}"
            val billValue = billToBeDeleted!!.value.toBRL()
            val billDate = "Data: ${billToBeDeleted!!.date.toDate().time.toBrazilianDateFormat()}"

            AlertDialog(
                onDismissRequest = { showDeleteBillDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Trash icon"
                    )
                },
                title = { Text("Deletar Conta") },
                text = {
                    Text(
                        text = "Deseja realmente deletar a seguinte conta?\n\n$billDescription\nValor: $billValue\n$billDate"
                    )
                },
                confirmButton = {
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabledText = "Cancelar"
                    ) {
                        showDeleteBillDialog = false
                    }
                },
                dismissButton = {
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabledText = "Excluir Conta"
                    ) {
                        showDeleteBillDialog = false
                        homeViewModel.deleteBillFrestore(billToBeDeleted!!.id)
                    }
                },
                containerColor = BackgroundColor,
                iconContentColor = Primary,
                titleContentColor = Primary,
                textContentColor = Color.White
            )
        }
    }

    if(isScreenLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularLoading(size = 80.dp)
        }
    } else {
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
                    text = "HEALTHY FINANCE",
                    color = Primary,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SALDO",
                        color = Primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
                    .padding(8.dp),
            ) {
                BillContainer(
                    modifier = Modifier.padding(4.dp),
                    title = "Receita",
                    value = incomes,
                    arrowEndIcon = true,
                    isSelected = true,
                    onClick = {
                        navController.navigate(Screen.Detail.route + "/INCOME")
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                BillContainer(
                    modifier = Modifier.padding(4.dp),
                    title = "Gastos",
                    value = expenses,
                    arrowEndIcon = true,
                    isSelected = true,
                    onClick = {
                        navController.navigate(Screen.Detail.route + "/EXPENSE")
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                BillContainer(
                    modifier = Modifier.padding(4.dp),
                    title = "Investimento",
                    value = investments,
                    arrowEndIcon = true,
                    isSelected = true,
                    onClick = {
                        navController.navigate(Screen.Detail.route + "/INVESTMENT")
                    }
                )
            }

            AnimatedVisibility(visible = showGraphic) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if(isUserWithoutValues) {
                            Text(
                                text = "Registre algumas contas para começar a ver o gráfico.",
                                color = Color(0xFFFFD700),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp),
                            )
                        } else {
                            if(showGraphic) {
                                BarChartScreen(incomePercentage, expensePercentage, investmentPercentage)
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Histórico",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp, end = 4.dp)
                )

                Icon(
                    imageVector = if(showGraphic) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Arrow icon",
                    modifier = Modifier.clickable {
                        homeViewModel.sendShowGraphic(!showGraphic)
                    },
                    tint = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.ChangeCircle,
                    contentDescription = "Change icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            isReversedBillsHistoric = !isReversedBillsHistoric
                        },
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(BackgroundContainer)
                    .padding(top = 8.dp, bottom = 12.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if(isHistoricEmpty) Arrangement.Center else Arrangement.Top
            ) {
                if(isHistoricEmpty) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Você ainda não tem contas registradas.",
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                } else {
                    homeViewModel.billsToShow(
                        bills = userBills,
                        reversed = isReversedBillsHistoric,
                        date = selectedDate
                    ).forEach {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp)
                        ) {
                            val color = when(it.billCategory.type) {
                                HistoryItemType.INCOME.toString() -> Primary
                                HistoryItemType.EXPENSE.toString() -> ExpensePrimary
                                HistoryItemType.INVESTMENT.toString() -> InvestimentPrimary
                                else -> Color.Transparent
                            }

                            Row {
                                Text(
                                    text = it.description,
                                    modifier = Modifier.weight(1f),
                                    color = color,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete icon",
                                    modifier = Modifier.clickable {
                                        //homeViewModel.deleteBillFrestore(it.id)
                                        homeViewModel.sendBillToBeDeleted(it)
                                        showDeleteBillDialog = true
                                    },
                                    tint = Color.Gray
                                )
                            }

                            Row {
                                Text(
                                    text = it.value.toBRL(),
                                    color = color,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = it.date.toDate().time.toBrazilianDateFormat(),
                                    color = color,
                                )
                            }
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

            Spacer(modifier = Modifier.height(4.dp))

            MyBottomNavigationBar(navController, false)
        }
    }
}