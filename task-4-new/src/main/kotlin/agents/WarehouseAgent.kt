package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage

class WarehouseAgent : Agent() {
    private val inventory = mutableMapOf<String, Int>()

    override fun setup() {
        println("Агент склада ${aid.name} готов.")
        addBehaviour(WarehouseHandler())

        // Инициализация склада
        initializeInventory()
    }

    inner class WarehouseHandler : CyclicBehaviour() {
        override fun action() {
            val msg = receive()
            if (msg != null && msg.performative == ACLMessage.REQUEST) {
                println("Агент склада ${aid.name} получил запрос на товар: ${msg.content}")
                val item = msg.content.split(":")[0].trim()
                val quantity = msg.content.split(":")[1].trim().toInt()

                if (inventory.containsKey(item) && inventory[item]!! >= quantity) {
                    inventory[item] = inventory[item]!! - quantity
                    val reply = msg.createReply()
                    reply.performative = ACLMessage.INFORM
                    reply.content = "Товар $item: $quantity шт. готов к отправке."
                    send(reply)
                    println("Товар $item: $quantity шт. отправлен.")
                } else {
                    val reply = msg.createReply()
                    reply.performative = ACLMessage.FAILURE
                    reply.content = "Недостаточно товара $item на складе."
                    send(reply)
                    println("Недостаточно товара $item на складе.")
                }
            } else {
                block()
            }
        }
    }

    private fun initializeInventory() {
        for (i in 1..10) {
            inventory["Товар $i"] = 10 // Изначально по 10 единиц каждого товара
        }
    }
}
