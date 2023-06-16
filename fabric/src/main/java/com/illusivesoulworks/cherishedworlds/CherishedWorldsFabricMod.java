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

import com.illusivesoulworks.cherishedworlds.integration.ViewerIntegration;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public class CherishedWorldsFabricMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    CherishedWorldsCommonMod.setup();
    ScreenEvents.AFTER_INIT.register(
        (client, screen, scaledWidth, scaledHeight) -> {

          if (screen instanceof JoinMultiplayerScreen || screen instanceof SelectWorldScreen) {
            com.illusivesoulworks.cherishedworlds.client.ScreenEvents.onInit(screen);
            ScreenEvents.afterRender(screen).register(
                (screen1, drawContext, mouseX, mouseY, tickDelta) -> com.illusivesoulworks.cherishedworlds.client.ScreenEvents.onDraw(
                    mouseX, mouseY, drawContext, screen1));
            ScreenMouseEvents.afterMouseClick(screen).register(
                (screen1, mouseX, mouseY, button) -> com.illusivesoulworks.cherishedworlds.client.ScreenEvents.onMouseClick(
                    (int) mouseX, (int) mouseY, screen1));
            ScreenMouseEvents.afterMouseRelease(screen).register(
                (screen1, mouseX, mouseY, button) -> com.illusivesoulworks.cherishedworlds.client.ScreenEvents.onMouseClicked(
                    screen1));
          }
        });

    ViewerIntegration.register("compact-ui", (height) -> {
      int newHeight = (height - 4) / 3 + 4;
      int newTopOffset = newHeight / 2 - 3;
      return new Pair<>(newTopOffset, newHeight);
    });
  }
}
