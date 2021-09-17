package top.theillusivec4.cherishedworlds.client.favorites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ErrorScreen;
import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;
import top.theillusivec4.cherishedworlds.mixin.core.WorldSelectionListEntryAccessor;
import top.theillusivec4.cherishedworlds.mixin.core.WorldSelectionScreenAccessor;

public class FavoriteWorlds implements IFavoritesManager<WorldSelectionScreen> {

  @Override
  public void init(WorldSelectionScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {
      TextFieldWidget textField = accessor.getSearchField();

      if (textField != null) {
        textField.setResponder((s) -> refreshList(selectionList, () -> s));
      }
      refreshList(selectionList);
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void draw(GuiScreenEvent.DrawScreenEvent.Post evt, WorldSelectionScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
        WorldSelectionList.Entry entry = selectionList.getEventListeners().get(i);

        if (entry != null) {
          WorldSelectionListEntryAccessor entryAccessor =
              (WorldSelectionListEntryAccessor) (Object) entry;
          WorldSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getFileName());
            drawIcon(evt, screen, i, isFavorite, selectionList.getTop(),
                selectionList.getScrollAmount(), selectionList.getBottom());
          }
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void click(GuiScreenEvent.MouseClickedEvent.Pre evt, WorldSelectionScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
        WorldSelectionList.Entry entry = selectionList.getEventListeners().get(i);

        if (entry != null) {
          WorldSelectionListEntryAccessor entryAccessor =
              (WorldSelectionListEntryAccessor) (Object) entry;
          WorldSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getFileName());
            int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                .getScrollAmount());
            int x = evt.getGui().width / 2 - getOffset();
            double mouseX = evt.getMouseX();
            double mouseY = evt.getMouseY();

            if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
              String s = summary.getFileName();

              if (isFavorite) {
                FavoritesList.remove(s);
              } else {
                FavoritesList.add(s);
              }
              FavoritesList.save();
              refreshList(selectionList);
              return;
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void clicked(WorldSelectionScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {
      WorldSelectionList.Entry entry = selectionList.getSelected();

      if (entry != null) {
        WorldSelectionListEntryAccessor entryAccessor =
            (WorldSelectionListEntryAccessor) (Object) entry;
        WorldSummary summary = entryAccessor.getWorldSummary();
        Button deleteButton = accessor.getDeleteButton();

        if (deleteButton != null && summary != null) {
          disableDeletion(summary, deleteButton);
        }
      }
    }
  }

  @Override
  public int getOffset() {
    return 148;
  }

  private static void refreshList(WorldSelectionList listWorldSelection) {
    refreshList(listWorldSelection, null);
  }

  @SuppressWarnings("ConstantConditions")
  private static void refreshList(WorldSelectionList listWorldSelection,
                                  Supplier<String> supplier) {
    Minecraft mc = Minecraft.getInstance();
    SaveFormat saveformat = mc.getSaveLoader();
    List<WorldSummary> list;

    try {
      list = saveformat.getSaveList();
    } catch (AnvilConverterException anvilconverterexception) {
      CherishedWorldsMod.LOGGER.error("Couldn't load level list", anvilconverterexception);
      mc.displayGuiScreen(
          new ErrorScreen(new TranslationTextComponent("selectWorld.unable_to_load"),
              new StringTextComponent(anvilconverterexception.getMessage())));
      return;
    }
    List<WorldSelectionList.Entry> entries = listWorldSelection.getEventListeners();
    entries.clear();
    Iterator<WorldSummary> iter = list.listIterator();
    List<WorldSummary> favorites = new ArrayList<>();

    while (iter.hasNext()) {
      WorldSummary summ = iter.next();

      if (FavoritesList.contains(summ.getFileName())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(list);
    String s = supplier == null ? "" : supplier.get().toLowerCase(Locale.ROOT);

    for (WorldSummary worldsummary : favorites) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getFileName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(listWorldSelection.new Entry(listWorldSelection, worldsummary));
      }
    }

    for (WorldSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getFileName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(listWorldSelection.new Entry(listWorldSelection, worldsummary));
      }
    }
    WorldSelectionList.Entry entry = listWorldSelection.getSelected();

    if (entry != null) {
      WorldSelectionListEntryAccessor entryAccessor =
          (WorldSelectionListEntryAccessor) (Object) entry;
      WorldSummary summary = entryAccessor.getWorldSummary();
      Button deleteButton =
          ((WorldSelectionScreenAccessor) listWorldSelection.getGuiWorldSelection())
              .getDeleteButton();

      if (deleteButton != null && summary != null) {
        disableDeletion(summary, deleteButton);
      }
    }
  }

  private static void disableDeletion(@Nonnull WorldSummary summary, Button deleteButton) {
    deleteButton.active = !FavoritesList.contains(summary.getFileName());
  }
}
