import agents.HelloWorldAgent
import agents.PingAgent
import agents.PongAgent
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
        val helloWorldAgent: AgentController = mainContainer.createNewAgent("HelloWorldAgent", HelloWorldAgent::class.java.name, null)
        val pingAgent: AgentController = mainContainer.createNewAgent("PingAgent", PingAgent::class.java.name, null)
        val pongAgent: AgentController = mainContainer.createNewAgent("PongAgent", PongAgent::class.java.name, null)

        helloWorldAgent.start()
        pingAgent.start()
        pongAgent.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
