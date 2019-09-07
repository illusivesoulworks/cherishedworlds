/*
 * Copyright (C) 2018-2019  C4
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

package top.theillusivec4.cherishedworlds.util;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectionAccessor {

  @Nullable
  public static WorldSelectionList getSelectionList(WorldSelectionScreen worldSelection) {
    return ObfuscationReflectionHelper
        .getPrivateValue(WorldSelectionScreen.class, worldSelection, "field_184866_u");
  }

  @Nullable
  public static WorldSummary getWorldSummary(WorldSelectionList.Entry entry) {
    return ObfuscationReflectionHelper
        .getPrivateValue(WorldSelectionList.Entry.class, entry, "field_214451_d");
  }

  public static Button getDeleteButton(WorldSelectionScreen worldSelection) {
    return ObfuscationReflectionHelper.getPrivateValue(WorldSelectionScreen.class, worldSelection, "field_146642_y");
  }

  public static TextFieldWidget getTextField(WorldSelectionScreen worldSelection) {
    return ObfuscationReflectionHelper
        .getPrivateValue(WorldSelectionScreen.class, worldSelection, "field_212352_g");
  }
}
