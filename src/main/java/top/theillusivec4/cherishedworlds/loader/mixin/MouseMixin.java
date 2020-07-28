package top.theillusivec4.cherishedworlds.loader.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.core.WorldScreenHooks;

@Mixin(Mouse.class)
public class MouseMixin {

  @Inject(at = @At("HEAD"), method = "method_1611([ZDDI)V")
  public void _cherishedworlds_preMouseClick(boolean[] unused, double mouseX, double mouseY, int button, CallbackInfo cb) {
    Screen screen = MinecraftClient.getInstance().currentScreen;

    if (screen instanceof SelectWorldScreen) {
      WorldScreenHooks.checkMouseClick((SelectWorldScreen) screen, mouseX, mouseY);
    }
  }

  @Inject(at = @At("TAIL"), method = "method_1611([ZDDI)V")
  public void _cherishedworlds_postMouseClick(CallbackInfo cb) {
    Screen screen = MinecraftClient.getInstance().currentScreen;

    if (screen instanceof SelectWorldScreen) {
      WorldScreenHooks.disableDeleteButton((SelectWorldScreen) screen);
    }
  }
}
