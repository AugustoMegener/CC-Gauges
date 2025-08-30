package io.kito.ccgauges.common.world.inventory

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock.PanelSlot
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory
import dan200.computercraft.shared.network.container.ComputerContainerData
import io.kito.ccgauges.ComputedPanelBehaviour
import io.kito.kore.common.world.inventory.RegisterMenu
import io.kito.kore.common.world.inventory.RegisterMenu.Companion.menuType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

@RegisterMenu("computed_gauge")
class ComputedGaugeMenu: ComputerMenuWithoutInventory
{
    val behaviour: ComputedPanelBehaviour

    constructor(containerId: Int,
                playerInv: Inventory,
                behaviour: ComputedPanelBehaviour
    ) :
        super(menuType(ComputedGaugeMenu::class), containerId, playerInv, { true }, behaviour.computer)
    {
        this.behaviour = behaviour
    }

    constructor(containerId: Int, inv: Inventory, buf: RegistryFriendlyByteBuf) :
            super(menuType(ComputedGaugeMenu::class), containerId, inv, ComputerContainerData.STREAM_CODEC.decode(buf))
    {
        behaviour = (inv.player.level().getBlockEntity(buf.readBlockPos()) as FactoryPanelBlockEntity)
            .panels!![buf.readEnum(PanelSlot::class.java)]!! as ComputedPanelBehaviour

    }



}