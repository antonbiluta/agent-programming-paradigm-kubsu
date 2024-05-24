package ru.biluta.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentManufacturer extends Agent {
    private String name;
    private int performance;
    private int reliability;
    private int cost;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            name = (String) args[0];
            performance = Integer.parseInt(args[1].toString());
            reliability = Integer.parseInt(args[2].toString());
            cost = Integer.parseInt(args[3].toString());
        }

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(name + "," + performance + "," + reliability + "," + cost);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
