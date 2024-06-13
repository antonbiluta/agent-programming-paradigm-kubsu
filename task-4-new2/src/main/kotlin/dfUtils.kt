package ru.biluta

import jade.core.AID
import jade.core.Agent
import jade.core.AgentContainer
import jade.core.behaviours.Behaviour
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate

fun Agent.registerInDF(aid: AID, type: String, name: String) {
    val dfd = DFAgentDescription()
    dfd.name = aid
    val sd = ServiceDescription()
    sd.type = type
    sd.name = name
    dfd.addServices(sd)
    DFService.register(this, dfd)
}

fun searchAgent(myAgent: Agent, serviceType: String): AID? {
    return searchAgents(myAgent, serviceType).firstOrNull()
}

fun searchAgents(myAgent: Agent, serviceType: String): List<AID> {
    val template = DFAgentDescription()
    val sd = ServiceDescription()
    sd.type = serviceType
    template.addServices(sd)

    return try {
        val result = DFService.search(myAgent, template)
        result.map { it.name }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}