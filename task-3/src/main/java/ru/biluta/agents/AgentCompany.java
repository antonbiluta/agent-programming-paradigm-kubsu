package ru.biluta.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;

public class AgentCompany extends Agent {
    private String companyName;
    private int requiredPerformance;
    private int requiredReliability;
    private int budget;
    private ArrayList<AID> manufactures;
    private Map<String, String> offers;
    private int repliesCount = 0;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            companyName = (String) args[0];
            requiredPerformance = Integer.parseInt(args[1].toString());
            requiredReliability = Integer.parseInt(args[2].toString());
            budget = Integer.parseInt(args[3].toString());
        }

        manufactures = new ArrayList<>();
        offers = new HashMap<>();
        manufactures.add(new AID("manufacturer1", AID.ISLOCALNAME));
        manufactures.add(new AID("manufacturer2", AID.ISLOCALNAME));
        manufactures.add(new AID("manufacturer3", AID.ISLOCALNAME));
        manufactures.add(new AID("manufacturer4", AID.ISLOCALNAME));

        System.out.println(companyName + " агент запущен.");
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                for (AID manufacturer : manufactures) {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.addReceiver(manufacturer);
                    send(msg);
                }
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
                ACLMessage reply = receive(mt);
                if (reply != null) {
                    try {
                        repliesCount++;
                        String[] content = reply.getContent().split(",");
                        String name = content[0];
                        int performance = Integer.parseInt(content[1]);
                        int reliability = Integer.parseInt(content[2]);
                        int cost = Integer.parseInt(content[3]);

                        if (performance >= requiredPerformance && reliability >= requiredReliability && cost <= budget) {
                            offers.put(name, reply.getContent());
                        }

                        if (repliesCount >= manufactures.size()) {
                            chooseBestOffer();
                            myAgent.doDelete();
                        }


                        System.out.println(companyName + " получил предложение от " + name
                                + ": производительность=" + performance
                                + ", надежность=" + reliability
                                + ", стоимость=" + cost);
                    } catch (Exception e) {
                        System.err.println(companyName + " сломался я блять");
                    }
                } else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return repliesCount >= manufactures.size();
            }

            private void chooseBestOffer() {
                String bestManufacturer = null;
                int highestScore = Integer.MIN_VALUE;

                for (String name : offers.keySet()) {
                    String[] content = offers.get(name).split(",");
                    int performance = Integer.parseInt(content[1]);
                    int reliability = Integer.parseInt(content[2]);
                    int cost = Integer.parseInt(content[3]);

                    int score = performance + reliability - cost;

                    if (score > highestScore) {
                        highestScore = score;
                        bestManufacturer = name;
                    }
                }

                if (bestManufacturer != null) {
                    System.out.println(companyName + " выбрал " + bestManufacturer);
                } else {
                    System.out.println(companyName + " не нашел подходящего производителя.");
                }
            }
        });
    }

    protected void takeDown() {
        System.out.println(companyName + " агент завершает работу.");
    }
}
