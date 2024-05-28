package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.core.behaviours.TickerBehaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage

class CompanyAgent : Agent() {
    private var targetCost: Int = 0
    private var targetProduction: Int = 0
    private var targetReliability: Int = 0

    private val manufacturers = mutableListOf<AID>()

    override fun setup() {
        val args = arguments
        if (args != null && args.isNotEmpty()) {
            targetCost = args[0] as Int
            targetProduction = args[1] as Int
            targetReliability = args[2] as Int
        }

        println("Фирма ${aid.name} готова. Требования: Стоимость: $targetCost, Производительность: $targetProduction, Надежность: $targetReliability")

        addBehaviour(object : TickerBehaviour(this, 10000) {
            override fun onTick() {
                val template = DFAgentDescription()
                val sd = ServiceDescription()
                sd.type = "equipment-supplier"
                template.addServices(sd)

                try {
                    val result = DFService.search(myAgent, template)
                    manufacturers.clear()
                    for (dfAgent in result) {
                        manufacturers.add(dfAgent.name)
                    }
                    println("Найдено заводов: ${manufacturers.size}")

                    if (manufacturers.isNotEmpty()) {
                        myAgent.addBehaviour(RequestPerformer())
                    }
                } catch (fe: Exception) {
                    fe.printStackTrace()
                }
            }
        })
    }

    inner class RequestPerformer : CyclicBehaviour() {
        private var bestManufacturer: AID? = null
        private var bestScore = Int.MAX_VALUE
        private var repliesCnt = 0
        private var step = 0

        override fun action() {
            when (step) {
                0 -> {
                    val cfp = ACLMessage(ACLMessage.CFP)
                    for (manufacturer in manufacturers) {
                        cfp.addReceiver(manufacturer)
                    }
                    cfp.conversationId = "equipment-trade"
                    cfp.replyWith = "cfp${System.currentTimeMillis()}"
                    send(cfp)
                    step = 1
                }
                1 -> {
                    val reply = receive()
                    if (reply != null) {
                        repliesCnt++
                        if (reply.performative == ACLMessage.PROPOSE) {
                            val content = reply.content.split(",")
                            val production = content[0].toInt()
                            val reliability = content[1].toInt()
                            val cost = content[2].toInt()
                            val score = calculateScore(production, reliability, cost)
                            if (score < bestScore) {
                                bestScore = score
                                bestManufacturer = reply.sender
                            }
                        }
                        if (repliesCnt >= manufacturers.size) {
                            step = 2
                        }
                    } else {
                        block()
                    }
                }
                2 -> {
                    val order = ACLMessage(ACLMessage.ACCEPT_PROPOSAL)
                    order.addReceiver(bestManufacturer)
                    order.conversationId = "equipment-trade"
                    order.replyWith = "order${System.currentTimeMillis()}"
                    send(order)
                    step = 3
                }
                3 -> {
                    val reply = receive()
                    if (reply != null) {
                        if (reply.performative == ACLMessage.INFORM) {
                            println("Оборудование успешно закуплено у завода ${reply.sender.name}")
                            doDelete()
                        }
                    } else {
                        block()
                    }
                }
            }
        }

        private fun calculateScore(production: Int, reliability: Int, cost: Int): Int {
            var score = 0
            if (cost <= targetCost) score++
            if (production >= targetProduction) score++
            if (reliability >= targetReliability) score++
            return score
        }
    }
}
