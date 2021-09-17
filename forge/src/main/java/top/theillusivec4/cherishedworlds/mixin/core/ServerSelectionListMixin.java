package top.theillusivec4.cherishedworlds.mixin.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(ServerSelectionList.class)
public abstract class ServerSelectionListMixin {

  @Shadow
  @Final
  private MultiplayerScreen owner;

  @Shadow
  @Final
  private List<ServerSelectionList.NormalEntry> serverListInternet;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "updateOnlineServers", cancellable = true)
  private void _cherishedworlds_updateServers(ServerList serverList, CallbackInfo ci) {
    this.serverListInternet.clear();
    List<ServerSelectionList.NormalEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.NormalEntry> others = new ArrayList<>();

    for (int i = 0; i < serverList.countServers(); ++i) {
      ServerData data = serverList.getServerData(i);
      ServerSelectionList.NormalEntry entry =
          ServerSelectionListNormalEntryAccessor
              .cherishedworlds$createEntry((ServerSelectionList) (Object) this, this.owner, data);

      if (FavoritesList.contains(data.serverName + data.serverIP)) {
        favorites.add(entry);
      } else {
        others.add(entry);
      }
    }
    this.serverListInternet.addAll(favorites);
    this.serverListInternet.addAll(others);

    for (int i = 0; i < this.serverListInternet.size(); i++) {
      serverList.set(i, this.serverListInternet.get(i).getServerData());
    }
    this.setList();
    serverList.saveServerList();
    ci.cancel();
  }

  @Shadow
  abstract void setList();
}
