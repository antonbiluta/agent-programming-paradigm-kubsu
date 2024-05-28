package agents

import jade.core.Agent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.ACLMessage

class PongAgent : Agent() {
    override fun setup() {
        println("Привет! Агент Pong ${aid.name} готов.")
        addBehaviour(PongBehaviour())
    }

    inner class PongBehaviour : CyclicBehaviour() {
        override fun action() {
            val msg = receive()
            if (msg != null && msg.content == "ping") {
                val reply = msg.createReply()
                reply.content = "pong"
                send(reply)
                println("Pong sent by ${aid.name}")
            }
            block()
        }
    }

    override fun takeDown() {
        println("Агент Pong ${aid.name} завершает работу.")
    }
}
