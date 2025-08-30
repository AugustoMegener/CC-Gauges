package io.kito.ccgauges.common.network

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import dan200.computercraft.client.gui.ComputerScreen
import dan200.computercraft.shared.network.container.ComputerContainerData
import io.kito.ccgauges.Ccgauges.local
import io.kito.ccgauges.ComputedPanelBehaviour
import io.kito.kore.common.data.codec.stream.Send
import io.kito.kore.common.data.codec.stream.StreamCodecSource
import io.kito.kore.common.network.Packet
import io.kito.kore.common.network.PacketTarget
import io.kito.kore.common.network.PacketType
import io.kito.kore.common.network.RegisterPacket
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class OpenComputedGaugeMenuPacket(@Send val pos: BlockPos, @Send val slot: FactoryPanelBlock.PanelSlot) :
    Packet(OpenComputedGaugeMenuPacket)
{
    override fun invoke(ctx: IPayloadContext?) {
        val player = ctx?.player() ?: return

        val gauge = (player.level().getBlockEntity(pos) as? FactoryPanelBlockEntity)?.panels?.get(slot) as?
                ComputedPanelBehaviour ?: return

        if (!gauge.computer.isOn) gauge.computer.turnOn()

        (player as? ServerPlayer)?.openMenu(gauge) {
            ComputerContainerData.STREAM_CODEC.encode(it, gauge.data.createComputerData(gauge))
            it.writeBlockPos(pos)
            it.writeEnum(gauge.slot)
        }
    }

    @RegisterPacket("1", PacketTarget.SERVER)
    companion object : PacketType<OpenComputedGaugeMenuPacket>(
        local("open_computed_gauge_menu"), OpenComputedGaugeMenuPacket::class
    ) {

        @StreamCodecSource
        fun blockPos() = BlockPos.STREAM_CODEC

        @StreamCodecSource
        fun panelSlot() = FactoryPanelBlock.PanelSlot.STREAM_CODEC
    }
}