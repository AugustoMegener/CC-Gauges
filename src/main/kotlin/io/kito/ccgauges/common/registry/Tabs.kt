package io.kito.ccgauges.common.registry

import io.kito.ccgauges.DG.named
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.registry.Items.computedGaugeItem
import io.kito.ccgauges.common.registry.Items.stockComputedGaugeItem
import io.kito.ccgauges.common.registry.Items.tableGaugeItem
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.CreativeModeTabRegister
import io.kito.kore.util.minecraft.EN_US
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Scan
object Tabs : CreativeModeTabRegister(ID) {

    val ccGaugesTab by "cc_gauges" where {
        named(it, EN_US to "CC: Gauges")
        icon { computedGaugeItem.defaultInstance }

        display {
            items(::computedGaugeItem, ::stockComputedGaugeItem, ::tableGaugeItem)
        }
    }
}