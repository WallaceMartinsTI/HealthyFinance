package com.wcsm.healthyfinance.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.wcsm.healthyfinance.data.model.ItemCategory
import com.wcsm.healthyfinance.data.model.MyTopAppBarIcon
import com.wcsm.healthyfinance.ui.components.CircularLoading
import com.wcsm.healthyfinance.ui.components.MyTopAppBar
import com.wcsm.healthyfinance.ui.components.PrimaryButton
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.util.formatTimestamp
import com.wcsm.healthyfinance.ui.util.showToastMessage
import com.wcsm.healthyfinance.ui.util.toBrazilianDateFormat
import com.wcsm.healthyfinance.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val userData by profileViewModel.userData.collectAsState()

    val isLoading by profileViewModel.isLoading.collectAsState()
    val isLoadingUpdate by profileViewModel.isLoadingUpdate.collectAsState()
    val updateMessage by profileViewModel.updateMessage.collectAsState()

    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteAccountDialog by remember { mutableStateOf(false) }

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }

    var genderExpanded by remember { mutableStateOf(false) }

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

    if(showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Trash icon"
                )
            },
            title = { Text("Deletar Conta") },
            text = { Text("Você deseja deletar a sua conta?") },
            confirmButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "Cancelar"
                ) {
                    showDeleteAccountDialog = false
                }
            },
            dismissButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "Deletar"
                ) {
                    showDeleteAccountDialog = false
                    showConfirmDeleteAccountDialog = true
                }
            },
            containerColor = BackgroundColor,
            iconContentColor = Primary,
            titleContentColor = Primary,
            textContentColor = Color.White
        )
    }

    if(showConfirmDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteAccountDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Trash icon"
                )
            },
            title = { Text("Deletar Conta") },
            text = { Text("Sua conta será deletada permanentemente, você confirma a exclusão?") },
            confirmButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "NÃO"
                ) {
                    showConfirmDeleteAccountDialog = false
                }
            },
            dismissButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    textColor = ExpensePrimary.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, ExpensePrimary.copy(alpha = 0.8f)),
                    enabledText = "SIM"
                ) {
                    showConfirmDeleteAccountDialog = false
                    profileViewModel.deleteUserFirestore(navController)
                }
            },
            containerColor = BackgroundColor,
            iconContentColor = Primary,
            titleContentColor = Primary,
            textContentColor = Color.White
        )
    }

    LaunchedEffect(userData) {
        val userBirthDate = userData?.birthDate
        name = userData?.name.toString()
        email = userData?.email.toString()
        selectedDate = formatTimestamp(userBirthDate)
        gender = userData?.gender.toString()
    }

    LaunchedEffect(updateMessage) {
        if(updateMessage.isNotEmpty()) {
            showToastMessage(context, updateMessage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            MyTopAppBar(
                MyTopAppBarIcon.ARROW_LEFT
            ) {
                navController.popBackStack()
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Account Icon",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = "PERFIL",
                    color = Primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if(isLoading) {
                CircularLoading(
                    modifier = Modifier.fillMaxWidth(),
                    size = 80.dp
                )
            } else {
                Column(
                    modifier = Modifier
                        .width(280.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Nome",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            if(name.length <= 50) {
                                name = it
                            }
                        },
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = outlinedTextFieldColors,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
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
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )

                    Text(
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                        text = "Email",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = outlinedTextFieldColors,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "Account Circle",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Lock icon",
                                tint = Color.Gray
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        readOnly = true
                    )

                    Text(
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                        text = "Senha",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        value = "dummy data pasword",
                        onValueChange = {},
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = outlinedTextFieldColors,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Account Circle",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Lock icon",
                                tint = Color.Gray
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        readOnly = true
                    )

                    Text(
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                        text = "Data de Nascimento",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )

                    if(showDatePickerDialog) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePickerDialog = false },
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
                                    Text(text = "Escolher data")
                                }
                            }) {
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
                                    focusManager.clearFocus(force = true)
                                }
                            },
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = outlinedTextFieldColors,
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

                    Text(
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                        text = "Gênero",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )

                    Box {
                        ExposedDropdownMenuBox(
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = !genderExpanded }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                value = gender,
                                onValueChange = {
                                    genderExpanded = !genderExpanded
                                },
                                textStyle = TextStyle(color = Color.LightGray),
                                colors = outlinedTextFieldColors,
                                singleLine = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector =
                                        if(genderExpanded) Icons.Filled.KeyboardArrowUp
                                        else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "Add icon",
                                        tint = Color.Gray
                                    )
                                },
                                readOnly = true,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
                            )

                            ExposedDropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                val selectGenderList = listOf(
                                    ItemCategory("Masculino", Icons.Filled.Male),
                                    ItemCategory("Feminino", Icons.Filled.Female),
                                    ItemCategory("Outros", Icons.Filled.AccountCircle),
                                    ItemCategory("Prefiro não informar", Icons.Filled.Close),
                                )

                                selectGenderList.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = it.title)
                                        },
                                        onClick = {
                                            gender = it.title
                                            genderExpanded = false
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

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        modifier = Modifier
                            .width(280.dp),
                        enabled = !isLoadingUpdate,
                        enabledText = "SALVAR ALTERAÇÕES",
                        disabledText = "ATUALIZANDO...",
                        onClick = {
                            profileViewModel.setLoadingUpdate(true)

                            profileViewModel.updateUserProfile(
                                name = name,
                                birthDate = selectedDate,
                                gender = gender
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        modifier = Modifier
                            .width(280.dp),
                        enabledText = "DELETAR CONTA",
                        textColor = ExpensePrimary.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, ExpensePrimary.copy(alpha = 0.8f)),
                        onClick = {
                            showDeleteAccountDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}