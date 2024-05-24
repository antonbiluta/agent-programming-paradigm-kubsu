package ru.biluta;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ru.biluta.agents.BookBuyerAgent;
import ru.biluta.agents.BookSellerAgent;

import java.util.*;

public class MainLauncher {

    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();

        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        // Список возможных книг
        List<String> books = Arrays.asList("Java Programming", "The Art of Computer Programming",
                "Data Structures and Algorithms", "Modern Operating Systems",
                "Effective Java");

        try {
            Random random = new Random();

            for (int i = 1; i <= 3; i++) {
                Map<String, Integer> catalogue = new HashMap<>();
                int numBooks = 1 + random.nextInt(books.size()); // Продавец возьмет от 1 до всех книг
                Collections.shuffle(books); // Перемешиваем список книг
                for (int j = 0; j < numBooks; j++) {
                    catalogue.put(books.get(j), 1 + random.nextInt(100)); // Цена от 1 до 100
                }

                Object[] sellerArgs = {catalogue};
                AgentController seller = mainContainer.createNewAgent("seller" + i,
                        BookSellerAgent.class.getName(), sellerArgs);
                seller.start();
            }

            AgentController buyerAgent = mainContainer.createNewAgent(
                    "buyer",
                    BookBuyerAgent.class.getName(),
                    new Object[] {"Java Programming"}
            );
            buyerAgent.start();

        } catch (StaleProxyException e) {
            System.err.println("Error launching agents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
