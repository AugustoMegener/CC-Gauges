package io.kito.ccgauges.common.registry

import dev.engine_room.flywheel.lib.model.baked.PartialModel
import io.kito.ccgauges.Ccgauges.local
import io.kito.kore.common.reflect.Scan
import io.kito.kore.util.minecraft.ResourceLocationExt.block

@Scan
object PartialModels  {

    val computedGaugeModel: PartialModel = PartialModel.of(local("computed_gauge").block)
    val stockComputedGaugeModel: PartialModel = PartialModel.of(local("stock_computed_gauge").block)
    val tableGaugeModel: PartialModel = PartialModel.of(local("table_gauge").block)
}