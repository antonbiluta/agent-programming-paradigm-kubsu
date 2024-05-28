package agents

import jade.core.Agent
import jade.core.behaviours.OneShotBehaviour

class HelloWorldAgent : Agent() {
    override fun setup() {
        println("Привет! Агент ${aid.name} готов.")
        addBehaviour(object : OneShotBehaviour() {
            override fun action() {
                println("Hello, World! from ${aid.name}")
            }
        })
    }

    override fun takeDown() {
        println("Агент ${aid.name} завершает работу.")
    }
}
