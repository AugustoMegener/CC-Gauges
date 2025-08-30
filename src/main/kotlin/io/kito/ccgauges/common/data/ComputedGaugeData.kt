package io.kito.ccgauges.common.data

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Encoder
import com.mojang.serialization.codecs.RecordCodecBuilder
import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import dan200.computercraft.shared.computer.core.ServerContext
import dan200.computercraft.shared.computer.core.TerminalSize
import dan200.computercraft.shared.config.ConfigSpec
import dan200.computercraft.shared.network.container.ComputerContainerData
import dan200.computercraft.shared.util.IDAssigner
import io.kito.ccgauges.ComputedGaugeDisplaySource
import io.kito.ccgauges.ComputedPanelBehaviour
import io.kito.ccgauges.common.registry.Items.computedGaugeItem
import io.kito.kore.common.data.Save
import io.kito.kore.common.data.codec.CodecSource
import io.kito.kore.common.data.codec.KCodecSerializer
import io.kito.kore.common.data.nbt.KNBTSerializable
import io.kito.kore.common.reflect.Scan
import net.minecraft.core.UUIDUtil
import net.minecraft.server.level.ServerLevel
import java.util.*
import kotlin.math.max
import kotlin.math.min


class ComputedGaugeData : KNBTSerializable {

    @Save
    var instanceId = UUID.randomUUID()

    @Save
    var computerId: Int = -1

    @Save
    var label = ""

    @Save
    var number: Int = 0

    @Save
    var redstoneSignal: Int = 0
        set(value) { field = max(min(value, 15), 0) }

    @Save
    var string: String = ""

    @Save
    var displaySource: ComputedGaugeDisplaySource = ComputedGaugeDisplaySource.STRING

    fun computer(panel: ComputedPanelBehaviour): ServerComputer {
        val level = panel.blockEntity.level!!
        val server = level.server ?: error("Cannot access server computer on the client.")

        var computer = ServerContext.get(server).registry()[instanceId]
        if (computer == null) {
            if (computerId < 0) {
                computerId = ComputerCraftAPI.createUniqueNumberedSaveDir(server, IDAssigner.COMPUTER)
                panel.blockEntity.setChanged()
            }


            computer = ServerComputer(
                level as ServerLevel, panel.blockEntity.blockPos,
                ServerComputer.properties(computerId, ComputerFamily.ADVANCED)
                    .label(label)
                    .terminalSize(TerminalSize(ConfigSpec.computerTermWidth.get(), ConfigSpec.computerTermHeight.get()))
            )

            instanceId = computer.register()
        }

        return computer
    }

    fun createComputerData(panel: ComputedPanelBehaviour) =
        ComputerContainerData(panel.computer, computedGaugeItem.defaultInstance)

    @Scan
    companion object {

        @CodecSource
        fun uuidCodec() = UUIDUtil.CODEC

    }
}