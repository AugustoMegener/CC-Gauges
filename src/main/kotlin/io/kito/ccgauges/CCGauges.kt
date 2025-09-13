package io.kito.ccgauges

import io.kito.ccgauges.common.cc.api.gauge.GaugeAPI
import io.kito.ccgauges.common.cc.api.stock.StockAPI
import io.kito.kore.KMod
import io.kito.kore.common.datagen.DataGenHelper
import io.kito.kore.common.reflect.Scan

@KMod("ccgauges")
fun init() {
    GaugeAPI.register()
    StockAPI.register()
}

@Scan
object DG : DataGenHelper(ID)