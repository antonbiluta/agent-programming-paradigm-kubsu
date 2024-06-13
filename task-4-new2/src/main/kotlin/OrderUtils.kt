package ru.biluta

import kotlin.random.Random

object OrderUtils {
    val items = listOf("огурцы", "помидоры", "яблоки", "бананы", "картофель", "морковь", "капуста", "лук", "чеснок", "грибы")

    fun generateOrder(): String {
        val numItems = Random.nextInt(1, 11)
        val availableItems = items.shuffled().take(numItems)
        val orderItems = availableItems.map { item ->
            val quantity = Random.nextInt(1, 11)
            "$item $quantity шт"
        }
        return orderItems.joinToString("; ")
    }

    fun parseOrder(order: String): List<Pair<String, Int>> {
        val regex = Regex("([а-яА-ЯёЁ]+) (\\d+) шт")
        return regex.findAll(order).map {
            val (item, quantity) = it.destructured
            item to quantity.toInt()
        }.toList()
    }
}