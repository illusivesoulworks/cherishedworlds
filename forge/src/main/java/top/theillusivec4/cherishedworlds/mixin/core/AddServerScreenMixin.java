package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(AddServerScreen.class)
public class AddServerScreenMixin {

  @Shadow
  @Final
  private ServerData serverData;
  @Shadow
  private TextFieldWidget textFieldServerAddress;
  @Shadow
  private TextFieldWidget textFieldServerName;

  @Inject(at = @At("HEAD"), method = "onButtonServerAddPressed")
  private void cherishedworlds$onButtonServerAddPressed(CallbackInfo ci) {
    String key = this.serverData.serverName + this.serverData.serverIP;

    if (FavoritesList.contains(key)) {
      FavoritesList.remove(key);
      FavoritesList.add(this.textFieldServerName.getText() + this.textFieldServerAddress.getText());
    }
  }
}
