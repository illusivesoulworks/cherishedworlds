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

package com.illusivesoulworks.cherishedworlds.integration;

import com.illusivesoulworks.cherishedworlds.platform.Services;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ViewerIntegration {

  private static final Map<String, Boolean> LOADED = new ConcurrentHashMap<>();
  private static Function<Integer, Pair<Integer, Integer>> activeOverride = null;

  private static boolean isModLoaded(String modId) {
    return LOADED.computeIfAbsent(modId, (k) -> Services.PLATFORM.isModLoaded(modId));
  }

  public static void register(String modId, Function<Integer, Pair<Integer, Integer>> override) {

    if (activeOverride == null && isModLoaded(modId)) {
      activeOverride = override;
    }
  }

  public static Pair<Integer, Integer> getOverride(int height) {

    if (activeOverride != null) {
      return activeOverride.apply(height);
    }
    return null;
  }
}
