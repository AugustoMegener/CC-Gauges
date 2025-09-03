package io.kito.ccgauges.common.cc.api.gauge

import io.kito.ccgauges.common.Util.allInputs
import io.kito.ccgauges.common.create.behaviour.ComputedGaugeDisplaySource
import io.kito.ccgauges.common.create.behaviour.ComputedPanelBehaviour
import net.liukrast.eg.registry.EGPanelConnections.*
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class GaugeBrain(private val gauge: ComputedPanelBehaviour) : IGaugeAcess {

    override val level = gauge.blockEntity.level!!
    override val blockPos: BlockPos = gauge.blockEntity.blockPos

    override val intInputs get() = gauge.allInputs(INTEGER.get())
    override val restoneInputs get() = gauge.allInputs(REDSTONE.get())
    override val stringInputs get() = gauge.allInputs(STRING.get())
    override val filterInputs get() = gauge.allInputs(FILTER.get())

    override var displaySource
        get() =  gauge.data.displaySource
        set(value) { gauge.data.displaySource = value }

    override var renderBulb
        get() = gauge.data.renderBulb
        set(value) { gauge.data.renderBulb = value }

    override var bulbColor
        get() = gauge.data.bulbColor
        set(value) { gauge.data.bulbColor = value }

    override var tip
        get() = gauge.data.tip
        set(value) { gauge.data.tip = value }

    override fun setIntOutput(value: Int) {
        gauge.data.integer = value
        gauge.blockEntity.setChanged()
    }

    override fun setRedstoneOutput(value: Int) {
        gauge.data.redstoneSignal = value
        gauge.blockEntity.setChanged()
    }

    override fun setStringOutput(value: String) {
        gauge.data.string = value
        gauge.blockEntity.setChanged()
    }

    override fun setFilterOutput(value: ItemStack) {
        gauge.filter = value
        gauge.blockEntity.setChanged()
    }

}