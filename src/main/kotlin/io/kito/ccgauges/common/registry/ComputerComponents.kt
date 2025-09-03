package io.kito.ccgauges.common.registry

import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.component.ComputerComponent
import dan200.computercraft.api.turtle.ITurtleAccess
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.cc.api.gauge.IGaugeAcess

object ComputerComponents  {

    val gaugeComponent: ComputerComponent<IGaugeAcess> = ComputerComponent.create(ID, "gauge")

}