package ru.biluta.agents

import jade.core.Agent
import jade.core.behaviours.Behaviour
import jade.lang.acl.ACLMessage
import ru.biluta.OrderUtils.generateOrder
import ru.biluta.searchAgent

class OrderGeneratorAgent : Agent() {
    override fun setup() {
        val numOrders = arguments[0] as? Int ?: 1
        addBehaviour(OrderGeneratorBehaviour(numOrders))
    }

    private inner class OrderGeneratorBehaviour(private val numOrders: Int) : Behaviour() {
        private var ordersGenerated = 0

        override fun action() {
            if (ordersGenerated < numOrders) {
                val order = generateOrder()
                val orderMsg = ACLMessage(ACLMessage.REQUEST)
                orderMsg.content = order
                val warehouseManager = searchAgent(myAgent, "warehouse-management")

                if (warehouseManager != null) {
                    orderMsg.addReceiver(warehouseManager)
                    ordersGenerated++
                    send(orderMsg)
                } else {
                    println("No warehouse manager agent found.")
                }
            } else {
                block(1000)
            }
        }

        override fun done(): Boolean {
            return ordersGenerated >= numOrders
        }
    }
}