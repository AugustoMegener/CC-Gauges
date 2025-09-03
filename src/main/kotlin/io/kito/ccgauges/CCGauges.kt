package io.kito.ccgauges

import io.kito.ccgauges.common.cc.api.gauge.GaugeAPI
import io.kito.kore.KMod
import io.kito.kore.common.datagen.DataGenHelper
import io.kito.kore.common.reflect.Scan
import net.neoforged.bus.EventBus
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@KMod("ccgauges")
fun init() {
    GaugeAPI.register()
}

@Scan
object DG : DataGenHelper(ID)