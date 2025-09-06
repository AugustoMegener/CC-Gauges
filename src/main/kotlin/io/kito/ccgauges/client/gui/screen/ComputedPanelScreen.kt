package io.kito.ccgauges.client.gui.screen

import dan200.computercraft.client.gui.ClientInputHandler
import dan200.computercraft.client.gui.ComputerScreen
import dan200.computercraft.client.gui.widgets.ComputerSidebar
import dan200.computercraft.client.gui.widgets.TerminalWidget
import io.kito.ccgauges.Ccgauges.local
import io.kito.ccgauges.common.create.behaviour.ComputedPanelBehaviour
import io.kito.ccgauges.common.world.inventory.ComputedGaugeMenu
import io.kito.kore.common.event.KSubscribe
import io.kito.kore.common.reflect.Scan
import io.kito.kore.common.world.inventory.RegisterMenu.Companion.menuType
import net.liukrast.eg.api.logistics.board.BasicPanelScreen
import net.liukrast.eg.content.logistics.board.StringPanelScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

class ComputedPanelScreen(val gaugeMenu: ComputedGaugeMenu, behaviour: ComputedPanelBehaviour) :
    BasicPanelScreen<ComputedPanelBehaviour>(behaviour), MenuAccess<ComputedGaugeMenu>
{

    val inputHandler = ClientInputHandler(gaugeMenu)
    lateinit var terminalWidget: TerminalWidget

    val terminalXPos get() = guiLeft + (windowWidth - gaugeMenu.terminal.width * 6) / 2
    val terminalYPos get() = guiTop + (windowHeight - gaugeMenu.terminal.height * 9) / 2 - 10

    val buttonX get() = guiLeft + (terminalXPos - 2 - guiLeft) / 2 - 10
    val buttonY get() = terminalYPos + (gaugeMenu.terminal.height * 9) / 2 - 19


    override fun init() {
        super.init()

        terminalWidget = addRenderableWidget(
            TerminalWidget(
                gaugeMenu.terminal,
                inputHandler,
                terminalXPos,
                terminalYPos
            )
        )

        ComputerSidebar.addButtons(
            gaugeMenu::isOn,
            inputHandler,
            { addRenderableWidget(it) },
            buttonX,
            buttonY
        )

        focused = terminalWidget
    }

    override fun getWindowWidth() = gaugeMenu.terminal.width * 4 + 60

    override fun getWindowHeight() = gaugeMenu.terminal.height * 9 + 20

    override fun renderWindow(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.renderWindow(graphics, mouseX, mouseY, partialTicks)

        graphics.blitSprite(
            brassFrameLocation, terminalXPos - 2, terminalYPos - 2,
            gaugeMenu.terminal.width * 6 + 8, gaugeMenu.terminal.height * 9 + 8
        )

        graphics.blitSprite(brassBgLocation, buttonX, buttonY, 20, 38)
    }

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

        val brassFrameLocation = local("brass_frame")
        val brassBgLocation = local("brass_bg")

        @KSubscribe
        fun RegisterMenuScreensEvent.registerScreen() {
            this.register(menuType(ComputedGaugeMenu::class)) { a, _, _ -> ComputedPanelScreen(a, a.behaviour) }
        }
    }
}