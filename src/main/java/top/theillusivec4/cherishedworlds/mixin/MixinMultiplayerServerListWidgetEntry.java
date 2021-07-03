package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.FavoritesList;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MixinMultiplayerServerListWidgetEntry {

  @Shadow
  @Final
  private MultiplayerScreen screen;

  @Inject(at = @At("HEAD"), method = "swapEntries", cancellable = true)
  private void cherishedworld$swapEntries(int pos1, int pos2, CallbackInfo ci) {

    if (isNotValidSwap(this.screen.getServerList(), pos1, pos2)) {
      ci.cancel();
    }
  }

  private static boolean isNotValidSwap(ServerList serverList, int pos1, int pos2) {
    ServerInfo data1 = serverList.get(pos1);
    ServerInfo data2 = serverList.get(pos2);
    boolean isFavored1 = FavoritesList.contains(data1.name + data1.address);
    boolean isFavored2 = FavoritesList.contains(data2.name + data2.address);
    return (isFavored1 && !isFavored2) || (!isFavored1 && isFavored2);
  }
}
