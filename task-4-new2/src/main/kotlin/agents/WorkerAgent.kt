package ru.biluta.agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate
import ru.biluta.OrderUtils.parseOrder
import ru.biluta.registerInDF
import ru.biluta.searchAgent

class WorkerAgent : Agent() {
    private var isBusy: Boolean = false
    private lateinit var currentOrder: List<Pair<String, Int>>
    private val receivedLocations = mutableMapOf<String, String>()
    private val unavailableItems = mutableListOf<String>()
    private val insufficientItems = mutableListOf<Pair<String, Int>>()

    override fun setup() {
        println("Worker Agent ${aid.name} is ready.")
        registerInDF(aid, "worker", "Worker")
        addBehaviour(OrderProcessingBehaviour())
        addBehaviour(StatusResponderBehaviour())
    }

    private inner class OrderProcessingBehaviour : CyclicBehaviour() {
        override fun action() {
            if (!isBusy) {
                // Поиск склада
                val warehouseAgent = searchAgent(myAgent, "warehouse")
                if (warehouseAgent == null) {
                    println("${aid.name} could not find warehouse agent.")
                    isBusy = false
                    block()
                }

                val msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM))
                if (msg != null) {
                    isBusy = true
                    println("${aid.name} received order: ${msg.content}")
                    currentOrder = parseOrder(msg.content)
                    receivedLocations.clear()
                    unavailableItems.clear()
                    insufficientItems.clear()

                    // Запрос информации о местоположении всех товаров у склада
                    currentOrder.forEach { (item, quantity) ->
                        val locationQuery = ACLMessage(ACLMessage.QUERY_REF)
                        locationQuery.content = "$item $quantity шт"
                        locationQuery.addReceiver(warehouseAgent)
                        send(locationQuery)
                    }
                } else {
                    block()
                }
            } else {

                // Обработка ответа от агента-склада
                val reply = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM))
                if (reply != null) {
                    val content = reply.content
                    when {
                        content.contains("недоступен") -> unavailableItems.add(content.split(" ")[0])
                        content.contains("доступно") -> {
                            val parts = content.split(" ")
                            insufficientItems.add(parts[0] to parts[2].toInt())
                        }
                        else -> {
                            val parts = content.split(" ")
                            val item = parts[1]
                            val text = parts.filter {
                                parts.indexOf(it) > 3
                            }.joinToString(" ")
                            receivedLocations[item] = text
                        }
                    }

                    if (receivedLocations.size + unavailableItems.size + insufficientItems.size == currentOrder.size) {
                        if (unavailableItems.isEmpty() && insufficientItems.isEmpty()) {
                            currentOrder.forEach { (item, quantity) ->
                                println("${aid.name} is processing $item $quantity шт ${receivedLocations[item]}")
                                Thread.sleep(2000)
                                println("${aid.name} completed $item $quantity шт")
                            }
                            sendCompletionReport("Order completed: ${currentOrder.joinToString(", ") { "${it.first} ${it.second} шт" }}")
                        } else {
                            val unavailableMessage =
                                if (unavailableItems.isNotEmpty()) "Unavailable items: ${unavailableItems.joinToString(", ")}" else ""
                            val insufficientMessage = if (insufficientItems.isNotEmpty()) "Insufficient items: ${
                                insufficientItems.joinToString(", ") { "${it.first} доступно ${it.second} шт" }
                            }" else ""
                            sendCompletionReport("Order pending: $unavailableMessage $insufficientMessage")
                        }
                        isBusy = false
                    }
                } else {
                    block()
                }
            }
        }

        private fun sendCompletionReport(content: String) {
            val report = ACLMessage(ACLMessage.INFORM)
            report.addReceiver(searchAgent(myAgent,"warehouse-management")!!)
            report.content = content
            println("${aid.name} send report: $content")
            send(report)

        }
    }

    private inner class StatusResponderBehaviour : CyclicBehaviour() {
        override fun action() {
            val msg = receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF))
            if (msg != null) {
                val reply = msg.createReply()
                reply.performative = if (isBusy) ACLMessage.DISCONFIRM else ACLMessage.CONFIRM
                send(reply)
            } else {
                block()
            }
        }
    }
}