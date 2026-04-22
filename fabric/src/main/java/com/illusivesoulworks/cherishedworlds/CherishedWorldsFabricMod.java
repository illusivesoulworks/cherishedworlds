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

package com.illusivesoulworks.cherishedworlds;

import com.illusivesoulworks.cherishedworlds.client.ScreenEventHooks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

public class CherishedWorldsFabricMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    CherishedWorldsCommonMod.setup();
    ScreenEvents.AFTER_INIT.register(
        (client, screen, scaledWidth, scaledHeight) ->
            ScreenEventHooks.addFavoritesWidget(screen,
                                                widget -> Screens.getWidgets(screen).add(widget)));
  }
}
