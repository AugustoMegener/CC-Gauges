package io.kito.ccgauges.common.cc.api.gauge

import com.simibubi.create.content.logistics.filter.FilterItemStack
import io.kito.ccgauges.common.create.behaviour.ComputedGaugeDisplaySource
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

interface IGaugeAcess {

    val level: Level
    val blockPos: BlockPos

    val intInputs: Collection<Int>
    val restoneInputs: Collection<Int>
    val stringInputs: Collection<String>
    val filterInputs: Collection<FilterItemStack>

    var displaySource: ComputedGaugeDisplaySource

    var renderBulb: Boolean
    var tip: Component

    fun setIntOutput(value: Int)
    fun setRedstoneOutput(value: Int)
    fun setStringOutput(value: String)
    fun setFilterOutput(value: ItemStack)
}