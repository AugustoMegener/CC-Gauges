package io.kito.ccgauges.common.create.behaviour

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBehaviour
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import dan200.computercraft.api.component.ComputerComponent
import io.kito.ccgauges.common.Util.allInputs
import io.kito.ccgauges.common.cc.api.gauge.GaugeBrain
import io.kito.ccgauges.common.data.ComputedGaugeData
import io.kito.ccgauges.common.network.OpenComputedGaugeMenuPacket
import io.kito.ccgauges.common.registry.Items.computedGaugeItem
import io.kito.ccgauges.common.registry.PanelConnections.peripheralConnection
import io.kito.ccgauges.common.registry.PartialModels.computedGaugeModel
import io.kito.ccgauges.common.world.inventory.ComputedGaugeMenu
import net.createmod.catnip.math.VecHelper
import net.liukrast.eg.api.logistics.board.AbstractPanelBehaviour
import net.liukrast.eg.api.registry.PanelType
import net.liukrast.eg.content.logistics.board.IntPanelBehaviour
import net.liukrast.eg.registry.EGPanelConnections.*
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component.literal
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import io.kito.ccgauges.common.create.behaviour.ComputedGaugeDisplaySource as Source

open class ComputedPanelBehaviour(panel: PanelType<*>,
                                  be: FactoryPanelBlockEntity,
                                  panelSlot: FactoryPanelBlock.PanelSlot) :
    AbstractPanelBehaviour(panel, be, panelSlot),  MenuProvider
{
    private var isInitialized = false
    val data  = ComputedGaugeData()

    val brain get() = GaugeBrain(this)

    val computer by lazy { data.computer(this) }

    val computerComponents = hashMapOf<ComputerComponent<*>, Any>()

    override fun addConnections(builder: PanelConnectionBuilder) {
        builder.put(FILTER, ::filter)
        builder.put(REDSTONE) { data.redstoneSignal }
        builder.put(INTEGER) { data.integer }
        builder.put(STRING) { getDisplayLinkComponent(false).toString() }
    }


    override fun getItem() = computedGaugeItem

    override fun getModel(state: FactoryPanelBlock.PanelState, type: FactoryPanelBlock.PanelType) = computedGaugeModel

    override fun getDisplayLinkComponent(shortenNumbers: Boolean): MutableComponent {
        return when(data.displaySource) {
            Source.FILTER -> filter.item().let { item ->
                item.displayName.let { if (item.count > 1) literal("${item.count}x").append(it) else it.copy() }
            }

            Source.INTEGER -> literal("${data.integer}")
            Source.REDSTONE -> literal("${data.redstoneSignal}")
            Source.STRING -> literal(data.string)
        }
    }

    override fun displayScreen(player: Player) { OpenComputedGaugeMenuPacket(pos, slot).send() }

    override fun tick() {
        super.tick()

        if (!blockEntity.level!!.isClientSide) {
            if (!isInitialized) {
                computer.turnOn()
                isInitialized = true
            }
            computer.keepAlive()
        }
    }

    override fun checkForRedstoneInput() {
        super.checkForRedstoneInput()

        val a = allInputs(INTEGER.get())
        a // empty list
    }

    override fun createMenu(id: Int, inv: Inventory, player: Player) = ComputedGaugeMenu(id, inv, this)

    override fun shouldRenderBulb(original: Boolean) = data.renderBulb

    override fun getTip(): MutableComponent = data.tip.copy()

    override fun easyWrite(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyWrite(nbt, registries, clientPacket)
        nbt.put("data", data.serializeNBT(registries))
    }

    override fun easyRead(nbt: CompoundTag, registries: HolderLookup.Provider, clientPacket: Boolean) {
        super.easyRead(nbt, registries, clientPacket)
        if (nbt.contains("data")) data.deserializeNBT(registries, nbt.getCompound("data"))
    }
}