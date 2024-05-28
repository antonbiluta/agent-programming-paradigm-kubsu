package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage

class ManufacturerAgent : Agent() {
    private lateinit var name: String
    private var production: Int = 0
    private var reliability: Int = 0
    private var cost: Int = 0

    override fun setup() {
        val args = arguments
        if (args != null && args.isNotEmpty()) {
            name = args[0] as String
            production = args[1] as Int
            reliability = args[2] as Int
            cost = args[3] as Int
        }

        println("Завод $name готов. Производительность: $production, Надежность: $reliability, Стоимость: $cost")

        val dfd = DFAgentDescription()
        dfd.name = aid
        val sd = ServiceDescription()
        sd.type = "equipment-supplier"
        sd.name = name
        dfd.addServices(sd)
        DFService.register(this, dfd)

        addBehaviour(OfferRequestsServer())
    }

    override fun takeDown() {
        try {
            DFService.deregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("Завод $name завершает работу.")
    }

    inner class OfferRequestsServer : CyclicBehaviour() {
        override fun action() {
            val msg = receive()
            if (msg != null && msg.performative == ACLMessage.CFP) {
                val reply = msg.createReply()
                reply.performative = ACLMessage.PROPOSE
                reply.content = "$production,$reliability,$cost"
                send(reply)
            } else {
                block()
            }
        }
    }
}
