package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.mixin.CherishedWorldsMixinHooks;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSummary.class)
public class MixinLevelSummary {

  @Inject(
      at = @At("RETURN"),
      method = "canDelete",
      cancellable = true
  )
  private void cherishedworlds$canDelete(CallbackInfoReturnable<Boolean> cir) {

    if (!CherishedWorldsMixinHooks.canDelete((LevelSummary) (Object) this)) {
      cir.setReturnValue(false);
    }
  }
}
