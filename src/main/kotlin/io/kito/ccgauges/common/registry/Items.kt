package io.kito.ccgauges.common.registry

import io.kito.ccgauges.DG.named
import io.kito.ccgauges.DG.recipe
import io.kito.ccgauges.ID
import io.kito.ccgauges.common.registry.PanelTypes.computedGauge
import io.kito.ccgauges.common.registry.PanelTypes.stockComputedGauge
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.ItemRegister
import io.kito.kore.util.minecraft.EN_US
import net.liukrast.eg.api.logistics.board.PanelBlockItem
import net.liukrast.eg.content.item.LogisticallyLinkedPanelBlockItem
import net.minecraft.core.NonNullList
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Scan
object Items : ItemRegister(ID) {

    val computedGaugeItem by "computed_gauge" of { PanelBlockItem(::computedGauge, it) } where {
        named(EN_US to "Computed Gauge")

        recipe(itemId) { _, item ->
            ShapelessRecipe(ID, CraftingBookCategory.MISC, item.defaultInstance, NonNullList.of(Ingredient.of()))
        }
    }

    val stockComputedGaugeItem by "stock_computed_gauge" of {
        LogisticallyLinkedPanelBlockItem(::stockComputedGauge, it)
    } where { named(EN_US to "Stock Computed Gauge") }

}