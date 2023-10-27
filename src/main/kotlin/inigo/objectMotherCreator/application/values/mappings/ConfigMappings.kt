package inigo.objectMotherCreator.application.values.mappings

import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService
import java.util.*

class ConfigMappings : Mappings {
    override fun random(type: String): String {
        return IntellijPluginService.getInstance().getGeneratorFor(type)
    }

    override fun importsFor(name: String): Vector<String> {
        return IntellijPluginService.getInstance().getImportsFor(name)
    }

}
