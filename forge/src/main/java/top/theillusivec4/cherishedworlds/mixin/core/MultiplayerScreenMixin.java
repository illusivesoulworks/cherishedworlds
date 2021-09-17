package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

  @Shadow
  protected ServerSelectionList serverListSelector;

  @Shadow
  private Button btnDeleteServer;

  @Inject(at = @At("TAIL"), method = "func_214295_b")
  private void _cherishedworlds_initButtons(CallbackInfo ci) {
    ServerSelectionList.Entry entry = this.serverListSelector.getSelected();

    if (entry instanceof ServerSelectionList.NormalEntry) {
      ServerData data = ((ServerSelectionList.NormalEntry) entry).getServerData();
      this.btnDeleteServer.active = !FavoritesList.contains(data.serverName + data.serverIP);
    }
  }
}
