package com.wcsm.healthyfinance.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.wcsm.healthyfinance.data.model.Bill
import com.wcsm.healthyfinance.data.model.User
import com.wcsm.healthyfinance.data.repository.BillRepository
import com.wcsm.healthyfinance.data.repository.NetworkRepository
import com.wcsm.healthyfinance.data.repository.UserRepository
import com.wcsm.healthyfinance.ui.util.formatTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val billRepository: BillRepository,
    private val networkRepository: NetworkRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val TAG = "#FIREBASE_AUTH#"

    private val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading = _isScreenLoading.asStateFlow()

    private val _userValues = MutableStateFlow<Map<String, Double>>(emptyMap())
    val userValues: StateFlow<Map<String, Double>> = _userValues.asStateFlow()

    private val _isHistoricEmpty = MutableStateFlow(true)
    val isHistoricEmpty = _isHistoricEmpty

    private val _userBills = MutableStateFlow<List<Bill>>(emptyList())
    val userBills = _userBills

    private val _percentages = MutableStateFlow<Map<String, Double>>(emptyMap())
    val percentages = _percentages.asStateFlow()

    private val _showGraphic = MutableStateFlow(true)
    val showGraphic = _showGraphic

    private val _billToBeDeleted = MutableStateFlow<Bill?>(null)
    val billToBeDeleted = _billToBeDeleted

    private var _isSignout = MutableStateFlow(false)
    val isSignout = _isSignout.asStateFlow()

    private val _isConnected = MutableStateFlow(networkRepository.isConnected())
    val isConnected = _isConnected.asStateFlow()

    private val currentUser = auth.currentUser

    init {
        fetchUserData()
    }

    fun sendBillToBeDeleted(bill: Bill) {
        billToBeDeleted.value = bill
    }

    fun sendShowGraphic(show: Boolean) {
        _showGraphic.value = show
    }

    fun setScreenLoading(status: Boolean) {
        _isScreenLoading.value = status
    }

    fun signOut() {
        auth.signOut()
        _isSignout.value = true
    }

    fun checkConnection() {
        viewModelScope.launch {
            _isConnected.value = networkRepository.isConnected()
        }
    }

    fun showDialog(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun updateUserValues(date: String = "") {
        val filteredDate = if (date.length > 3) date.substring(3) else ""

        if (filteredDate.isNotEmpty()) {
            val filteredBills = userBills.value.filter {
                formatTimestamp(it.date).substring(3) == filteredDate
            }

            if (filteredBills.isNotEmpty()) {
                _userValues.value = calculateUserValues(filteredBills)
                val incomes = userValues.value["INCOME"] ?: 0.0
                val expenses = userValues.value["EXPENSE"] ?: 0.0
                val investments = userValues.value["INVESTMENT"] ?: 0.0
                _percentages.value = calculatePercentages(incomes, expenses, investments)
            }
        } else {
            _userValues.value = calculateUserValues(userBills.value)
            val incomes = userValues.value["INCOME"] ?: 0.0
            val expenses = userValues.value["EXPENSE"] ?: 0.0
            val investments = userValues.value["INVESTMENT"] ?: 0.0
            _percentages.value = calculatePercentages(incomes, expenses, investments)
        }
    }

    fun billsToShow(
        bills: List<Bill>,
        reversed: Boolean = false,
        date: String = ""
    ): List<Bill> {
        val filtered = date.isNotEmpty()
        val filteredDate =  if(date.length > 3) date.substring(3) else ""
        val filteredBills = mutableListOf<Bill>()

        if(filteredDate.isNotEmpty()) {
            bills.map {
                val formattedBillDate = formatTimestamp(it.date)
                val billDate = if(formattedBillDate.length > 3) formattedBillDate.substring(3)
                else ""
                if(billDate.isNotEmpty()) {
                    if(filteredDate == billDate) {
                        filteredBills.add(it)
                    }
                }
            }
        }

        if(filtered) {
            return if(filteredBills.isNotEmpty()) {
                if(reversed) filteredBills.reversed() else filteredBills
            } else {
                emptyList()
            }
        }

        return if(reversed) {
            bills.reversed()
        } else {
            bills
        }
    }

    fun calculatePercentages(
        income: Double,
        expense: Double,
        investment: Double
    ): Map<String, Double> {
        val total = income + expense + investment
        val incomePercentage = if (total != 0.0) (income / total) * 100 else 0.0
        val expensePercentage = if (total != 0.0) (expense / total) * 100 else 0.0
        val investmentPercentage = if (total != 0.0) (investment / total) * 100 else 0.0

        return mapOf(
            "INCOME" to incomePercentage,
            "EXPENSE" to expensePercentage,
            "INVESTMENT" to investmentPercentage
        )
    }

    fun deleteBillFrestore(billId: String) {
        if(currentUser != null) {
            viewModelScope.launch {
                billRepository.deleteBillFirestore(
                    currentUser = currentUser,
                    billId = billId
                )
                .addOnSuccessListener {
                    _userBills.value = _userBills.value.filterNot { it.id == billId }
                    _userValues.value = calculateUserValues(_userBills.value)
                    if(userBills.value.isEmpty()) {
                        resetUserData()
                    }
                    Log.i(TAG, "Bill DELETADA com Sucesso!")
                }.addOnFailureListener {
                    Log.i(TAG, "ERRO ao DELETAR Bill!")
                }
            }
        }
    }

    private fun resetUserData() {
        _isHistoricEmpty.value = true
        _userValues.value = mapOf(
            "INCOME" to 0.0,
            "EXPENSE" to 0.0,
            "INVESTMENT" to 0.0
        )
    }

    private fun calculateUserValues(bills: List<Bill>): Map<String, Double> {
        val income = bills.filter {
            it.billCategory.type == "INCOME"
        }.sumOf { it.value }

        val expense = bills.filter {
            it.billCategory.type == "EXPENSE"
        }.sumOf { it.value }

        val investment = bills.filter {
            it.billCategory.type == "INVESTMENT"
        }.sumOf { it.value }

        return mapOf(
            "INCOME" to income,
            "EXPENSE" to expense,
            "INVESTMENT" to investment
        )
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        if(currentUser != null) {
            viewModelScope.launch {
                userRepository.fetchUserData(currentUser)
                    .addOnSuccessListener {document ->
                        val user = document.toObject(User::class.java)
                        user?.let {
                            _isHistoricEmpty.value = user.bills.isEmpty()
                            val sortedBills = user.bills.sortedByDescending { bill -> bill.date  }
                            _userBills.value = sortedBills
                            _userValues.value = calculateUserValues(it.bills)
                        }
                        Log.i(TAG, "Busca de Usuário no FIRESTORE com SUCESSO!")
                        setScreenLoading(false)
                    }
                    .addOnFailureListener {
                        Log.i(TAG, "ERRO ao buscar USUÁRIO no FIRESTORE.")
                    }
            }
        }
    }
}