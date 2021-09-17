package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(JoinMultiplayerScreen.class)
public class MultiplayerScreenMixin {

  @Shadow
  protected ServerSelectionList serverSelectionList;

  @Shadow
  private Button deleteButton;

  @Inject(at = @At("TAIL"), method = "onSelectedChange")
  private void _cherishedworlds_initButtons(CallbackInfo ci) {
    ServerSelectionList.Entry entry = this.serverSelectionList.getSelected();

    if (entry instanceof ServerSelectionList.OnlineServerEntry) {
      ServerData data = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
      this.deleteButton.active = !FavoritesList.contains(data.name + data.ip);
    }
  }
}
