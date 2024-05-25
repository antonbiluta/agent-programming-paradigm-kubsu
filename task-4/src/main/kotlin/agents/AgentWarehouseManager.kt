package ru.biluta.agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import ru.biluta.extractItemInfo
import ru.biluta.sendOrderRequest
import ru.biluta.sendRestockRequest
import ru.biluta.sendStockQuantity
import kotlin.random.Random


class AgentWarehouseManager : Agent() {
    override fun setup() {
        addBehaviour(OrderProcessingBehaviour())
    }

    fun generateRandomOrder(): StringBuilder {
        val random = Random.Default
        val numItems = random.nextInt(1, 11)
        val order = StringBuilder()
        for (i in 1..numItems) {
            val quantity = random.nextInt(1, 11)
            order.append("Item$i").append(":").append(quantity).append(";")
        }
        return order
    }

    private inner class OrderProcessingBehaviour : CyclicBehaviour() {
        override fun action() {
            val order = generateRandomOrder()
            sendOrderRequest(order)
            receive()?.let {
                val reply = it.content
                println("$localName получил отчет: $reply")
                checkInventory(reply)
            } ?: block()
        }

        private fun checkInventory(report: String) {
            val items = report.split(";")
            items.filter { item -> item.isNotBlank() }
                .forEach { item ->
                    val parts = item.split(":")
                    val (itemName, quantity) = extractItemInfo(parts)
                    sendStockQuantity(itemName)
                    receive()?.let {
                        val stockLevel = try {
                            it.content.toInt()
                        } catch (e: NumberFormatException) {
                            -1 // на случай если пришло outOfStock
                        }
                        if (stockLevel < quantity) {
                            sendRestockRequest(itemName)
                            println("$localName отправил заявку на пополнение товара: $itemName")
                        }
                    } ?: block()
                }
        }
    }
}