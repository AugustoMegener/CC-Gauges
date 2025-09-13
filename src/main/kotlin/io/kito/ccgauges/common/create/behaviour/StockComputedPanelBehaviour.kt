package io.kito.ccgauges.common.create.behaviour

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager
import io.kito.ccgauges.common.cc.api.stock.GaugeStockBrain
import io.kito.ccgauges.common.registry.ComputerComponents.stockComponent
import io.kito.ccgauges.common.registry.Items.stockComputedGaugeItem
import io.kito.ccgauges.common.registry.PartialModels.stockComputedGaugeModel
import io.kito.kore.util.minecraft.set
import net.liukrast.eg.api.registry.PanelType
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag

class StockComputedPanelBehaviour(panel: PanelType<*>,
                                  be: FactoryPanelBlockEntity,
                                  panelSlot: FactoryPanelBlock.PanelSlot) :
    ComputedPanelBehaviour(panel, be, panelSlot)
{
    val stockBrain = GaugeStockBrain(this)

    init { computerComponents[stockComponent] = stockBrain }

    override fun getModel(state: FactoryPanelBlock.PanelState, type: FactoryPanelBlock.PanelType) =
        stockComputedGaugeModel

    override fun getItem() = stockComputedGaugeItem

    override fun easyWrite(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyWrite(nbt, registries, clientPacket)
        nbt.putUUID("network", network)
    }

    override fun easyRead(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyRead(nbt, registries, clientPacket)
        network = nbt.getUUID("network")
    }
}