package io.kito.ccgauges

import io.kito.kore.KMod
import io.kito.kore.common.datagen.DataGenHelper
import io.kito.kore.common.reflect.Scan

@KMod("ccgauges")
fun init() {

}

@Scan
object DG : DataGenHelper(ID)