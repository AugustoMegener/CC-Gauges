package io.kito.ccgauges.common.create.behaviour

import io.kito.kore.common.data.codec.CodecSource
import io.kito.kore.common.reflect.Scan
import net.minecraft.util.StringRepresentable

enum class ComputedGaugeDisplaySource(private val serialName: String) : StringRepresentable {
    FILTER("filter"),
    INTEGER("integer"),
    REDSTONE("redstone"),
    STRING("string");


    override fun getSerializedName() = serialName

    @Scan
    companion object  {

        val codec: StringRepresentable.EnumCodec<ComputedGaugeDisplaySource> =
            StringRepresentable.fromEnum(ComputedGaugeDisplaySource::values)

        @CodecSource
        fun codec() = codec
    }
}