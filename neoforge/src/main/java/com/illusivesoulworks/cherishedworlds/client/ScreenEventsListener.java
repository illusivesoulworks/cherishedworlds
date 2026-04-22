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

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ScreenEventsListener {

  @SubscribeEvent
  public void onGuiInit(ScreenEvent.Init.Post evt) {
    ScreenEventHooks.addFavoritesWidget(evt.getScreen(), evt::addListener);
  }
}
