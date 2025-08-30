package io.kito.ccgauges.common.registry

import io.kito.ccgauges.ComputedPanelBehaviour
import io.kito.ccgauges.ID
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.SimpleRegister
import net.liukrast.eg.api.EGRegistries.PANEL_REGISTRY
import net.liukrast.eg.api.registry.PanelType
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Scan
object PanelTypes : SimpleRegister<PanelType<*>>(ID, PANEL_REGISTRY) {

    val computedGauge by "computed_gauge" { PanelType(::ComputedPanelBehaviour, ComputedPanelBehaviour::class.java) }
}