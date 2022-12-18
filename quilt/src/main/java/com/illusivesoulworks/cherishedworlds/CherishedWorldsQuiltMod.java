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
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class CherishedWorldsQuiltMod implements ClientModInitializer {

  @Override
  public void onInitializeClient(ModContainer modContainer) {
    CherishedWorldsCommonMod.setup();
    ViewerIntegration.register("compact-ui", (height) -> {
      int newHeight = (height - 4) / 3 + 4;
      int newTopOffset = newHeight / 2 - 3;
      return new Pair<>(newTopOffset, newHeight);
    });
  }
}
