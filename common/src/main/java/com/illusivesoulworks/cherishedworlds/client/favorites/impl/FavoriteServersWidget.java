/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.cherishedworlds.client.favorites.impl;

import com.illusivesoulworks.cherishedworlds.client.favorites.AbstractFavoritesListWidget;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorJoinMultiplayerScreen;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorNetworkServerEntry;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.LanServer;

public class FavoriteServersWidget
    extends AbstractFavoritesListWidget<ServerSelectionList, ServerSelectionList.Entry> {

  private final JoinMultiplayerScreen joinMultiplayerScreen;

  public FavoriteServersWidget(JoinMultiplayerScreen joinMultiplayerScreen) {
    super(joinMultiplayerScreen);
    this.joinMultiplayerScreen = joinMultiplayerScreen;
  }

  @Override
  public String getKey(ServerSelectionList.Entry entry) {
    String key = "";

    if (entry instanceof ServerSelectionList.OnlineServerEntry onlineServerEntry) {
      ServerData serverData = onlineServerEntry.getServerData();
      key = serverData.name + serverData.ip;
    } else if (entry instanceof ServerSelectionList.NetworkServerEntry networkServerEntry) {
      LanServer serverData = ((AccessorNetworkServerEntry) networkServerEntry).getServerData();
      key = serverData.getAddress();
    }
    return key;
  }

  @Override
  public ServerSelectionList getList() {
    return ((AccessorJoinMultiplayerScreen) this.parentScreen).getSelectionList();
  }

  @Override
  protected String getNarratableId(ServerSelectionList.Entry entry) {
    String id = "";

    if (entry instanceof ServerSelectionList.OnlineServerEntry onlineServerEntry) {
      ServerData serverData = onlineServerEntry.getServerData();
      id = serverData.name;
    } else if (entry instanceof ServerSelectionList.NetworkServerEntry networkServerEntry) {
      LanServer serverData = ((AccessorNetworkServerEntry) networkServerEntry).getServerData();
      id = serverData.getAddress();
    }
    return id;
  }

  @Override
  protected void updateList() {

    if (this.list != null) {
      this.list.updateOnlineServers(this.joinMultiplayerScreen.getServers());
      ServerSelectionList.Entry selected = this.list.getSelected();

      if (selected instanceof ServerSelectionList.OnlineServerEntry
          || selected instanceof ServerSelectionList.NetworkServerEntry) {
        ((AccessorJoinMultiplayerScreen) this.joinMultiplayerScreen).getDeleteButton().active =
            !FavoritesList.contains(this.getKey(selected));
      }
    }
  }
}
