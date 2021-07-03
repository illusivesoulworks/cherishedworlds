package top.theillusivec4.cherishedworlds.mixin;

import java.util.ArrayList;
import java.util.List;
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

@Mixin(MultiplayerServerListWidget.class)
public abstract class MixinMultiplayerServerListWidget {

  @Shadow
  @Final
  private MultiplayerScreen screen;

  @Shadow
  @Final
  private List<MultiplayerServerListWidget.ServerEntry> servers;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "setServers", cancellable = true)
  private void cherishedworlds$setServers(ServerList serverList, CallbackInfo ci) {
    this.servers.clear();
    List<MultiplayerServerListWidget.ServerEntry> favorites = new ArrayList<>();
    List<MultiplayerServerListWidget.ServerEntry> others = new ArrayList<>();

    for (int i = 0; i < serverList.size(); ++i) {
      ServerInfo serverInfo = serverList.get(i);
      MultiplayerServerListWidget.ServerEntry entry = AccessorMultiplayerServerListWidgetEntry
          .cherishedworlds$createEntry((MultiplayerServerListWidget) (Object) this, this.screen,
              serverInfo);

      if (FavoritesList.contains(serverInfo.name + serverInfo.address)) {
        favorites.add(entry);
      } else {
        others.add(entry);
      }
    }
    this.servers.addAll(favorites);
    this.servers.addAll(others);

    for (int i = 0; i < this.servers.size(); i++) {
      serverList.set(i, this.servers.get(i).getServer());
    }
    this.updateEntries();
    serverList.saveFile();
    ci.cancel();
  }

  @Shadow
  abstract void updateEntries();
}
