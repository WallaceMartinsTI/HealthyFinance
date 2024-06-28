package com.wcsm.healthyfinance.ui.view

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.wcsm.healthyfinance.data.model.HistoryItemType
import com.wcsm.healthyfinance.data.model.ItemCategory
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.components.CircularLoading
import com.wcsm.healthyfinance.ui.components.ErrorContainer
import com.wcsm.healthyfinance.ui.components.MyBottomNavigationBar
import com.wcsm.healthyfinance.ui.components.MyTopAppBar
import com.wcsm.healthyfinance.ui.components.PrimaryButton
import com.wcsm.healthyfinance.ui.components.SimpleBill
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.InvestimentPrimary
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.util.CurrencyVisualTransformation
import com.wcsm.healthyfinance.ui.util.showToastMessage
import com.wcsm.healthyfinance.ui.util.toBrazilianDateFormat
import com.wcsm.healthyfinance.ui.util.toHistoryItemType
import com.wcsm.healthyfinance.ui.viewmodels.AddBillViewModel

private val incomeCategoryItems = listOf(
    ItemCategory("Salário", Icons.Filled.AttachMoney),
    ItemCategory("Freelance", Icons.Filled.WorkOff),
    ItemCategory("Vendas", Icons.Filled.Sell),
    ItemCategory("Reembolsos", Icons.Filled.SaveAlt),
    ItemCategory("Prêmios/Bonificações", Icons.Filled.EmojiEvents),
    ItemCategory("Aluguel", Icons.Filled.OtherHouses),
    ItemCategory("Empréstimo", Icons.Filled.Work),
    ItemCategory("Investimento", Icons.Filled.MonetizationOn),
    ItemCategory("Outros", Icons.Filled.AddCircle),
)

private val expenseCategoryItems = listOf(
    ItemCategory("Alimentação", Icons.Filled.Restaurant),
    ItemCategory("Transporte", Icons.Filled.Commute),
    ItemCategory("Moradia", Icons.Filled.House),
    ItemCategory("Saúde", Icons.Filled.HealthAndSafety),
    ItemCategory("Educação", Icons.Filled.Book),
    ItemCategory("Lazer", Icons.Filled.SportsTennis),
    ItemCategory("Vestuário", Icons.Filled.Checkroom),
    ItemCategory("Manutenção e Serviços", Icons.Filled.MiscellaneousServices),
    ItemCategory("Outros", Icons.Filled.AddCircle)
)

private val investmentCategoryItems = listOf(
    ItemCategory("Ações", Icons.Filled.ShowChart),
    ItemCategory("Fundos Imobiliários", Icons.Filled.Warehouse),
    ItemCategory("Renda Fixa", Icons.Filled.PriceCheck),
    ItemCategory("Criptoativos", Icons.Filled.CurrencyBitcoin),
    ItemCategory("Imóveis", Icons.Filled.EmojiTransportation),
    ItemCategory("Outros", Icons.Filled.AddCircle),
)

