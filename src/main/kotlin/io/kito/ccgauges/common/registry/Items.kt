package io.kito.ccgauges.common.registry

import io.kito.ccgauges.DG.blockModel
import io.kito.ccgauges.DG.named
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.registry.PanelTypes.computedGauge
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.ItemRegister
import io.kito.kore.util.minecraft.EN_US
import net.liukrast.eg.api.logistics.board.PanelBlockItem
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Scan
object Items : ItemRegister(ID) {

    val computedGaugeItem by "computed_gauge" of { PanelBlockItem(::computedGauge, it) } where {
        named(EN_US to "Computed Gauge")
    }
}