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

import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ScreenEventsListener {

  @SubscribeEvent
  public void onGuiDrawScreen(ScreenEvent.Render.Post evt) {
    ScreenEvents.onDraw(evt.getMouseX(), evt.getMouseY(), evt.getGuiGraphics(), evt.getScreen());
  }

  @SubscribeEvent
  public void onGuiMouseClick(ScreenEvent.MouseButtonReleased.Pre evt) {
    ScreenEvents.onMouseClick((int) evt.getMouseX(), (int) evt.getMouseY(), evt.getScreen());
  }

  @SubscribeEvent
  public void onGuiMouseClicked(ScreenEvent.MouseButtonReleased.Post evt) {
    ScreenEvents.onMouseClicked(evt.getScreen());
  }

  @SubscribeEvent
  public void onGuiInit(ScreenEvent.Init.Post evt) {
    ScreenEvents.onInit(evt.getScreen());
  }
}
