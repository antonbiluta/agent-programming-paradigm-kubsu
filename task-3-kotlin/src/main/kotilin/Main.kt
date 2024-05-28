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
        val manufacturer1: AgentController = mainContainer.createNewAgent("Фотон", "agents.ManufacturerAgent", arrayOf("Фотон", 120, 9, 5))
        val manufacturer2: AgentController = mainContainer.createNewAgent("Спецдеталь", "agents.ManufacturerAgent", arrayOf("Спецдеталь", 200, 7, 8))
        val manufacturer3: AgentController = mainContainer.createNewAgent("Станкострой", "agents.ManufacturerAgent", arrayOf("Станкострой", 100, 8, 5))
        val manufacturer4: AgentController = mainContainer.createNewAgent("Атом", "agents.ManufacturerAgent", arrayOf("Атом", 150, 6, 7))

        val company1: AgentController = mainContainer.createNewAgent("Company1", "agents.CompanyAgent", arrayOf(5, 100, 8))
        val company2: AgentController = mainContainer.createNewAgent("Company2", "agents.CompanyAgent", arrayOf(6, 150, 5))

        manufacturer1.start()
        manufacturer2.start()
        manufacturer3.start()
        manufacturer4.start()
        company1.start()
        company2.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
