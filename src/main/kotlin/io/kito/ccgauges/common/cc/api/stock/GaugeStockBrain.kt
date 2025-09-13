package io.kito.ccgauges.common.cc.api.stock

import io.kito.ccgauges.common.create.behaviour.StockComputedPanelBehaviour
import net.minecraft.world.level.Level
import java.util.*

class GaugeStockBrain(private val gauge: StockComputedPanelBehaviour) : IStockAcess {
    override val networkId: UUID get() = gauge.network
    override val level get() = gauge.blockEntity.level!!
}