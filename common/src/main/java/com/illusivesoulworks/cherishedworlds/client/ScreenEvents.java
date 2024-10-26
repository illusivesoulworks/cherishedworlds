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

package com.illusivesoulworks.cherishedworlds.client;

import com.illusivesoulworks.cherishedworlds.client.favorites.FavoriteCreateWorld;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoriteServers;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoriteWorlds;
import com.illusivesoulworks.cherishedworlds.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public class ScreenEvents {

  private static final FavoriteWorlds WORLDS = new FavoriteWorlds();
  private static final FavoriteServers SERVERS = new FavoriteServers();
  private static final FavoriteCreateWorld CREATE_WORLD = new FavoriteCreateWorld();

  public static void onCreateNewWorld(String levelId) {
    CREATE_WORLD.saveFavorite(levelId);
  }

  public static void onDraw(int mouseX, int mouseY, GuiGraphics guiGraphics, Screen screen) {

    if (Services.PLATFORM.canRender()) {

      if (screen instanceof SelectWorldScreen) {
        WORLDS.draw(mouseX, mouseY, guiGraphics, (SelectWorldScreen) screen);
      } else if (screen instanceof JoinMultiplayerScreen) {
        SERVERS.draw(mouseX, mouseY, guiGraphics, (JoinMultiplayerScreen) screen);
      } else if (screen instanceof CreateWorldScreen) {
        CREATE_WORLD.draw(mouseX, mouseY, guiGraphics, (CreateWorldScreen) screen);
      }
    }
  }

  public static void onMouseClick(int mouseX, int mouseY, Screen screen) {

    if (Services.PLATFORM.canInteract()) {

      if (screen instanceof SelectWorldScreen) {
        WORLDS.click(mouseX, mouseY, (SelectWorldScreen) screen);
      } else if (screen instanceof JoinMultiplayerScreen) {
        SERVERS.click(mouseX, mouseY, (JoinMultiplayerScreen) screen);
      } else if (screen instanceof CreateWorldScreen) {
        CREATE_WORLD.click(mouseX, mouseY, (CreateWorldScreen) screen);
      }
    }
  }

  public static void onMouseClicked(Screen screen) {

    if (Services.PLATFORM.canInteract()) {

      if (screen instanceof SelectWorldScreen) {
        WORLDS.clicked((SelectWorldScreen) screen);
      } else if (screen instanceof JoinMultiplayerScreen) {
        SERVERS.clicked((JoinMultiplayerScreen) screen);
      }
    }
  }

  public static void onInit(Screen screen) {

    if (screen instanceof SelectWorldScreen) {
      WORLDS.init((SelectWorldScreen) screen);
    } else if (screen instanceof JoinMultiplayerScreen) {
      SERVERS.init((JoinMultiplayerScreen) screen);
    }
  }
}
