package io.kito.ccgauges.common

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.simibubi.create.content.logistics.filter.FilterItemStack
import net.liukrast.eg.api.logistics.board.AbstractPanelBehaviour
import net.liukrast.eg.api.logistics.board.PanelConnection
import net.liukrast.eg.registry.EGPanelConnections.FILTER
import net.minecraft.world.item.ItemStack
import java.lang.reflect.Type

object Util {

    fun <T> AbstractPanelBehaviour.allInputs(type: PanelConnection<T>) =
        arrayListOf<T>().also {
            consumeForPanels(type) { i -> it += i }
            consumeForExtra(type) { _, i -> it += i }
        }

    fun AbstractPanelBehaviour.allFilterInputs() =
        arrayListOf<FilterItemStack>().also {
            consumeForPanels(FILTER.get()) { i -> it += i }
            consumeForExtra(FILTER.get()) { _, i -> it += i }
        }


    fun Map<*, *>.asJsonElement(): JsonElement = Gson().toJsonTree(this)

    fun JsonElement.asMap(): Map<*, *> =
        Gson().fromJson(this, object : TypeToken<Map<String, Any?>>() {}.type)

    object NullJsonSerializationContext : JsonSerializationContext {
        override fun serialize(src: Any?) = error("Can not use a NullJsonSerializationContext")

        override fun serialize(src: Any?, typeOfSrc: Type?) =error("Can not use a NullJsonSerializationContext")
    }
}