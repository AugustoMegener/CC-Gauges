package io.kito.ccgauges.common.cc.api.stock

import com.simibubi.create.content.logistics.packager.InventorySummary
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.UUID

interface IStockAcess {

    val networkId: UUID
    val level: Level
}