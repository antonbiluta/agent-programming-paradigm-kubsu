package ru.biluta

import jade.core.AID
import jade.core.Agent
import jade.lang.acl.ACLMessage

fun Agent.sendWarehouseQuery(order: String) {
    val warehouseQuery = ACLMessage(ACLMessage.QUERY_REF).apply {
        content = order
        addReceiver(AID("warehouse", AID.ISLOCALNAME))
    }
    send(warehouseQuery)
}

fun Agent.sendStockQuantity(itemName: String) {
    val stockQuery = ACLMessage(ACLMessage.QUERY_REF).apply {
        content = itemName
        addReceiver(AID("warehouse", AID.ISLOCALNAME))
    }
    send(stockQuery)
}

fun Agent.sendRestockRequest(itemName: String) {
    val restockRequest = ACLMessage(ACLMessage.REQUEST).apply {
        content = "Restock:$itemName"
        addReceiver(AID("restockManager", AID.ISLOCALNAME))
    }
    send(restockRequest)
}

fun Agent.sendOrderRequest(order: StringBuilder) {
    val orderRequest = ACLMessage(ACLMessage.REQUEST).apply {
        content = order.toString()
        addReceiver(AID("assembler1", AID.ISLOCALNAME))
        addReceiver(AID("assembler2", AID.ISLOCALNAME))
    }
    send(orderRequest)
}

fun Agent.sendReply(msg: ACLMessage, response: StringBuilder) {
    val reply = msg.createReply().apply {
        content = response.toString()
    }
    send(reply)
}

fun Agent.sendReport(reportMessage: String) {
    val report = ACLMessage(ACLMessage.INFORM).apply {
        content = reportMessage
        addReceiver(AID("manager", AID.ISLOCALNAME))
    }
    send(report)
}
