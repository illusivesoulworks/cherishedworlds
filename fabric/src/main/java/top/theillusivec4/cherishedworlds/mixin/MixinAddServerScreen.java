package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.FavoritesList;

@Mixin(AddServerScreen.class)
public class MixinAddServerScreen {

  @Shadow
  @Final
  private ServerInfo server;
  @Shadow
  private TextFieldWidget addressField;
  @Shadow
  private TextFieldWidget serverNameField;

  @Inject(at = @At("HEAD"), method = "addAndClose")
  private void cherishedworlds$addAndClose(CallbackInfo ci) {
    String key = this.server.name + this.server.address;

    if (FavoritesList.contains(key)) {
      FavoritesList.remove(key);
      FavoritesList.add(this.serverNameField.getText() + this.addressField.getText());
    }
  }
}
