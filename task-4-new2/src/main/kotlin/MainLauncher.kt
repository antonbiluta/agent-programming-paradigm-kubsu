package ru.biluta

import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.AgentContainer
import jade.wrapper.AgentController
import ru.biluta.agents.OrderGeneratorAgent
import ru.biluta.agents.WarehouseAgent
import ru.biluta.agents.WarehouseManagerAgent
import ru.biluta.agents.WorkerAgent
import kotlin.random.Random

fun main() {
    val runtime: Runtime = Runtime.instance()
    val profile: Profile = ProfileImpl()
    val mainContainer: AgentContainer = runtime.createMainContainer(profile)

    val inventory = OrderUtils.items.associateWith { item ->
        //val quantity = Random.nextInt(0, 30)
        val quantity = 100
        quantity
    }
    val inventoryArgs = arrayOf(inventory)

    val numOrders = 5
    val orderAgentArgs = arrayOf(numOrders)

    try {

        val warehouseAgent: AgentController = mainContainer.createNewAgent("warehouse", WarehouseAgent::class.java.name, inventoryArgs)
        warehouseAgent.start()

        val managerAgent: AgentController = mainContainer.createNewAgent("warehouseManager", WarehouseManagerAgent::class.java.name, null)
        managerAgent.start()

        for (i in 1..3) {
            val workerAgent: AgentController = mainContainer.createNewAgent("worker$i", WorkerAgent::class.java.name, null)
            workerAgent.start()
        }

        val orderGeneratorAgent: AgentController = mainContainer.createNewAgent("orderGenerator", OrderGeneratorAgent::class.java.name, orderAgentArgs)
        orderGeneratorAgent.start()



    } catch (e: Exception) {
        e.printStackTrace()
    }
}