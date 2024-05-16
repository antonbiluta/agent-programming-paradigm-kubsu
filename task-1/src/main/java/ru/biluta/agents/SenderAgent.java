package ru.biluta.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SenderAgent extends Agent {
    protected void setup() {
        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("receiver", AID.ISLOCALNAME));
                msg.setContent("Hello from Sender!");
                send(msg);
            }
        });
    }
}
