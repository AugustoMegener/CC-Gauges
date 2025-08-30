package io.kito.ccgauges

import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.component.ComputerComponents
import dan200.computercraft.api.lua.IComputerSystem
import dan200.computercraft.api.lua.ILuaAPI
import io.kito.ccgauges.common.create.behaviour.ComputedPanelBehaviour


class GaugeAPI(val gauge: ComputedPanelBehaviour) : ILuaAPI {
    override fun getNames() = arrayOf<String>()

    override fun getModuleName() = "$ID.gauge"

    companion object {

        @JvmStatic
        fun register() {
            // @start region=register
            ComputerCraftAPI.registerAPIFactory { computer: IComputerSystem ->
                val turtle = computer.getComponent(ComputerComponents.TURTLE)
                if (turtle == null) null else GaugeAPI(turtle)
            }
            // @end region=register
        }
    }
}

