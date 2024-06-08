package com.wcsm.healthyfinance.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.wcsm.healthyfinance.data.model.Bill
import com.wcsm.healthyfinance.data.model.CategoryAndTotal
import com.wcsm.healthyfinance.data.model.HistoryItemType
import com.wcsm.healthyfinance.ui.util.formatTimestamp
import kotlinx.coroutines.flow.MutableStateFlow

class DetailViewModel : ViewModel() {
    private val _historyType = MutableStateFlow<HistoryItemType?>(null)
    val historyType = _historyType

    private val _expandedItems = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val expandedItems = _expandedItems

    private val _chartChanged = MutableStateFlow(false)
    val chartChanged = _chartChanged

    private val _showGraphic = MutableStateFlow(true)
    val showGraphic = _showGraphic

    fun sendType(type: HistoryItemType) {
        _historyType.value = type
    }

    fun sendShowGraphic(show: Boolean) {
        _showGraphic.value = show
    }

    fun sendChartChange() {
        _chartChanged.value = !chartChanged.value
    }

    fun toggleItemExpansion(description: String) {
        _expandedItems.value = _expandedItems.value.toMutableMap().apply {
            this[description] = !(this[description] ?: false)
        }
    }

    fun calculateBillPercentages(
        totalValue: Double,
        categoryTotals: MutableList<CategoryAndTotal>,
        isFiltered: Boolean = false
    ): List<CategoryAndTotal> {
        return if (!isFiltered) {
            val total = categoryTotals.sumOf { it.total }
            categoryTotals.map { CategoryAndTotal(it.name, it.total / total * 100) }
        } else {
            categoryTotals.map { CategoryAndTotal(it.name, it.total / totalValue * 100) }
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
}