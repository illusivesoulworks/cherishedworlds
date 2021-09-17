package top.theillusivec4.cherishedworlds.mixin.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
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
  private JoinMultiplayerScreen owner;

  @Shadow
  @Final
  private List<ServerSelectionList.OnlineServerEntry> serverListInternet;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "updateOnlineServers", cancellable = true)
  private void _cherishedworlds_updateServers(ServerList serverList, CallbackInfo ci) {
    this.serverListInternet.clear();
    List<ServerSelectionList.OnlineServerEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.OnlineServerEntry> others = new ArrayList<>();

    for (int i = 0; i < serverList.size(); ++i) {
      ServerData data = serverList.get(i);
      ServerSelectionList.OnlineServerEntry entry =
          ServerSelectionListNormalEntryAccessor
              .cherishedworlds$createEntry((ServerSelectionList) (Object) this, this.owner, data);

      if (FavoritesList.contains(data.name + data.ip)) {
        favorites.add(entry);
      } else {
        others.add(entry);
      }
    }
    this.serverListInternet.addAll(favorites);
    this.serverListInternet.addAll(others);

    for (int i = 0; i < this.serverListInternet.size(); i++) {
      serverList.replace(i, this.serverListInternet.get(i).getServerData());
    }
    this.setList();
    serverList.save();
    ci.cancel();
  }

  @Shadow
  abstract void setList();
}
