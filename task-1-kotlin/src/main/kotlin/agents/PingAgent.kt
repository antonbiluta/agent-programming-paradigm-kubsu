package agents

import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage

class PingAgent : Agent() {
    override fun setup() {
        println("Привет! Агент Ping ${aid.name} готов.")
        addBehaviour(PingBehaviour())
    }

    inner class PingBehaviour : CyclicBehaviour() {
        override fun action() {
            val msg = ACLMessage(ACLMessage.INFORM)
            msg.content = "ping"
            msg.addReceiver(AID("PongAgent", AID.ISLOCALNAME))
            send(msg)
            println("Ping sent by ${aid.name}")

            val reply = receive()
            if (reply != null) {
                println("Ping received reply: ${reply.content}")
            }
            block()
        }
    }

    override fun takeDown() {
        println("Агент Ping ${aid.name} завершает работу.")
    }
}
