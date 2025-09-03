package io.kito.ccgauges.client.gui.screen

import dan200.computercraft.client.gui.ClientInputHandler
import dan200.computercraft.client.gui.widgets.TerminalWidget
import io.kito.ccgauges.common.create.behaviour.ComputedPanelBehaviour
import io.kito.ccgauges.common.world.inventory.ComputedGaugeMenu
import io.kito.kore.common.event.KSubscribe
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.world.inventory.RegisterMenu.Companion.menuType
import net.liukrast.eg.api.logistics.board.BasicPanelScreen
import net.liukrast.eg.content.logistics.board.StringPanelScreen
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

class ComputedPanelScreen(val gaugeMenu: ComputedGaugeMenu, behaviour: ComputedPanelBehaviour) :
    BasicPanelScreen<ComputedPanelBehaviour>(behaviour), MenuAccess<ComputedGaugeMenu>
{

    lateinit var terminalWidget: TerminalWidget

    override fun init() {
        super.init()

        terminalWidget = addRenderableWidget(
            TerminalWidget(
                gaugeMenu.terminal,
                ClientInputHandler(gaugeMenu),
                (width - gaugeMenu.terminal.width * 6) / 2,
                (height - gaugeMenu.terminal.height * 9) / 2 - 10)
        )

        focused = terminalWidget
    }

    override fun getWindowWidth() = gaugeMenu.terminal.width * 4 + 30

    override fun getWindowHeight() = gaugeMenu.terminal.height * 9 + 20

    override fun removed() {
        if (minecraft!!.player != null)
            minecraft!!.player?.let { gaugeMenu.removed(it) }
    }

    override fun tick() {
        super.tick()
        if (!(minecraft!!.player!!.isAlive && !minecraft!!.player!!.isRemoved))
            minecraft!!.player!!.closeContainer()
    }

    override fun getMenu() = gaugeMenu

    override fun onClose() {
        super.onClose()
        minecraft!!.player!!.closeContainer()
    }

    @Scan
    companion object {

        @KSubscribe
        fun RegisterMenuScreensEvent.registerScreen() {
            this.register(menuType(ComputedGaugeMenu::class)) { a, _, _ -> ComputedPanelScreen(a, a.behaviour) }
        }
    }
}