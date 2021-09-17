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

package top.theillusivec4.cherishedworlds.client;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.cherishedworlds.client.favorites.FavoriteServers;
import top.theillusivec4.cherishedworlds.client.favorites.FavoriteWorlds;

@SuppressWarnings("unused")
public class ScreenEventsListener {

  private static final FavoriteWorlds WORLDS = new FavoriteWorlds();
  private static final FavoriteServers SERVERS = new FavoriteServers();

  @SubscribeEvent
  public void onGuiDrawScreen(GuiScreenEvent.DrawScreenEvent.Post evt) {
    Screen screen = evt.getGui();

    if (screen instanceof SelectWorldScreen) {
      WORLDS.draw(evt, (SelectWorldScreen) screen);
    } else if (screen instanceof JoinMultiplayerScreen) {
      SERVERS.draw(evt, (JoinMultiplayerScreen) screen);
    }
  }

  @SubscribeEvent
  public void onGuiMouseClick(GuiScreenEvent.MouseClickedEvent.Pre evt) {
    Screen screen = evt.getGui();

    if (screen instanceof SelectWorldScreen) {
      WORLDS.click(evt, (SelectWorldScreen) screen);
    } else if (screen instanceof JoinMultiplayerScreen) {
      SERVERS.click(evt, (JoinMultiplayerScreen) screen);
    }
  }

  @SubscribeEvent
  public void onGuiMouseClicked(GuiScreenEvent.MouseClickedEvent.Post evt) {
    Screen screen = evt.getGui();

    if (screen instanceof SelectWorldScreen) {
      WORLDS.clicked((SelectWorldScreen) screen);
    } else if (screen instanceof JoinMultiplayerScreen) {
      SERVERS.clicked((JoinMultiplayerScreen) screen);
    }
  }

  @SubscribeEvent
  public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post evt) {
    Screen screen = evt.getGui();

    if (screen instanceof SelectWorldScreen) {
      WORLDS.init((SelectWorldScreen) screen);
    } else if (screen instanceof JoinMultiplayerScreen) {
      SERVERS.init((JoinMultiplayerScreen) screen);
    }
  }
}
