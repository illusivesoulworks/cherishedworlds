package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(EditServerScreen.class)
public class AddServerScreenMixin {

  @Shadow
  @Final
  private ServerData serverData;
  @Shadow
  private EditBox ipEdit;
  @Shadow
  private EditBox nameEdit;

  @Inject(at = @At("HEAD"), method = "onButtonServerAddPressed")
  private void cherishedworlds$onButtonServerAddPressed(CallbackInfo ci) {
    String key = this.serverData.name + this.serverData.ip;

    if (FavoritesList.contains(key)) {
      FavoritesList.remove(key);
      FavoritesList.add(this.nameEdit.getValue() + this.ipEdit.getValue());
    }
  }
}
