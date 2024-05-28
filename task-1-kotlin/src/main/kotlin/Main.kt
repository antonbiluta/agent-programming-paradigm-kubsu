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
        val helloWorldAgent: AgentController = mainContainer.createNewAgent("HelloWorldAgent", "agents.HelloWorldAgent", null)
        val pingAgent: AgentController = mainContainer.createNewAgent("PingAgent", "agents.PingAgent", null)
        val pongAgent: AgentController = mainContainer.createNewAgent("PongAgent", "agents.PongAgent", null)

        helloWorldAgent.start()
        pingAgent.start()
        pongAgent.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
