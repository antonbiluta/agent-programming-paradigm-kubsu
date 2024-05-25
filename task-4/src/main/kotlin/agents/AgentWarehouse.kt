package ru.biluta.agents

import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import ru.biluta.extractItemInfo
import ru.biluta.sendReply
import kotlin.random.Random

class AgentWarehouse : Agent() {
    private val inventory = mutableMapOf<String, Int>()
    private val location = mutableMapOf<String, String>()

    override fun setup() {
        val random = Random.Default
        for (i in 1..11) {
            inventory["Item$i"] = random.nextInt(5, 100)
            val rowPos = random.nextInt(1,4)
            val shelfPos = random.nextInt(1,4)
            location["Item$i"] = "Row$rowPos:Shelf$shelfPos"
        }
        addBehaviour(WarehouseBehaviour())
    }

    private inner class WarehouseBehaviour : CyclicBehaviour() {

        override fun action() {
            receive()?.let {
                val order = it.content
                val items = order.split(";")
                val response = StringBuilder()
                items.filter { item -> item.isNotBlank() }
                    .forEach { item ->
                        val parts = item.split(":")
                        val (itemName, requestedQuantity) = extractItemInfo(parts)
                        val availableQuantity = inventory.getOrDefault(itemName, 0)

                        if (availableQuantity >= requestedQuantity) {
                            inventory[itemName] = availableQuantity - requestedQuantity
                            response.append(itemName).append("=").append(location[itemName]).append(";")
                        } else {
                            val notification = ACLMessage(ACLMessage.INFORM)
                            notification.content = "$itemName is out of stock!"
                            notification.addReceiver(AID("manager", AID.ISLOCALNAME))
                            send(notification)
                        }
                    }
                sendReply(it, response)
            } ?: block()
        }
    }
}