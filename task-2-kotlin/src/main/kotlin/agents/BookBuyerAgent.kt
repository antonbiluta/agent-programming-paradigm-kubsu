package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.core.behaviours.OneShotBehaviour
import jade.core.behaviours.TickerBehaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage

class BookBuyerAgent : Agent() {
    private val targetBookTitle = "Книга1"
    private val sellerAgents = mutableListOf<AID>()

    override fun setup() {
        println("Покупатель ${aid.name} готов.")

        // Добавление поведения для поиска продавцов через определенные интервалы времени
        addBehaviour(object : TickerBehaviour(this, 10000) {
            override fun onTick() {
                println("Попытка купить $targetBookTitle")
                // Поиск продавцов
                val template = DFAgentDescription()
                val sd = ServiceDescription()
                sd.type = "book-selling"
                template.addServices(sd)

                try {
                    val result = DFService.search(myAgent, template)
                    sellerAgents.clear()
                    for (dfAgent in result) {
                        sellerAgents.add(dfAgent.name)
                    }
                    println("Найдено продавцов книг: ${sellerAgents.size}")

                    if (sellerAgents.isNotEmpty()) {
                        myAgent.addBehaviour(RequestPerformer())
                    }
                } catch (fe: Exception) {
                    fe.printStackTrace()
                }
            }
        })
    }

    // Поведение для запроса и обработки предложений от продавцов
    inner class RequestPerformer : Behaviour() {
        private var bestSeller: AID? = null
        private var bestPrice: Int = Integer.MAX_VALUE
        private var repliesCnt = 0
        private var step = 0

        override fun action() {
            when (step) {
                0 -> {
                    // Отправка запросов на предложение (CFP) всем продавцам
                    val cfp = ACLMessage(ACLMessage.CFP)
                    for (seller in sellerAgents) {
                        cfp.addReceiver(seller)
                    }
                    cfp.content = targetBookTitle
                    cfp.conversationId = "book-trade"
                    cfp.replyWith = "cfp${System.currentTimeMillis()}"
                    myAgent.send(cfp)
                    step = 1
                }
                1 -> {
                    // Обработка всех ответов (PROPOSE, REFUSE) от продавцов
                    val replies = myAgent.receive()
                    if (replies != null) {
                        repliesCnt++
                        if (replies.performative == ACLMessage.PROPOSE) {
                            val price = replies.content.toInt()
                            if (price < bestPrice) {
                                bestPrice = price
                                bestSeller = replies.sender
                            }
                        }
                        if (repliesCnt >= sellerAgents.size) {
                            step = 2
                        }
                    } else {
                        block()
                    }
                }
                2 -> {
                    // Отправка заказа на покупку (ACCEPT_PROPOSAL) лучшему продавцу
                    val order = ACLMessage(ACLMessage.ACCEPT_PROPOSAL)
                    order.addReceiver(bestSeller)
                    order.content = targetBookTitle
                    order.conversationId = "book-trade"
                    order.replyWith = "order${System.currentTimeMillis()}"
                    myAgent.send(order)
                    step = 3
                }
                3 -> {
                    // Обработка подтверждения покупки (INFORM) от продавца
                    val reply = myAgent.receive()
                    if (reply != null) {
                        if (reply.performative == ACLMessage.INFORM) {
                            println("${targetBookTitle} успешно куплена у агента ${reply.sender.name}")
                            myAgent.doDelete()
                        }
                    } else {
                        block()
                    }
                }
            }
        }

        override fun done(): Boolean {
            return (step == 3 && bestSeller == null)
        }
    }
}
