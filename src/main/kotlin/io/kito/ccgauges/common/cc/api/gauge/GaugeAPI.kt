package io.kito.ccgauges.common.cc.api.gauge

import com.google.gson.JsonParseException
import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.lua.ILuaAPI
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.Util.asJsonElement
import io.kito.ccgauges.common.Util.asMap
import io.kito.ccgauges.common.create.behaviour.ComputedGaugeDisplaySource
import io.kito.ccgauges.common.registry.ComputerComponents.gaugeComponent
import io.kito.kore.util.minecraft.jsonOps
import net.liukrast.eg.content.logistics.board.IntPanelBehaviour
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack


class GaugeAPI(val gauge: IGaugeAcess) : ILuaAPI {
    override fun getNames() = arrayOf("gauge")

    override fun getModuleName() = "$ID.gauge"


    @LuaFunction
    fun intInputs() = gauge.intInputs.toTypedArray()

    @LuaFunction
    fun redstoneInputs() = gauge.restoneInputs.toTypedArray()

    @LuaFunction
    fun stringInputs() = gauge.stringInputs.toTypedArray()

    @LuaFunction
    fun filterInputs() = gauge.filterInputs.map {
        ItemStack.CODEC.encodeStart(jsonOps, it.item()).orThrow.asMap()
    }.toTypedArray()

    @LuaFunction
    fun targetAmount() = gauge.targetAmount

    @LuaFunction fun getDisplaySource() = gauge.displaySource
    @LuaFunction fun setDisplaySource(value: ComputedGaugeDisplaySource) { gauge.displaySource = value }

    @LuaFunction fun setIntOutput(value: Int) = gauge.setIntOutput(value)
    @LuaFunction fun setRedstoneOutput(value: Int) = gauge.setRedstoneOutput(value)
    @LuaFunction fun setStringOutput(value: String) = gauge.setStringOutput(value)
    @LuaFunction fun setFilterOutput(value: Map<*, *>) {
        try { gauge.setFilterOutput(ItemStack.CODEC.decode(jsonOps, value.asJsonElement()).orThrow.first) }
        catch (error: IllegalStateException) { throw LuaException(error.message) }
    }

    @LuaFunction fun isRenderingBulb() = gauge.renderBulb
    @LuaFunction fun setRenderBulb(value: Boolean) { gauge.renderBulb = value }

    @LuaFunction fun getTip() = Component.Serializer.toJson(gauge.tip, gauge.level.registryAccess())

    @LuaFunction fun setTip(value: String) {
        try { gauge.tip = Component.Serializer.fromJson(value, gauge.level.registryAccess())!! }
        catch (error: JsonParseException) { throw LuaException(error.message) }
    }

    companion object {
        fun register() {
            ComputerCraftAPI.registerAPIFactory { pc ->
                pc.getComponent(gaugeComponent)?.let { GaugeAPI(it) }
            }
        }
    }
}
