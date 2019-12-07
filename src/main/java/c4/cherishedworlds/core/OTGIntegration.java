package c4.cherishedworlds.core;

import c4.cherishedworlds.CherishedWorlds;
import c4.cherishedworlds.event.EventHandlerClient;
import com.google.common.collect.Lists;
import com.pg85.otg.forge.gui.mainmenu.OTGGuiListWorldSelection;
import com.pg85.otg.forge.gui.mainmenu.OTGGuiListWorldSelectionEntry;
import com.pg85.otg.forge.gui.mainmenu.OTGGuiWorldSelection;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class OTGIntegration {

  public static void drawOtg(GuiScreenEvent.DrawScreenEvent.Post evt) {
    GuiScreen gui = evt.getGui();

    if (gui instanceof OTGGuiWorldSelection) {
      OTGGuiWorldSelection worldSelect = (OTGGuiWorldSelection)gui;

      if (FavoriteWorldsList.hasFavorite()) {
        OTGGuiListWorldSelection selectionList = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "selectionList");

        if (selectionList != null) {
          List<OTGGuiListWorldSelectionEntry> entries = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelection.class, selectionList, "entries");

          for (int i = 0; i < entries.size(); i++) {
            OTGGuiListWorldSelectionEntry entry = selectionList.getListEntry(i);

            if (entry != null) {
              WorldSummary summary = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelectionEntry.class, entry, "worldSummary");

              if (summary != null && FavoriteWorldsList.isFavorite(summary.getFileName())) {
                int top = selectionList.top + 15 + selectionList.slotHeight * i - selectionList
                    .getAmountScrolled();
                int x = evt.getGui().width / 2 - 148;

                if (top < (selectionList.bottom - 8) && top > selectionList.top) {
                  Minecraft.getMinecraft().getTextureManager().bindTexture(EventHandlerClient.STAR_ICON);
                  Gui.drawModalRectWithCustomSizedTexture(x, top, 0, 0, 9, 9, 9, 9);
                }
              }
            }
          }
        }
      }
    }
  }

  public static void buttonClickOtg(GuiScreenEvent.ActionPerformedEvent evt) {
    GuiScreen gui = evt.getGui();

    if (gui instanceof OTGGuiWorldSelection) {
      OTGGuiWorldSelection worldSelect = (OTGGuiWorldSelection)gui;

      if (evt.getButton().id == 6) {
        OTGGuiListWorldSelection selectionList = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "selectionList");

        if (selectionList != null) {
          OTGGuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

          if (entry != null) {
            WorldSummary summary = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelectionEntry.class, entry, "worldSummary");

            if (summary != null) {
              FavoriteWorldsList.addFavorite(summary.getFileName());
              FavoriteWorldsList.saveFavoritesList();
            }
          }
        }
      } else if (evt.getButton().id == 7) {
        OTGGuiListWorldSelection selectionList = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "selectionList");

        if (selectionList != null) {
          OTGGuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

          if (entry != null) {
            WorldSummary summary = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelectionEntry.class, entry, "worldSummary");

            if (summary != null) {
              FavoriteWorldsList.removeFavorite(summary.getFileName());
              FavoriteWorldsList.saveFavoritesList();
            }
          }
        }
      }
    }
  }

  public static void mouseClickOtg(GuiScreenEvent.MouseInputEvent.Post evt) {
    GuiScreen gui = evt.getGui();

    if (gui instanceof OTGGuiWorldSelection) {
      OTGGuiWorldSelection worldSelect = (OTGGuiWorldSelection)gui;
      OTGGuiListWorldSelection selectionList = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "selectionList");

      if (selectionList != null) {
        OTGGuiListWorldSelectionEntry entry = selectionList.getSelectedWorld();

        if (entry != null) {
          List<GuiButton> buttonList = ReflectionAccessor.getButtonList(gui);
          WorldSummary summary = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelectionEntry.class, entry, "worldSummary");
          boolean isFavored = summary != null && FavoriteWorldsList.isFavorite(summary.getFileName());

          if (buttonList != null && !buttonList.isEmpty() && buttonList.size() >= 7) {

            for (GuiButton button : buttonList) {

              if (button.displayString.equals("Pin")) {
                button.enabled = !isFavored;
              } else if (button.displayString.equals("Unpin")) {
                button.enabled = isFavored;
              }
            }

            GuiButton deleteButton = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "deleteButton");
            deleteButton.enabled = !isFavored;
          }
        }
      }
    }
  }

  public static void initOtg(GuiScreenEvent.InitGuiEvent.Post evt) {
    GuiScreen gui = evt.getGui();

    if (gui instanceof OTGGuiWorldSelection) {
      OTGGuiWorldSelection worldSelect = (OTGGuiWorldSelection)gui;
      List<GuiButton> buttonList = evt.getButtonList();
      int width = worldSelect.width;
      GuiButton bookmark = new GuiButton(6, width / 2 + 48, 8, 50, 20, "Pin");
      GuiButton bookmark2 = new GuiButton(7, width / 2 + 104, 8, 50, 20, "Unpin");
      bookmark.enabled = false;
      bookmark2.enabled = false;
      buttonList.add(bookmark);
      buttonList.add(bookmark2);
      OTGGuiListWorldSelection selectionList = ObfuscationReflectionHelper.getPrivateValue(OTGGuiWorldSelection.class, worldSelect, "selectionList");

      if (selectionList != null) {
        FavoriteWorldsList.loadFavoritesList();
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

        List<OTGGuiListWorldSelectionEntry> entries = ObfuscationReflectionHelper.getPrivateValue(OTGGuiListWorldSelection.class, selectionList, "entries");

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

          try {
            Constructor<OTGGuiListWorldSelectionEntry> constructor = OTGGuiListWorldSelectionEntry.class
                .getDeclaredConstructor(OTGGuiListWorldSelection.class, WorldSummary.class, ISaveFormat.class);
            constructor.setAccessible(true);

            for (WorldSummary worldsummary : favorites) {
              entries.add(constructor.newInstance(selectionList, worldsummary, mc.getSaveLoader()));
            }

            for (WorldSummary worldsummary : list) {
              entries.add(constructor.newInstance(selectionList, worldsummary, mc.getSaveLoader()));
            }
          } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            CherishedWorlds.logger.error("Error in OTG-Gui compatibility!");
          }
        }
      }
    }
  }
}
