package top.theillusivec4.cherishedworlds.loader.mixin;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.core.WorldScreenHooks;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "init")
  public void _cherishedworlds_init(CallbackInfo cb) {
    WorldScreenHooks.init((SelectWorldScreen) (Object) this);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "render")
  public void _cherishedworlds_render(MatrixStack matrices, int mouseX, int mouseY, float delta,
      CallbackInfo cb) {
    WorldScreenHooks.render((SelectWorldScreen) (Object) this, matrices, mouseX, mouseY);
  }
}
