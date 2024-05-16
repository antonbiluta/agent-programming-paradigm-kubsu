package ru.biluta.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PongAgent extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive(MessageTemplate.MatchContent("ping"));
                if (msg != null) {
                    System.out.println("Пинг получен " + msg.getSender().getLocalName());
                    ACLMessage reply = msg.createReply();
                    reply.setContent("pong");
                    send(reply);

                    System.out.println("Понг отправлен " + msg.getSender().getLocalName());
                } else {
                    block();
                }
            }
        });
    }
}
