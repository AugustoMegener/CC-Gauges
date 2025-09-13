package io.kito.ccgauges.mixin;

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBehaviour;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.liukrast.eg.api.logistics.board.PanelConnection;
import net.liukrast.eg.registry.EGPanelConnections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(EGPanelConnections.class)
public class EGPanelConnectionsMixin {

    @Accessor("FACTORY_CONNECTIONS")
    static Map<PanelConnection<?>, Function<FactoryPanelBehaviour, ?>> factoryConnections() {
        throw new AssertionError();
    }

    @Inject(method = "initDefaults", at = @At("TAIL"))
    private static void initDefaults(CallbackInfo ci) {
        factoryConnections().put(EGPanelConnections.FILTER.get(),
                (FactoryPanelBehaviour i) -> FilterItemStack.of(i.getFilter()));
    }
}
