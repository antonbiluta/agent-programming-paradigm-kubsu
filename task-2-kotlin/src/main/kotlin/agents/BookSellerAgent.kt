package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.core.behaviours.OneShotBehaviour
import jade.core.behaviours.TickerBehaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage

class BookSellerAgent : Agent() {
    private val catalogue = mutableMapOf<String, Int>()

    override fun setup() {
        println("Продавец ${aid.name} готов.")

        // Регистрация услуги продажи книг в "Желтых страницах"
        val dfd = DFAgentDescription()
        dfd.name = aid
        val sd = ServiceDescription()
        sd.type = "book-selling"
        sd.name = "JADE-book-trading"
        dfd.addServices(sd)
        DFService.register(this, dfd)

        // Добавление поведения
        addBehaviour(OfferRequestsServer())
        addBehaviour(PurchaseOrdersServer())

        // Пример добавления книги в каталог
        addBehaviour(object : OneShotBehaviour() {
            override fun action() {
                catalogue["Книга1"] = 100
                catalogue["Книга2"] = 200
            }
        })
    }

    override fun takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("Продавец ${aid.name} завершает работу.")
    }

    // Поведение для обработки запросов на предложение (CFP)
    inner class OfferRequestsServer : CyclicBehaviour() {
        override fun action() {
            val msg = myAgent.receive()
            if (msg != null) {
                if (msg.performative == ACLMessage.CFP) {
                    val title = msg.content
                    val reply = msg.createReply()

                    val price = catalogue[title]
                    if (price != null) {
                        reply.performative = ACLMessage.PROPOSE
                        reply.content = price.toString()
                    } else {
                        reply.performative = ACLMessage.REFUSE
                        reply.content = "not-available"
                    }
                    myAgent.send(reply)
                }
            } else {
                block()
            }
        }
    }

    // Поведение для обработки заказов на покупку (ACCEPT_PROPOSAL)
    inner class PurchaseOrdersServer : CyclicBehaviour() {
        override fun action() {
            val msg = myAgent.receive()
            if (msg != null) {
                if (msg.performative == ACLMessage.ACCEPT_PROPOSAL) {
                    val title = msg.content
                    val reply = msg.createReply()

                    val price = catalogue.remove(title)
                    if (price != null) {
                        reply.performative = ACLMessage.INFORM
                        println("${title} продана агенту ${msg.sender.name}")
                    } else {
                        reply.performative = ACLMessage.FAILURE
                        reply.content = "not-available"
                    }
                    myAgent.send(reply)
                }
            } else {
                block()
            }
        }
    }
}
