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
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionList;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionListEntry;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionScreen;
import com.illusivesoulworks.cherishedworlds.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;

public class FavoriteWorlds implements IFavoritesViewer<SelectWorldScreen> {

  @Override
  public void init(SelectWorldScreen screen) {
    AccessorWorldSelectionScreen accessor = (AccessorWorldSelectionScreen) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {
      EditBox textField = accessor.getSearchBox();

      if (textField != null) {
        selectionList.updateFilter(textField.getValue());
      }
    }
  }

  @Override
  public void draw(int mouseX, int mouseY, PoseStack poseStack, SelectWorldScreen screen) {
    AccessorWorldSelectionScreen accessor = (AccessorWorldSelectionScreen) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof WorldSelectionList.WorldListEntry) {
          @SuppressWarnings("ConstantConditions") AccessorWorldSelectionListEntry entryAccessor =
              (AccessorWorldSelectionListEntry) entry;
          LevelSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            int top = Services.PLATFORM.getTop(selectionList);
            int bottom = Services.PLATFORM.getBottom(selectionList);
            boolean isFavorite = FavoritesList.contains(summary.getLevelId());
            drawIcon(mouseX, mouseY, poseStack, screen, i, isFavorite, top,
                selectionList.getScrollAmount(), bottom);
          }
        }
      }
    }
  }

  @Override
  public void click(int mouseX, int mouseY, SelectWorldScreen screen) {
    AccessorWorldSelectionScreen accessor = (AccessorWorldSelectionScreen) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof WorldSelectionList.WorldListEntry) {
          @SuppressWarnings("ConstantConditions") AccessorWorldSelectionListEntry entryAccessor =
              (AccessorWorldSelectionListEntry) entry;
          LevelSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getLevelId());
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
              String s = summary.getLevelId();

              if (isFavorite) {
                FavoritesList.remove(s);
              } else {
                FavoritesList.add(s);
              }
              FavoritesList.save();
              EditBox textField = accessor.getSearchBox();
              String filter = "";

              if (textField != null) {
                filter = textField.getValue();
              }
              AccessorWorldSelectionList accessorWorldSelectionList =
                  (AccessorWorldSelectionList) selectionList;
              List<LevelSummary> levelSummaries =
                  accessorWorldSelectionList.getCurrentlyDisplayedLevels();

              if (levelSummaries != null) {
                accessorWorldSelectionList.callFillLevels(filter, levelSummaries);
              }
              return;
            }
          }
        }
      }
    }
  }

  @Override
  public void clicked(SelectWorldScreen screen) {
    AccessorWorldSelectionScreen accessor = (AccessorWorldSelectionScreen) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {
      WorldSelectionList.Entry entry = selectionList.getSelected();

      if (entry instanceof WorldSelectionList.WorldListEntry) {
        @SuppressWarnings("ConstantConditions") AccessorWorldSelectionListEntry entryAccessor =
            (AccessorWorldSelectionListEntry) entry;
        LevelSummary summary = entryAccessor.getWorldSummary();
        Button deleteButton = accessor.getDeleteButton();

        if (deleteButton != null && summary != null) {
          deleteButton.active = !FavoritesList.contains(summary.getLevelId());
        }
      }
    }
  }

  @Override
  public int getHorizontalOffset() {
    return 148;
  }
}