private val installmentOptions = listOf(
    "1x", "2x", "3x", "4x", "5x", "6x", "7x", "8x", "9x", "10x", "11x", "12x"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillScreen(
    navController: NavHostController,
    addBillViewModel: AddBillViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val addBillFormState by addBillViewModel.addBillFormState.collectAsState()
    val updateMessage by addBillViewModel.updateMessage.collectAsState()
    val billAdded by addBillViewModel.billAdded.collectAsState()

    val isConnected by addBillViewModel.isConnected.collectAsState()
    var errorMessage by remember { mutableStateOf("") }

    val isExpenseScreen = addBillFormState.type == HistoryItemType.EXPENSE.toString()

    val valueFocusRequest = remember { FocusRequester() }
    val descriptionFocusRequest = remember { FocusRequester() }

    var value by rememberSaveable {
        mutableStateOf("0")
    }

    var category by remember {
        mutableStateOf("Selecione uma categoria")
    }

    var installment by remember {
        mutableStateOf("Informe o número de parcelas")
    }

    var categoryExpanded by remember { mutableStateOf(false) }
    var installmentExpanded by remember { mutableStateOf(false) }

    var switchChecked by remember { mutableStateOf(false) }

    var returnClicked by remember { mutableStateOf(false) }

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember {
        mutableStateOf("")
    }

    var dateFieldErrorMessage by remember { mutableStateOf("") }
    var installmentFieldErrorMessage by remember { mutableStateOf("") }

    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = BackgroundContainer,
        cursorColor = Primary,
        focusedBorderColor = Color.White,
        selectionColors = TextSelectionColors(
            Primary, Color.Transparent
        ),
    )

    LaunchedEffect(billAdded) {
        if(billAdded) {
            installment = "0"
            category = "Selecione uma categoria"
            selectedDate = ""
            value = "0"
            addBillViewModel.updateAddBillFormState(
                addBillFormState.copy(
                    description = ""
                )
            )
        }
    }

    LaunchedEffect(updateMessage) {
        if(!addBillFormState.isLoading && updateMessage.isNotEmpty()) {
            showToastMessage(context, updateMessage)
        }
    }

    BackHandler {
        navController.navigate(Screen.Home.route)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        topBar = {
            Box(contentAlignment = Alignment.Center) {
                MyTopAppBar(returnIcon = true) {
                    if(!returnClicked) {
                        returnClicked = true
                        navController.navigate(Screen.Home.route)
                    }
                }
                Text(
                    text = "Cadastro",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        },
        bottomBar = {
            MyBottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SimpleBill(
                    title = "Receita",
                    color = Primary,
                    selected = addBillFormState.type == HistoryItemType.INCOME.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onClick = {
                        addBillViewModel.updateAddBillFormState(
                            addBillFormState.copy(
                                type = HistoryItemType.INCOME.toString(),
                                description = ""
                            )
                        )
                        category = "Selecione uma categoria"
                        selectedDate = ""
                        value = "0"
                        errorMessage = ""
                    }
                )

                SimpleBill(
                    title = "Gastos",
                    color = ExpensePrimary,
                    selected = addBillFormState.type == HistoryItemType.EXPENSE.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onClick = {
                        addBillViewModel.updateAddBillFormState(
                            addBillFormState.copy(
                                type = HistoryItemType.EXPENSE.toString(),
                                description = ""
                            )
                        )
                        switchChecked = false
                        installment = "0"
                        category = "Selecione uma categoria"
                        selectedDate = ""
                        value = "0"
                        errorMessage = ""
                    }
                )

                SimpleBill(
                    title = "Investimento",
                    color = InvestimentPrimary,
                    selected = addBillFormState.type == HistoryItemType.INVESTMENT.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onClick = {
                        addBillViewModel.updateAddBillFormState(
                            addBillFormState.copy(
                                type = HistoryItemType.INVESTMENT.toString(),
                                description = ""
                            )
                        )
                        category = "Selecione uma categoria"
                        selectedDate = ""
                        value = "0"
                        errorMessage = ""
                    }
                )
            }

            if(addBillFormState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularLoading(size = 80.dp)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Column(
                        modifier = Modifier.width(280.dp)
                    ) {
                        AddBillTextFieldWithError(
                            modifier = Modifier.focusRequester(valueFocusRequest),
                            fieldName = "Valor",
                            value = value,
                            onValueChange = {newValue ->
                                if(newValue.all { it.isDigit() }) {
                                    value = newValue.ifEmpty {
                                        ""
                                    }
                                }
                            },
                            errorMessage = addBillFormState.valueErrorMessage,
                            visualTransformation = CurrencyVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            unfocusedLabelColorExpression = addBillFormState.value.isNotEmpty(),
                            trailingIcon = {
                                if(value != "0") {
                                    if(value != "") {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear icon",
                                            tint = Color.Gray,
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clickable {
                                                    value = "0"
                                                    valueFocusRequest.requestFocus()
                                                }
                                        )
                                    }
                                }
                            }
                        )

                        AddBillTextFieldWithError(
                            modifier = Modifier.focusRequester(descriptionFocusRequest),
                            fieldName = "Descrição",
                            label = "Descrição",
                            value = addBillFormState.description,
                            onValueChange = { newValue ->
                                if(newValue.length <= 30) {
                                    addBillViewModel.updateAddBillFormState(
                                        addBillFormState.copy(
                                            description = newValue
                                        )
                                    )
                                }
                            },
                            isError = addBillFormState.descriptionErrorMessage != null,
                            errorMessage = addBillFormState.descriptionErrorMessage,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            unfocusedLabelColorExpression = addBillFormState.description.isNotEmpty(),
                            trailingIcon = {
                                if(addBillFormState.description != "") {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Clear icon",
                                        tint = Color.Gray,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clickable {
                                                addBillViewModel.updateAddBillFormState(
                                                    addBillFormState.copy(
                                                        description = ""
                                                    )
                                                )
                                                descriptionFocusRequest.requestFocus()
                                            }
                                    )
                                }
                            },
                            supportingText = {
                                Text(
                                    text = "${addBillFormState.description.length} / 50",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End
                                )
                            },
                            singleLine = true
                        )

                        Text(
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                            text = "Categoria",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                        )

                        Box {
                            ExposedDropdownMenuBox(
                                expanded = categoryExpanded,
                                onExpandedChange = { categoryExpanded = !categoryExpanded }
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = category,
                                    onValueChange = {
                                        categoryExpanded = !categoryExpanded
                                    },
                                    textStyle = TextStyle(color = Color.LightGray),
                                    colors = outlinedTextFieldColors,
                                    singleLine = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector =
                                            if(categoryExpanded) Icons.Filled.KeyboardArrowUp
                                            else Icons.Filled.KeyboardArrowDown,
                                            contentDescription = "Add icon",
                                            tint = Color.Gray
                                        )
                                    },
                                    readOnly = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
                                )

                                ExposedDropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false }
                                ) {
                                    val listToRender = when(addBillFormState.type.toHistoryItemType()) {
                                        HistoryItemType.INCOME -> incomeCategoryItems
                                        HistoryItemType.EXPENSE -> expenseCategoryItems
                                        HistoryItemType.INVESTMENT -> investmentCategoryItems
                                    }

                                    listToRender.forEach {
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = it.title)
                                            },
                                            onClick = {
                                                category = it.title
                                                categoryExpanded = false
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = it.icon,
                                                    contentDescription = "${it.title} icon"
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if(addBillFormState.categoryErrorMessage != null) {
                            ErrorContainer(errorMessage = addBillFormState.categoryErrorMessage!!)
                        }

                        Text(
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                            text = "Data",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Start
                        )

                        if(showDatePickerDialog) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    showDatePickerDialog = false
                                    focusManager.clearFocus(force = true)
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            datePickerState
                                                .selectedDateMillis?.let { millis ->
                                                    selectedDate = millis.toBrazilianDateFormat()
                                                }
                                            showDatePickerDialog = false
                                            focusManager.clearFocus(force = true)
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
                            value = selectedDate,
                            onValueChange = {},
                            modifier = Modifier
                                .onFocusEvent {
                                    if(it.isFocused) {
                                        showDatePickerDialog = true
                                    }
                                },
                            textStyle = TextStyle(color = Color.LightGray),
                            colors = outlinedTextFieldColors,
                            isError = dateFieldErrorMessage.isNotEmpty(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.CalendarMonth,
                                    contentDescription = "Account Circle",
                                    tint = Color.Gray
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit Pencil",
                                    tint = Color.Gray
                                )
                            },
                            readOnly = true
                        )

                        if(dateFieldErrorMessage.isNotEmpty()) {
                            ErrorContainer(errorMessage = dateFieldErrorMessage)
                        }

                        if(isExpenseScreen) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                                    text = "Parcelamento",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                )

                                Switch(
                                    modifier = Modifier.scale(1f),
                                    checked = switchChecked,
                                    onCheckedChange = {
                                        switchChecked = !switchChecked
                                        if(!switchChecked) {
                                            installment = "0"
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Primary,
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = BackgroundContainer,
                                    ),
                                )
                            }

                            if(switchChecked) {
                                Box {
                                    ExposedDropdownMenuBox(
                                        expanded = installmentExpanded,
                                        onExpandedChange = { installmentExpanded = !installmentExpanded }
                                    ) {
                                        OutlinedTextField(
                                            modifier = Modifier.menuAnchor(),
                                            value = installment,
                                            onValueChange = {
                                                installmentExpanded = !installmentExpanded
                                            },
                                            textStyle = TextStyle(color = Color.LightGray),
                                            colors = outlinedTextFieldColors,
                                            singleLine = true,
                                            isError = installmentFieldErrorMessage.isNotEmpty(),
                                            trailingIcon = {
                                                Icon(
                                                    imageVector =
                                                    if(installmentExpanded) Icons.Filled.KeyboardArrowUp
                                                    else Icons.Filled.KeyboardArrowDown,
                                                    contentDescription = "Add icon",
                                                    tint = Color.Gray
                                                )
                                            },
                                            readOnly = true,
                                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
                                        )

                                        ExposedDropdownMenu(
                                            expanded = installmentExpanded,
                                            onDismissRequest = { installmentExpanded = false }
                                        ) {
                                            installmentOptions.forEach {
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(text = it)
                                                    },
                                                    onClick = {
                                                        installment = it
                                                        installmentExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                                if(installmentFieldErrorMessage.isNotEmpty()) {
                                    ErrorContainer(errorMessage = installmentFieldErrorMessage)
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }

            if(errorMessage.isNotEmpty()) {
                ErrorContainer(
                    errorMessage = errorMessage,
                    errorIcon = {
                        Icon(
                            imageVector = Icons.Filled.WifiOff,
                            contentDescription = "Wifi Off Icon",
                            tint = Color.White
                        )
                    }
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .width(280.dp)
                    .padding(vertical = 24.dp),
                enabled = !addBillFormState.isLoading,
                enabledText = "CADASTRAR",
                disabledText = "SALVANDO...",
                onClick = {
                    errorMessage = ""

                    addBillViewModel.setBillAdded(false)

                    if(value.isEmpty()) value = "0"

                    dateFieldErrorMessage = ""
                    installmentFieldErrorMessage = ""

                    if(selectedDate.isEmpty()) {
                        dateFieldErrorMessage = "Você deve informar uma data."
                        focusManager.clearFocus(force = true)
                        return@PrimaryButton
                    }

                    var installmentForDatabase = 0
                    if(switchChecked) {
                        if(installment == "Informe o número de parcelas") {
                            installmentFieldErrorMessage = "Você deve informar as parcelas"
                            return@PrimaryButton
                        } else {
                            val formatedInstallment = installment.substring(0, installment.length - 1).toIntOrNull()
                            if(formatedInstallment != null) {
                                installmentForDatabase = formatedInstallment
                            } else {
                                installmentFieldErrorMessage = "Informe uma parcela válida."
                                return@PrimaryButton
                            }
                        }
                    }

                    addBillViewModel.checkConnection()

                    if(isConnected) {
                        addBillViewModel.updateAddBillFormState(
                            addBillFormState.copy(
                                value = value,
                                category = category,
                                installment = installmentForDatabase
                            )
                        )

                        addBillViewModel.saveBill(selectedDate)
                    } else {
                        errorMessage = "Sem conexão no momento, tente mais tarde."
                    }
                }
            )
        }
    }
}

@Composable
private fun AddBillTextFieldWithError(
    modifier: Modifier = Modifier,
    fieldName: String,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    unfocusedLabelColorExpression: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    supportingText: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Text(
        modifier = Modifier.padding(top = 12.dp),
        text = fieldName,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White
    )

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { if(label != null) Text(text = label) },
        textStyle = TextStyle(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = BackgroundContainer,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor =
            if (unfocusedLabelColorExpression) Primary else Color.Gray.copy(alpha = 0.8f),
            focusedBorderColor = Color.White,
            cursorColor = Primary,
            selectionColors = TextSelectionColors(
                Primary, Color.Transparent
            )
        ),
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        singleLine = singleLine,
        readOnly = readOnly
    )
    errorMessage?.let {
        ErrorContainer(errorMessage = it)
    }
}