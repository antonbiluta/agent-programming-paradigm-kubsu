import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.AgentContainer
import jade.wrapper.AgentController

fun main() {
    val runtime: Runtime = Runtime.instance()
    val profile: Profile = ProfileImpl()
    profile.setParameter(Profile.MAIN_HOST, "localhost")
    profile.setParameter(Profile.MAIN_PORT, "1099")
    val mainContainer: AgentContainer = runtime.createMainContainer(profile)

    try {
        val sellerAgent: AgentController = mainContainer.createNewAgent("BookSellerAgent", "agents.BookSellerAgent", null)
        val buyerAgent: AgentController = mainContainer.createNewAgent("BookBuyerAgent", "agents.BookBuyerAgent", null)

        sellerAgent.start()
        buyerAgent.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
