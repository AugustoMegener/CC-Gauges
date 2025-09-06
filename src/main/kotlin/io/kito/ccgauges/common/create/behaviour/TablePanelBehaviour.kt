package io.kito.ccgauges.common.create.behaviour

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import dev.engine_room.flywheel.lib.model.baked.PartialModel
import io.kito.ccgauges.common.registry.Items.tableGaugeItem
import io.kito.ccgauges.common.registry.PartialModels.tableGaugeModel
import io.kito.kore.common.data.Save
import io.kito.kore.common.data.nbt.KNBTSerializable
import net.liukrast.eg.api.logistics.board.AbstractPanelBehaviour
import net.liukrast.eg.api.registry.PanelType
import net.liukrast.eg.content.logistics.board.StringPanelBehaviour
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.Item

class TablePanelBehaviour(panel: PanelType<*>,
                          be: FactoryPanelBlockEntity,
                          panelSlot: FactoryPanelBlock.PanelSlot) :
    AbstractPanelBehaviour(panel, be, panelSlot), KNBTSerializable
{
    @Save var keyDelimiter = "(.*?):(.*)"
    @Save var entryDelimiter = "(.*?),(.*)"
    @Save var subTableDelimiter = "\\{(.*?)\\}"

    override fun addConnections(builder: PanelConnectionBuilder) {
        Regex(keyDelimiter)
    }

    override fun getItem() = tableGaugeItem

    override fun getModel(state: FactoryPanelBlock.PanelState, type: FactoryPanelBlock.PanelType) = tableGaugeModel

    override fun easyWrite(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyWrite(nbt, registries, clientPacket)
        nbt.put("data", serializeNBT(registries))
    }

    override fun easyRead(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyRead(nbt, registries, clientPacket)
        deserializeNBT(registries, nbt.getCompound("data"))
    }
}