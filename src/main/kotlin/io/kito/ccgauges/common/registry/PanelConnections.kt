package io.kito.ccgauges.common.registry

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.shared.ModRegistry.Blocks.*
import dan200.computercraft.shared.peripheral.modem.wired.CableBlockEntity
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemBlock
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemBlockEntity
import io.kito.ccgauges.ID
import io.kito.kore.common.event.KSubscribe
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.registry.SimpleRegister
import net.liukrast.eg.api.EGRegistries.PANEL_CONNECTION_REGISTRY
import net.liukrast.eg.api.logistics.board.PanelConnection
import net.neoforged.neoforge.event.level.LevelEvent
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import java.util.*

@Scan
object PanelConnections : SimpleRegister<PanelConnection<*>>(ID, PANEL_CONNECTION_REGISTRY) {

    val peripheralConnection by "peripheral" { PanelConnection<IPeripheral>() }

    @KSubscribe
    fun LevelEvent.Load.registerExtras() {
        peripheralConnection.addListener(
            { _, state, _, be ->
                Optional.ofNullable(
                    (be as? WirelessModemBlockEntity)
                        ?.getPeripheral(state.getValue(WirelessModemBlock.FACING))
                )
            }, WIRELESS_MODEM_NORMAL.get(), WIRELESS_MODEM_ADVANCED.get()
        )
        peripheralConnection.addListener(
            { _, _, _, be ->
                Optional.ofNullable(
                    (be as? CableBlockEntity)
                        ?.getPeripheral(null)
                )
            }, CABLE.get()
        )
    }
}