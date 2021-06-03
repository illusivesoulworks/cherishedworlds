/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cherishedworlds;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.cherishedworlds.event.GuiEventsListener;

@Mod(CherishedWorldsMod.MODID)
public class CherishedWorldsMod {

  public static final String MODID = "cherishedworlds";
  public static final Logger LOGGER = LogManager.getLogger();

  public CherishedWorldsMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
        () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
  }

  private void setupClient(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new GuiEventsListener());
  }
}
