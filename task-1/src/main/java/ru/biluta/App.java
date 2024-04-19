package ru.biluta;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();

        profile.setParameter(Profile.MAIN_HOST, "localhost");
        AgentContainer container = runtime.createMainContainer(profile);

        try {
            AgentController agAnton = container.createNewAgent("Anton-Agent", "ru.biluta.HelloWorldAgent", null);
            AgentController agOleg = container.createNewAgent("Oleg-Agent", "ru.biluta.HelloWorldAgent", null);
            agAnton.start();
            agOleg.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
