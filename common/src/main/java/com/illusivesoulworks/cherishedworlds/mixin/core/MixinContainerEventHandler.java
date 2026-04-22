package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.mixin.CherishedWorldsMixinHooks;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Optional;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerEventHandler.class)
public interface MixinContainerEventHandler {

  @ModifyReturnValue(
      at = @At("RETURN"),
      method = "getChildAt(DD)Ljava/util/Optional;"
  )
  private Optional<GuiEventListener> cherishedworlds$getChildAt(Optional<GuiEventListener> original,
                                                                double x, double y) {
    return CherishedWorldsMixinHooks.getChildAt((ContainerEventHandler) this, original, x, y);
  }
}
