package ru.biluta

fun extractItemInfo(itemInfo: List<String>): Pair<String, Int> {
    val itemName = itemInfo[0]
    val requestedQuantity = when (itemInfo.size == 2) {
        true -> itemInfo[1].let {
            when (it.isNotBlank()) {
                true -> it.toInt()
                else -> 0
            }
        }
        else -> 0
    }
    return Pair(itemName, requestedQuantity)
}