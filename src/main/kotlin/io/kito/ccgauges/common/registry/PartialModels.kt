package io.kito.ccgauges.common.registry

import dev.engine_room.flywheel.lib.model.baked.PartialModel
import io.kito.ccgauges.Ccgauges.local
import io.kito.kore.common.reflect.Scan
import io.kito.kore.util.minecraft.ResourceLocationExt.block

@Scan
object PartialModels  {

    val computedGaugeModel: PartialModel = PartialModel.of(local("computed_gauge").block);
}