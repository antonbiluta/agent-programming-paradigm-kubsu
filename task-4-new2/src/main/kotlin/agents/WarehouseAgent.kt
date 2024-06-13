package ru.biluta.agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate
import ru.biluta.OrderUtils.parseOrder
import ru.biluta.registerInDF
import kotlin.random.Random

class WarehouseAgent : Agent() {

    private lateinit var inventory: MutableMap<String, Int>

    override fun setup() {
        println("Warehouse Agent ${aid.name} is ready.")
        inventory = arguments[0] as MutableMap<String, Int>
        registerInDF(aid, "warehouse", "Warehouse")
        addBehaviour(InventoryQueryBehaviour())
    }

    private inner class InventoryQueryBehaviour : CyclicBehaviour() {
        override fun action() {
            val msg = receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF))
            if (msg != null) {
                val (item, quantity) = parseOrder(msg.content).first()
                val reply = msg.createReply()
                reply.performative = ACLMessage.INFORM
                val availableQuantity = inventory[item] ?: 0
                reply.content = when {
                    availableQuantity == 0 -> "$item недоступен"
                    availableQuantity < quantity -> "$item доступно $availableQuantity шт"
                    else -> {
                        val location = getItemLocation(item)
                        inventory[item] = availableQuantity - quantity
                        "Item $item $quantity шт is located at $location"
                    }
                }
                send(reply)
            } else {
                block()
            }
        }

        private fun getItemLocation(item: String): String {
            // Случайная имитация местоположения
            val row = Random.nextInt(1, 11)
            val shelf = Random.nextInt(1, 11)
            return "Row $row, Shelf $shelf"
        }
    }

}