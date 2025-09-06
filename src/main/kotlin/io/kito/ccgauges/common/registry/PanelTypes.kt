package io.kito.ccgauges.common.registry

import io.kito.ccgauges.ID
import io.kito.ccgauges.common.create.behaviour.ComputedPanelBehaviour
import io.kito.ccgauges.common.create.behaviour.StockComputedPanelBehaviour
import io.kito.ccgauges.common.create.behaviour.TablePanelBehaviour
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.SimpleRegister
import net.liukrast.eg.api.EGRegistries.PANEL_REGISTRY
import net.liukrast.eg.api.registry.PanelType
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Scan
object PanelTypes : SimpleRegister<PanelType<*>>(ID, PANEL_REGISTRY) {

    val computedGauge by "computed_gauge" { PanelType(::ComputedPanelBehaviour, ComputedPanelBehaviour::class.java) }
    val stockComputedGauge by "stock_computed_gauge" {
        PanelType(::StockComputedPanelBehaviour, StockComputedPanelBehaviour::class.java)
    }
    val tableGauge by "table_gauge" {
        PanelType(::TablePanelBehaviour, TablePanelBehaviour::class.java)
    }
}