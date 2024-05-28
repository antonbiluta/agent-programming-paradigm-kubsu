package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage

class DeliveryAgent : Agent() {
    override fun setup() {
        println("Агент по сборке и доставке товара ${aid.name} готов.")
        addBehaviour(DeliveryHandler())
    }

    inner class DeliveryHandler : CyclicBehaviour() {
        override fun action() {
            val msg = receive()
            if (msg != null && msg.performative == ACLMessage.REQUEST) {
                println("Агент доставки ${aid.name} получил разнарядку: ${msg.content}")
                val warehouseRequest = ACLMessage(ACLMessage.REQUEST)
                warehouseRequest.content = msg.content
                warehouseRequest.addReceiver(aid) // Заменить на агента склада
                send(warehouseRequest)
            } else {
                block()
            }
        }
    }
}
