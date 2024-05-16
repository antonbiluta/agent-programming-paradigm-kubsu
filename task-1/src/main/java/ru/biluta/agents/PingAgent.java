package ru.biluta.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class PingAgent extends Agent {
    protected void setup() {

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Pong", AID.ISLOCALNAME));
                msg.setContent("ping");
                send(msg);

                System.out.println("Init ping");
            }
        });
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive(MessageTemplate.MatchContent("pong"));
                if (msg != null) {
                    System.out.println("Понг получен");
                    msg.getSender().getLocalName();
                    ACLMessage reply = msg.createReply();
                    reply.setContent("ping");
                    send(reply);

                    System.out.println("Пинг отправлен " + msg.getSender().getLocalName());
                } else {
                    block();
                }
            }
        });
    }
}
