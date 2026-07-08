/*
 * Copyright (C) 2018-2026 Illusive Soulworks
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

import com.illusivesoulworks.cherishedworlds.client.favorites.impl.FavoriteCreateWorldWidget;
import com.illusivesoulworks.cherishedworlds.client.favorites.impl.FavoriteServersWidget;
import com.illusivesoulworks.cherishedworlds.client.favorites.impl.FavoriteWorldsWidget;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public class ScreenEventHooks {

  public static void onCreateNewWorld(String levelId) {
    FavoriteCreateWorldWidget.saveFavorite(levelId);
  }

  public static void addFavoritesWidget(Screen screen, Consumer<AbstractWidget> adder) {
    AbstractWidget widget = null;

    if (screen instanceof CreateWorldScreen createWorldScreen) {
      widget = new FavoriteCreateWorldWidget(createWorldScreen);
    } else if (screen instanceof JoinMultiplayerScreen joinMultiplayerScreen) {
      widget = new FavoriteServersWidget(joinMultiplayerScreen);
    } else if (screen instanceof SelectWorldScreen selectWorldScreen) {
      widget = new FavoriteWorldsWidget(selectWorldScreen);
    }

    if (widget != null) {
      adder.accept(widget);
    }
  }
}
