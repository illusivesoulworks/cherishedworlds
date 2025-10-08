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

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorServerSelectionListEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.world.level.storage.LevelSummary;

public class CherishedWorldsMixinHooks {

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

  public static void updateServers(ServerList serverList,
                                   List<ServerSelectionList.OnlineServerEntry> onlineServers,
                                   ServerSelectionList serverSelectionList,
                                   JoinMultiplayerScreen screen) {
    onlineServers.clear();
    List<ServerSelectionList.OnlineServerEntry> favorites = new ArrayList<>();
    List<ServerSelectionList.OnlineServerEntry> others = new ArrayList<>();

    for (int i = 0; i < serverList.size(); ++i) {
      ServerData data = serverList.get(i);
      ServerSelectionList.OnlineServerEntry entry =
          AccessorServerSelectionListEntry.cherishedworlds$createEntry(serverSelectionList, screen,
                                                                       data);

      if (FavoritesList.contains(data.name + data.ip)) {
        favorites.add(entry);
      } else {
        others.add(entry);
      }
    }
    onlineServers.addAll(favorites);
    onlineServers.addAll(others);

    for (int i = 0; i < onlineServers.size(); i++) {
      serverList.replace(i, onlineServers.get(i).getServerData());
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

  public static boolean canDelete(LevelSummary levelSummary) {
    return !FavoritesList.contains(levelSummary.getLevelId());
  }
}
