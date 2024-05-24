package ru.biluta;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ru.biluta.agents.AgentCompany;
import ru.biluta.agents.AgentManufacturer;

public class MainLauncher {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
//        profile.setParameter(Profile.GUI, "true");

        AgentContainer ac = rt.createMainContainer(profile);

        try {
            AgentController manufacturer1 = ac.createNewAgent(
                    "manufacturer1",
                    AgentManufacturer.class.getName(),
                    new Object[]{"Фотон", 120, 9, 5}
            );
            AgentController manufacturer2 = ac.createNewAgent(
                    "manufacturer2",
                    AgentManufacturer.class.getName(),
                    new Object[]{"Спецдеталь", 200, 7, 8}
            );
            AgentController manufacturer3 = ac.createNewAgent(
                    "manufacturer3",
                    AgentManufacturer.class.getName(),
                    new Object[]{"Станкострой", 100, 8, 5}
            );
            AgentController manufacturer4 = ac.createNewAgent(
                    "manufacturer4",
                    AgentManufacturer.class.getName(),
                    new Object[]{"Атом", 150, 6, 7}
            );
            AgentController company1 = ac.createNewAgent(
                    "company1",
                    AgentCompany.class.getName(),
                    new Object[]{"Фирма1", 100, 8, 5}
            );
            AgentController company2 = ac.createNewAgent(
                    "company2",
                    AgentCompany.class.getName(),
                    new Object[]{"Фирма2", 150, 5, 6}
            );

            manufacturer1.start();
            manufacturer2.start();
            manufacturer3.start();
            manufacturer4.start();
            company1.start();
            company2.start();
        } catch (StaleProxyException e) {
            System.out.println("Exception starting agent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
