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

package com.illusivesoulworks.cherishedworlds;

import com.illusivesoulworks.cherishedworlds.client.ScreenEventsListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CherishedWorldsConstants.MOD_ID)
public class CherishedWorldsNeoForgeMod {

  public CherishedWorldsNeoForgeMod(IEventBus eventBus) {
    eventBus.addListener(this::setupClient);
    CherishedWorldsCommonMod.setupConfig();
  }

  private void setupClient(final FMLClientSetupEvent evt) {
    CherishedWorldsCommonMod.setup();
    NeoForge.EVENT_BUS.register(new ScreenEventsListener());
  }
}