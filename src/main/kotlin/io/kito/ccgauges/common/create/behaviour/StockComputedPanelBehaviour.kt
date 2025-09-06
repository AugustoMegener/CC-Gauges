package io.kito.ccgauges.common.create.behaviour

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager
import io.kito.ccgauges.common.cc.api.stock.GaugeStockBrain
import io.kito.ccgauges.common.registry.ComputerComponents.stockComponent
import io.kito.ccgauges.common.registry.Items.stockComputedGaugeItem
import io.kito.ccgauges.common.registry.PartialModels.stockComputedGaugeModel
import net.liukrast.eg.api.registry.PanelType

class StockComputedPanelBehaviour(panel: PanelType<*>,
                                  be: FactoryPanelBlockEntity,
                                  panelSlot: FactoryPanelBlock.PanelSlot) :
    ComputedPanelBehaviour(panel, be, panelSlot)
{
    val stockBrain = GaugeStockBrain(this)

    override fun getModel(state: FactoryPanelBlock.PanelState, type: FactoryPanelBlock.PanelType) =
        stockComputedGaugeModel

    override fun getItem() = stockComputedGaugeItem

    init { computerComponents[stockComponent] = stockBrain }
}