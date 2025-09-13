package io.kito.ccgauges.common.cc.api.stock

import com.simibubi.create.content.logistics.BigItemStack
import com.simibubi.create.content.logistics.packager.InventorySummary
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts
import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.ILuaAPI
import dan200.computercraft.api.lua.LuaFunction
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.Util.asJsonElement
import io.kito.ccgauges.common.Util.asList
import io.kito.ccgauges.common.Util.asMap
import io.kito.ccgauges.common.registry.ComputerComponents.stockComponent
import io.kito.kore.util.UNCHECKED_CAST
import io.kito.kore.util.minecraft.jsonOps
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack


class StockAPI(val stock: IStockAcess) : ILuaAPI {
    override fun getNames() = arrayOf("stock")

    override fun getModuleName() = "$ID.stock"

    @LuaFunction(mainThread = true)
    fun id() = stock.networkId

    @LuaFunction(mainThread = true)
    fun networkSummary() =
        InventorySummary.CODEC.encodeStart(jsonOps, LogisticsManager.getSummaryOfNetwork(stock.networkId, true))
            .orThrow.asList()

    @LuaFunction(mainThread = true)
    fun getStockOf(stack: Map<*, *>) = LogisticsManager.getStockOf(
        stock.networkId, ItemStack.CODEC.decode(jsonOps, stack.asJsonElement()).orThrow.first, null
    )

    @Suppress(UNCHECKED_CAST)
    @LuaFunction(mainThread = true)
    fun request(args: IArguments) {
        LogisticsManager.performPackageRequests(LogisticsManager.findPackagersForRequest(
            stock.networkId,
            PackageOrderWithCrafts.simple(
                (args.getTable(1) as Map<*, Map<*, *>>).values.map {
                    BigItemStack.CODEC.decode(jsonOps, it.asJsonElement()).orThrow.first
                }
            ), null, args.getString(0) as String)
        )
    }

    @Suppress(UNCHECKED_CAST)
    @LuaFunction(mainThread = true)
    fun requestCrafting(args: IArguments) {
        val items = (args.getTable(2) as Map<String, Map<*, *>>).map { (c, v) ->
            c[0] to BigItemStack.CODEC.decode(jsonOps, v.asJsonElement()).orThrow.first
        }.toMap()

        LogisticsManager.performPackageRequests(LogisticsManager.findPackagersForRequest(
            stock.networkId,
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
