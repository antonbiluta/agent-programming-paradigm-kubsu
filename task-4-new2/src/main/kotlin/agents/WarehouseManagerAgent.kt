package ru.biluta.agents

import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate
import ru.biluta.registerInDF
import ru.biluta.searchAgents

class WarehouseManagerAgent : Agent() {

    override fun setup() {
        println("Warehouse Manager Agent ${aid.name} is ready.")
        registerInDF(aid, "warehouse-management", "WarehouseManagement")
        addBehaviour(OrderReceiverBehaviour())
        addBehaviour(ReportReceiverBehaviour())
    }

    private inner class OrderReceiverBehaviour : CyclicBehaviour() {
        private var indexOrder = 1
        override fun action() {
            val msg = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST))
            if (msg != null) {
                println("#${indexOrder++} | Received order request: ${msg.content}")

                val workerAgent = searchFreeWorkerAgent()
                if (workerAgent != null) {
                    val orderMsg = ACLMessage(ACLMessage.INFORM)
                    orderMsg.content = msg.content
                    orderMsg.addReceiver(workerAgent)
                    send(orderMsg)
                } else {
                    block(5000)
                    putBack(msg)
                }
            } else {
                block()
            }
        }

        fun searchFreeWorkerAgent(): AID? {
            val agents = searchAgents(myAgent,  "worker")
            return agents.firstOrNull { isWorkerFree(it) }
        }

        private fun isWorkerFree(workerAgent: AID): Boolean {
            val checkMsg = ACLMessage(ACLMessage.QUERY_IF)
            checkMsg.addReceiver(workerAgent)
            send(checkMsg)

            val reply = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM), 1000)
            return reply != null
        }
    }

    private inner class ReportReceiverBehaviour : CyclicBehaviour() {
        override fun action() {
            val msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM))
            if (msg != null) {
                println("Received report: ${msg.content}")
                if (msg.content.contains("Order pending")) {
                    println("Создан заказ на пополнение склада: ${msg.content}")
                }
            } else {
                block()
            }
        }
    }

}