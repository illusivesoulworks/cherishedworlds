/*
 * Copyright (C) 2018-2021  C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cherished
 * Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

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
  private JoinMultiplayerScreen screen;

  @Shadow
  @Final
  private List<ServerSelectionList.OnlineServerEntry> onlineServers;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "updateOnlineServers", cancellable = true)
  private void _cherishedworlds_updateServers(ServerList serverList, CallbackInfo ci) {
    this.onlineServers.clear();
    List<ServerSelectionList.OnlineServerEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.OnlineServerEntry> others = new ArrayList<>();

    for (int i = 0; i < serverList.size(); ++i) {
      ServerData data = serverList.get(i);
      ServerSelectionList.OnlineServerEntry entry =
          ServerSelectionListNormalEntryAccessor
              .cherishedworlds$createEntry((ServerSelectionList) (Object) this, this.screen, data);

      if (FavoritesList.contains(data.name + data.ip)) {
        favorites.add(entry);
      } else {
        others.add(entry);
      }
    }
    this.onlineServers.addAll(favorites);
    this.onlineServers.addAll(others);

    for (int i = 0; i < this.onlineServers.size(); i++) {
      serverList.replace(i, this.onlineServers.get(i).getServerData());
    }
    this.refreshEntries();
    serverList.save();
    ci.cancel();
  }

  @Shadow
  abstract void refreshEntries();
}
