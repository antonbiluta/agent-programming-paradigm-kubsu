package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import kotlin.random.Random

class WarehouseManagerAgent : Agent() {
    override fun setup() {
        println("Менеджер склада ${aid.name} готов.")
        addBehaviour(OrderHandler())
    }

    inner class OrderHandler : CyclicBehaviour() {
        override fun action() {
            val msg = receive()
            if (msg != null && msg.performative == ACLMessage.REQUEST) {
                println("Менеджер склада ${aid.name} получил заказ: ${msg.content}")
                val orders = generateOrders()
                for (order in orders) {
                    println("Менеджер отправляет разнарядку: $order")
                    val deliveryOrder = ACLMessage(ACLMessage.REQUEST)
                    deliveryOrder.content = order
                    deliveryOrder.addReceiver(aid) // Заменить на агента доставки
                    send(deliveryOrder)
                }
            } else {
                block()
            }
        }

        private fun generateOrders(): List<String> {
            val n = Random.nextInt(1, 11)
            val orders = mutableListOf<String>()
            for (i in 1..n) {
                val e = Random.nextInt(1, 11)
                orders.add("Товар $i: $e шт.")
            }
            return orders
        }
    }
}
