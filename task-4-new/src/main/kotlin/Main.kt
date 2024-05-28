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
        val managerAgent: AgentController = mainContainer.createNewAgent("WarehouseManager", "agents.WarehouseManagerAgent", null)
        val deliveryAgent: AgentController = mainContainer.createNewAgent("DeliveryAgent", "agents.DeliveryAgent", null)
        val warehouseAgent: AgentController = mainContainer.createNewAgent("Warehouse", "agents.WarehouseAgent", null)

        managerAgent.start()
        deliveryAgent.start()
        warehouseAgent.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
