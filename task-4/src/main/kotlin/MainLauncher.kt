package ru.biluta

import jade.core.ProfileException
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.AgentContainer
import jade.wrapper.AgentController
import jade.wrapper.StaleProxyException
import ru.biluta.agents.AgentAssembler
import ru.biluta.agents.AgentWarehouse
import ru.biluta.agents.AgentWarehouseManager
import kotlin.reflect.KClass

fun main() {
    val mainContainer = initProfile().getMainContainer()

    try {
        println("Запуск агентов...")

        val manager: AgentController = mainContainer createWarehouseManager  "manager"
        val assembler1: AgentController = mainContainer createAssembler "assembler1"
        val assembler2: AgentController = mainContainer createAssembler "assembler2"
        val warehouse: AgentController = mainContainer createWarehouse "warehouse"

        listOf(manager, assembler1, assembler2, warehouse).forEach {
            it.start()
        }
        println("Все агенты запущены.")
    } catch (e: StaleProxyException) {
        e.printStackTrace()
    }
}

fun initProfile(p: ProfileImpl = ProfileImpl()): ProfileImpl {
    //p.setParameter("gui", "true")
    return p
}

fun ProfileImpl.getMainContainer(rt: Runtime = Runtime.instance()): AgentContainer {
    return rt.createMainContainer(this)
}

infix fun AgentContainer.createWarehouseManager(name: String): AgentController {
    return this.createLazyAgent(name, AgentWarehouseManager::class)
}

infix fun AgentContainer.createWarehouse(name: String): AgentController {
    return this.createLazyAgent(name, AgentWarehouse::class)
}

infix fun AgentContainer.createAssembler(name: String): AgentController {
    return this.createLazyAgent(name, AgentAssembler::class)
}

fun AgentContainer.createLazyAgent(name: String, clazz: KClass<*>): AgentController {
    return this.createNewAgent(name, clazz.java.name, null)
}