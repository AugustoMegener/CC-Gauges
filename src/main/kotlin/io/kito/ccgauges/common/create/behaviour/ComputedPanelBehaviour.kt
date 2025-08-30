package io.kito.ccgauges.common.create.behaviour

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import io.kito.ccgauges.common.data.ComputedGaugeData
import io.kito.ccgauges.common.network.OpenComputedGaugeMenuPacket
import io.kito.ccgauges.common.registry.Items.computedGaugeItem
import io.kito.ccgauges.common.registry.PartialModels.computedGaugeModel
import io.kito.ccgauges.common.world.inventory.ComputedGaugeMenu
import net.liukrast.eg.api.logistics.board.AbstractPanelBehaviour
import net.liukrast.eg.api.registry.PanelType
import net.liukrast.eg.registry.EGPanelConnections.*
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.literal
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import io.kito.ccgauges.ComputedGaugeDisplaySource as Source

class ComputedPanelBehaviour(panel: PanelType<*>, be: FactoryPanelBlockEntity, panelSlot: FactoryPanelBlock.PanelSlot) :
    AbstractPanelBehaviour(panel, be, panelSlot),  MenuProvider
{
    val data  = ComputedGaugeData()

    val computer by lazy { data.computer(this) }

    override fun addConnections(builder: PanelConnectionBuilder) {
        builder.put(FILTER, ::filter)
        builder.put(REDSTONE) { data.redstoneSignal }
        builder.put(INTEGER) { data.number }
        builder.put(STRING) { getDisplayLinkComponent(false).toString() }
    }

    override fun getItem() = computedGaugeItem

    override fun getModel(state: FactoryPanelBlock.PanelState, type: FactoryPanelBlock.PanelType) = computedGaugeModel

    override fun getDisplayLinkComponent(shortenNumbers: Boolean): MutableComponent {
        return when(data.displaySource) {
            Source.FILTER -> filter.item().let { item ->
                item.displayName.let { if (item.count > 1) literal("${item.count}x").append(it) else it.copy() }
            }
            Source.INTEGER -> literal("${data.number}")
            Source.REDSTONE -> literal("${data.redstoneSignal}")
            Source.STRING -> literal(data.string)
        }
    }

    override fun displayScreen(player: Player) {
        OpenComputedGaugeMenuPacket(pos, slot).send()
    }

    override fun tick() {
        super.tick()
        if (!blockEntity.level!!.isClientSide) computer.keepAlive()
    }

    override fun createMenu(id: Int, inv: Inventory, player: Player) = ComputedGaugeMenu(id, inv, this)

    override fun getDisplayName(): Component =
        if (data.label.isNotEmpty()) literal(data.label) else super.getDisplayName()

    override fun easyWrite(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyWrite(nbt, registries, clientPacket)
        data
        nbt.put("data", data.serializeNBT(registries))
    }

    override fun easyRead(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyRead(nbt, registries, clientPacket)

        if (nbt.contains("data")) data.deserializeNBT(registries, nbt.getCompound("data"))
    }
}