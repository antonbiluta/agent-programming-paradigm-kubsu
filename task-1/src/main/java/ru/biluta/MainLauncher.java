package ru.biluta;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainLauncher {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();

        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);
        try {
            AgentController ping = mainContainer.createNewAgent("Ping", "ru.biluta.agents.PingAgent", null);
            AgentController pong = mainContainer.createNewAgent("Pong", "ru.biluta.agents.PongAgent", null);
            ping.start();
            pong.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
