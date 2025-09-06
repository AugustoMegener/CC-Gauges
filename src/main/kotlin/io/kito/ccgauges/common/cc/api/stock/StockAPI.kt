package io.kito.ccgauges.common.cc.api.stock

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.simibubi.create.content.logistics.BigItemStack
import com.simibubi.create.content.logistics.filter.FilterItemStack
import com.simibubi.create.content.logistics.packager.InventorySummary
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts
import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.ILuaAPI
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.Util
import io.kito.ccgauges.common.Util.asJsonElement
import io.kito.ccgauges.common.Util.asMap
import io.kito.ccgauges.common.create.behaviour.ComputedGaugeDisplaySource
import io.kito.ccgauges.common.registry.ComputerComponents.gaugeComponent
import io.kito.ccgauges.common.registry.ComputerComponents.stockComponent
import io.kito.kore.util.UNCHECKED_CAST
import io.kito.kore.util.minecraft.jsonOps
import net.liukrast.eg.content.logistics.board.PassivePanelBehaviour
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.reflect.KType


class StockAPI(val gauge: IStockAcess) : ILuaAPI {
    override fun getNames() = arrayOf("stock")

    override fun getModuleName() = "$ID.stock"

    @LuaFunction
    fun networkId() = gauge.networkId.toString()

    @LuaFunction
    fun networkSummary(): Map<*, *> {
        val a = LogisticsManager.getSummaryOfNetwork(gauge.networkId, true)

        return InventorySummary.CODEC.encodeStart(
            jsonOps, a
        ).orThrow.asMap()
    }

    @LuaFunction
    fun getStockOf(stack: Map<*, *>) = LogisticsManager.getStockOf(
        gauge.networkId, ItemStack.CODEC.decode(jsonOps, stack.asJsonElement()).orThrow.first, null
    )

    @Suppress(UNCHECKED_CAST)
    @LuaFunction(unsafe = true)
    fun request(args: IArguments) {
        LogisticsManager.performPackageRequests(LogisticsManager.findPackagersForRequest(
            gauge.networkId,
            PackageOrderWithCrafts.simple(
                (args.getTable(1) as Map<*, Map<*, *>>).values.map {
                    BigItemStack.CODEC.decode(jsonOps, it.asJsonElement()).orThrow.first
                }
            ), null, args.getString(0) as String)
        )
    }

    @Suppress(UNCHECKED_CAST)
    @LuaFunction(unsafe = true)
    fun requestCrafting(args: IArguments) {
        val items = (args.getTable(2) as Map<String, Map<*, *>>).map { (c, v) ->
            c[0] to BigItemStack.CODEC.decode(jsonOps, v.asJsonElement()).orThrow.first
        }.toMap()

        LogisticsManager.performPackageRequests(LogisticsManager.findPackagersForRequest(
            gauge.networkId,
            PackageOrderWithCrafts.singleRecipe(
                (args.getTable(1) as Map<*, String>).values.joinToString("").map { c ->
                    items[c]!!.let { BigItemStack(it.stack, it.count) }
                }
            ), null, args.getString(0))
        )
    }


    companion object {
        fun register() {
            ComputerCraftAPI.registerAPIFactory { pc ->
                pc.getComponent(stockComponent)?.let { StockAPI(it) }
            }
        }
    }
}
