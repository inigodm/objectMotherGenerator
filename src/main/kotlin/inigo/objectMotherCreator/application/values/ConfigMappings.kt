package inigo.objectMotherCreator.application.values

import inigo.objectMotherCreator.infraestructure.config.IntellijPluginService

class ConfigMappings : Mappings{
    override fun random(type: String): String {
        return IntellijPluginService.getInstance().getGeneratorFor(type)
    }
}
