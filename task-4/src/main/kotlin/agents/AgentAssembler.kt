package ru.biluta.agents

import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate
import ru.biluta.extractItemInfo
import ru.biluta.sendReport
import ru.biluta.sendWarehouseQuery

class AgentAssembler : Agent() {
    override fun setup() {
        addBehaviour(AssembleBehaviour())
    }

    private inner class AssembleBehaviour : CyclicBehaviour() {
        override fun action() {
            val mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
            receive(mt)?.let {
                val order = it.content
                println("$localName получил разнарядку: $order")
                sendWarehouseQuery(order)
                receive()?.let { warehouseReply ->
                    println("$localName получил информацию от склада: ${warehouseReply.content}")
                    val items = order.split(";")
                    items.filter { item -> item.isNotBlank() }
                        .forEach { item ->
                            val parts = item.split(":")
                            val (itemName, quantity) = extractItemInfo(parts)
                            println("$localName комплектует $quantity единиц товара $itemName")
                            // Эмулируем типо комплектование товара происходит
                            Thread.sleep(1000 * quantity.toLong())
                        }
                    sendReport("Order completed: $order")
                    println("$localName отправил отчет менеджеру о выполнении заказа: $order")
                } ?: block()
            } ?: block()
        }
    }
}