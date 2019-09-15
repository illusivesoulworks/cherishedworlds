/*
 * Copyright (C) 2018  C4
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

package c4.cherishedworlds.event;

import c4.cherishedworlds.CherishedWorlds;
import c4.cherishedworlds.core.FavoriteWorldsList;
import c4.cherishedworlds.core.ReflectionAccessor;
import com.google.common.collect.Lists;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EventHandlerClient {

    private static final ResourceLocation STAR_ICON = new ResourceLocation(CherishedWorlds.MODID, "textures/gui/staricon.png");

    @SubscribeEvent
    public void onGuiDrawScreen(GuiScreenEvent.DrawScreenEvent.Post evt) {
        GuiScreen gui = evt.getGui();

        if (gui instanceof GuiWorldSelection) {
            GuiWorldSelection worldSelect = (GuiWorldSelection)gui;

            if (FavoriteWorldsList.hasFavorite()) {
                GuiListWorldSelection selectionList = ReflectionAccessor.getSelectionList(worldSelect);

                if (selectionList != null) {
                    int size = ReflectionAccessor.getSize(selectionList);

                    for (int i = 0; i < size; i++) {
                        GuiListWorldSelectionEntry entry = selectionList.getListEntry(i);

                        if (entry != null) {
                            WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);

                            if (summary != null && FavoriteWorldsList.isFavorite(summary.getFileName())) {
                                int top = selectionList.top + 15 + selectionList.slotHeight * i - selectionList
                                        .getAmountScrolled();
                                int x = evt.getGui().width / 2 - 148;

                                if (top < (selectionList.bottom - 8) && top > selectionList.top) {
                                    Minecraft.getMinecraft().getTextureManager().bindTexture(STAR_ICON);
                                    Gui.drawModalRectWithCustomSizedTexture(x, top, 0, 0, 9, 9, 9, 9);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiButtonClick(GuiScreenEvent.ActionPerformedEvent evt) {
        GuiScreen gui = evt.getGui();

        if (gui instanceof GuiWorldSelection) {
            GuiWorldSelection worldSelect = (GuiWorldSelection)gui;

            if (evt.getButton().id == 6) {
                GuiListWorldSelection selectionList = ReflectionAccessor.getSelectionList(worldSelect);

                if (selectionList != null) {
                    GuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

                    if (entry != null) {
                        WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);

                        if (summary != null) {
                            FavoriteWorldsList.addFavorite(summary.getFileName());
                            FavoriteWorldsList.saveFavoritesList();
                        }
                    }
                }
            } else if (evt.getButton().id == 7) {
                GuiListWorldSelection selectionList = ReflectionAccessor.getSelectionList(worldSelect);

                if (selectionList != null) {
                    GuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

                    if (entry != null) {
                        WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);

                        if (summary != null) {
                            FavoriteWorldsList.removeFavorite(summary.getFileName());
                            FavoriteWorldsList.saveFavoritesList();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiMouseClick(GuiScreenEvent.MouseInputEvent.Post evt) {
        GuiScreen gui = evt.getGui();

        if (gui instanceof GuiWorldSelection) {
            GuiWorldSelection worldSelect = (GuiWorldSelection)gui;
            GuiListWorldSelection selectionList = ReflectionAccessor.getSelectionList(worldSelect);

            if (selectionList != null) {
                GuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

                if (entry != null) {
                    List<GuiButton> buttonList = ReflectionAccessor.getButtonList(gui);
                    WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);
                    boolean isFavored = summary != null && FavoriteWorldsList.isFavorite(summary.getFileName());

                    if (buttonList != null && !buttonList.isEmpty() && buttonList.size() >= 7) {

                        for (GuiButton button : buttonList) {

                            if (button.displayString.equals("Pin")) {
                                button.enabled = !isFavored;
                            } else if (button.displayString.equals("Unpin")) {
                                button.enabled = isFavored;
                            }
                        }

                        buttonList.get(3).enabled = !isFavored;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post evt) {
        GuiScreen gui = evt.getGui();

        if (gui instanceof GuiWorldSelection) {
            GuiWorldSelection worldSelect = (GuiWorldSelection)gui;
            List<GuiButton> buttonList = evt.getButtonList();
            int width = worldSelect.width;
            GuiButton bookmark = new GuiButton(6, width / 2 + 48, 8, 50, 20, "Pin");
            GuiButton bookmark2 = new GuiButton(7, width / 2 + 104, 8, 50, 20, "Unpin");
            bookmark.enabled = false;
            bookmark2.enabled = false;
            buttonList.add(bookmark);
            buttonList.add(bookmark2);
            GuiListWorldSelection selectionList = ReflectionAccessor.getSelectionList(worldSelect);

            if (selectionList != null) {
                FavoriteWorldsList.loadFavoritesList();
                refreshList(selectionList);
            }
        }
    }

    private static void refreshList(GuiListWorldSelection listWorldSelection) {
        Minecraft mc = Minecraft.getMinecraft();
        ISaveFormat isaveformat = mc.getSaveLoader();
        List<WorldSummary> list;

        try {
            list = isaveformat.getSaveList();
        } catch (AnvilConverterException anvilconverterexception) {
            CherishedWorlds.logger.error("Couldn't load level list", anvilconverterexception);
            mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"), anvilconverterexception.getMessage()));
            return;
        }

        List<GuiListWorldSelectionEntry> entries = ReflectionAccessor.getWorldSelectionEntries(listWorldSelection);

        if (entries != null) {
            entries.clear();
            Iterator<WorldSummary> iter = list.listIterator();
            List<WorldSummary> favorites = Lists.newArrayList();

            while (iter.hasNext()) {
                WorldSummary summ = iter.next();

                if (FavoriteWorldsList.isFavorite(summ.getFileName())) {
                    favorites.add(summ);
                    iter.remove();
                }
            }
            Collections.sort(favorites);
            Collections.sort(list);

            for (WorldSummary worldsummary : favorites) {
                entries.add(new GuiListWorldSelectionEntry(listWorldSelection, worldsummary, mc.getSaveLoader()));
            }

            for (WorldSummary worldsummary : list) {
                entries.add(new GuiListWorldSelectionEntry(listWorldSelection, worldsummary, mc.getSaveLoader()));
            }
        }
    }
}
