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

package com.illusivesoulworks.cherishedworlds.mixin;

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConfig;
import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import com.illusivesoulworks.cherishedworlds.client.favorites.AbstractFavoritesListWidget;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorNetworkServerEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.server.LanServer;
import net.minecraft.world.level.storage.LevelSummary;

public class CherishedWorldsMixinHooks {

  public static LevelSummary.BackupStatus getBackupStatus(LevelSummary levelSummary,
                                                          LevelSummary.BackupStatus original) {
    CherishedWorldsConfig.BackupType backupType =
        CherishedWorldsConfig.CLIENT.backupWorldType.get();

    if (original == LevelSummary.BackupStatus.NONE
        && backupType != CherishedWorldsConfig.BackupType.NONE) {

      if (backupType == CherishedWorldsConfig.BackupType.ALL
          || FavoritesList.contains(levelSummary.getLevelId())) {
        int levelVersion = levelSummary.levelVersion().minecraftVersion().version();
        int currentVersion = SharedConstants.getCurrentVersion().dataVersion().version();

        if (levelVersion < currentVersion) {
          return LevelSummary.BackupStatus.UPGRADE_TO_SNAPSHOT;
        }
      }
    }
    return original;
  }

  public static boolean isNotValidSwap(ServerList serverList, int pos1, int pos2) {
    int offset = 0;

    if (pos2 >= serverList.size()) {
      offset = pos2 - serverList.size() + 1;
    } else if (pos1 >= serverList.size()) {
      offset = pos1 - serverList.size() + 1;
    }

    try {
      ServerData data1 = serverList.get(pos1 - offset);
      ServerData data2 = serverList.get(pos2 - offset);
      boolean isFavored1 = FavoritesList.contains(data1.name + data1.ip);
      boolean isFavored2 = FavoritesList.contains(data2.name + data2.ip);
      return (isFavored1 && !isFavored2) || (!isFavored1 && isFavored2);
    } catch (IndexOutOfBoundsException e) {
      CherishedWorldsConstants.LOG.error("Error trying to swap servers!", e);
    }
    return false;
  }

  public static void renameFavorite(String prevName, String newName) {

    if (FavoritesList.contains(prevName)) {
      FavoritesList.remove(prevName);
      FavoritesList.add(newName);
    }
  }

  public static boolean editDeleteButton(ServerSelectionList.Entry entry, boolean flag) {

    if (entry instanceof ServerSelectionList.OnlineServerEntry) {
      ServerData data = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
      return !FavoritesList.contains(data.name + data.ip);
    }
    return flag;
  }

  public static void updateOnlineServers(ServerList servers,
                                         List<ServerSelectionList.OnlineServerEntry> onlineServers) {
    List<ServerSelectionList.OnlineServerEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.OnlineServerEntry> others = new ArrayList<>();

    for (ServerSelectionList.OnlineServerEntry onlineServer : onlineServers) {
      ServerData data = onlineServer.getServerData();

      if (FavoritesList.contains(data.name + data.ip)) {
        favorites.add(onlineServer);
      } else {
        others.add(onlineServer);
      }
    }
    onlineServers.clear();
    onlineServers.addAll(favorites);
    onlineServers.addAll(others);

    for (int i = 0; i < onlineServers.size(); i++) {
      servers.replace(i, onlineServers.get(i).getServerData());
    }
  }

  public static void updateNetworkServers(List<LanServer> servers,
                                          List<ServerSelectionList.NetworkServerEntry> onlineServers) {
    List<ServerSelectionList.NetworkServerEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.NetworkServerEntry> others = new ArrayList<>();

    for (ServerSelectionList.NetworkServerEntry onlineServer : onlineServers) {
      LanServer data = ((AccessorNetworkServerEntry) onlineServer).getServerData();

      if (FavoritesList.contains(data.getAddress())) {
        favorites.add(onlineServer);
      } else {
        others.add(onlineServer);
      }
    }
    onlineServers.clear();
    onlineServers.addAll(favorites);
    onlineServers.addAll(others);

    for (int i = 0; i < onlineServers.size(); i++) {
      servers.set(i, ((AccessorNetworkServerEntry) onlineServers.get(i)).getServerData());
    }
  }

  public static Comparator<WorldSelectionList.Entry> getLevelComparator() {
    return (o1, o2) -> {
      LevelSummary l1 = o1.getLevelSummary();
      LevelSummary l2 = o2.getLevelSummary();

      if (l1 != null && l2 != null) {
        boolean isFavorite1 = FavoritesList.contains(l1.getLevelId());
        boolean isFavorite2 = FavoritesList.contains(l2.getLevelId());

        if (isFavorite1 && !isFavorite2) {
          return -1;
        } else if (!isFavorite1 && isFavorite2) {
          return 1;
        }
        return l1.compareTo(l2);
      }
      return 0;
    };
  }

  public static Optional<GuiEventListener> getChildAt(ContainerEventHandler eventHandler,
                                                      Optional<GuiEventListener> original, double x,
                                                      double y) {

    if (eventHandler instanceof SelectWorldScreen
        || eventHandler instanceof JoinMultiplayerScreen) {

      for (GuiEventListener child : eventHandler.children()) {

        if (child instanceof AbstractFavoritesListWidget<?, ?> widget) {

          if (widget.isMouseOver(x, y)) {
            return Optional.of(child);
          } else {
            return original;
          }
        }
      }
    }
    return original;
  }

  public static boolean canDelete(LevelSummary levelSummary) {
    return !FavoritesList.contains(levelSummary.getLevelId());
  }
}
