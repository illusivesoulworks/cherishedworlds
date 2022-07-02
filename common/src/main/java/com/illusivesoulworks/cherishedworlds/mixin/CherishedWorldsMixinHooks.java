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

import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorServerSelectionListEntry;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionListEntry;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionScreen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.world.level.storage.LevelSummary;

public class CherishedWorldsMixinHooks {

  public static boolean isNotValidSwap(ServerList serverList, int pos1, int pos2) {
    ServerData data1 = serverList.get(pos1);
    ServerData data2 = serverList.get(pos2);
    boolean isFavored1 = FavoritesList.contains(data1.name + data1.ip);
    boolean isFavored2 = FavoritesList.contains(data2.name + data2.ip);
    return (isFavored1 && !isFavored2) || (!isFavored1 && isFavored2);
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

  public static void fillLevels(String filter, List<LevelSummary> levels,
                                WorldSelectionList selectionList) {
    List<WorldSelectionList.Entry> entries = selectionList.children();
    entries.clear();
    filter = filter.toLowerCase(Locale.ROOT);
    List<LevelSummary> copy = new ArrayList<>(levels);
    Iterator<LevelSummary> iter = copy.listIterator();
    List<LevelSummary> favorites = new ArrayList<>();

    while (iter.hasNext()) {
      LevelSummary summ = iter.next();

      if (FavoritesList.contains(summ.getLevelId())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(copy);

    for (LevelSummary level : favorites) {

      if (filterAccepts(filter, level)) {
        entries.add(selectionList.new WorldListEntry(selectionList, level));
      }
    }

    for (LevelSummary level : copy) {

      if (filterAccepts(filter, level)) {
        entries.add(selectionList.new WorldListEntry(selectionList, level));
      }
    }
    WorldSelectionList.Entry entry = selectionList.getSelected();

    if (entry instanceof WorldSelectionList.WorldListEntry) {
      @SuppressWarnings("ConstantConditions") AccessorWorldSelectionListEntry entryAccessor =
          (AccessorWorldSelectionListEntry) entry;
      LevelSummary summary = entryAccessor.getWorldSummary();
      Button deleteButton =
          ((AccessorWorldSelectionScreen) selectionList.getScreen()).getDeleteButton();

      if (deleteButton != null && summary != null) {
        deleteButton.active = !FavoritesList.contains(summary.getLevelId());
      }
    }
  }

  private static boolean filterAccepts(String filter, LevelSummary level) {
    return level.getLevelName().toLowerCase(Locale.ROOT).contains(filter) ||
        level.getLevelId().toLowerCase(Locale.ROOT).contains(filter);
  }
}
