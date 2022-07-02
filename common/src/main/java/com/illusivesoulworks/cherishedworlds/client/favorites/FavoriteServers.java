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

package com.illusivesoulworks.cherishedworlds.client.favorites;

import com.illusivesoulworks.cherishedworlds.integration.ViewerIntegration;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorJoinMultiplayerScreen;
import com.illusivesoulworks.cherishedworlds.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;

public class FavoriteServers implements IFavoritesViewer<JoinMultiplayerScreen> {

  @Override
  public void init(JoinMultiplayerScreen screen) {
    AccessorJoinMultiplayerScreen accessor = (AccessorJoinMultiplayerScreen) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {
      selectionList.updateOnlineServers(screen.getServers());
    }
  }

  @Override
  public void draw(int mouseX, int mouseY, PoseStack poseStack, JoinMultiplayerScreen screen) {
    AccessorJoinMultiplayerScreen accessor = (AccessorJoinMultiplayerScreen) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof ServerSelectionList.OnlineServerEntry) {
          ServerData serverData = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
          int top = Services.PLATFORM.getTop(selectionList);
          int bottom = Services.PLATFORM.getBottom(selectionList);
          boolean isFavorite = FavoritesList.contains(serverData.name + serverData.ip);
          drawIcon(mouseX, mouseY, poseStack, screen, i, isFavorite, top,
              selectionList.getScrollAmount(), bottom);
        }
      }
    }
  }

  @Override
  public void click(int mouseX, int mouseY, JoinMultiplayerScreen screen) {
    AccessorJoinMultiplayerScreen accessor = (AccessorJoinMultiplayerScreen) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof ServerSelectionList.OnlineServerEntry) {
          ServerData serverData = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
          boolean isFavorite = FavoritesList.contains(serverData.name + serverData.ip);
          int topOffsetMod = 15;
          int height = 36;
          Pair<Integer, Integer> override = ViewerIntegration.getOverride(height);

          if (override != null) {
            topOffsetMod = override.getFirst();
            height = override.getSecond();
          }
          int top = (int) (Services.PLATFORM.getTop(selectionList) + topOffsetMod + height * i -
              selectionList.getScrollAmount());
          int x = screen.width / 2 - getHorizontalOffset();

          if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
            String s = serverData.name + serverData.ip;

            if (isFavorite) {
              FavoritesList.remove(s);
            } else {
              FavoritesList.add(s);
            }
            FavoritesList.save();
            accessor.getSelectionList().updateOnlineServers(screen.getServers());
            ServerSelectionList.Entry selected = accessor.getSelectionList().getSelected();

            if (selected instanceof ServerSelectionList.OnlineServerEntry) {
              disableDeletion((ServerSelectionList.OnlineServerEntry) selected,
                  accessor.getDeleteButton());
            }
            return;
          }
        }
      }
    }
  }

  @Override
  public void clicked(JoinMultiplayerScreen screen) {
    // NO-OP
  }

  @Override
  public int getHorizontalOffset() {
    return 168;
  }

  private static void disableDeletion(ServerSelectionList.OnlineServerEntry entry,
                                      Button deleteButton) {
    ServerData serverData = entry.getServerData();
    deleteButton.active = !FavoritesList.contains(serverData.name + serverData.ip);
  }
}
