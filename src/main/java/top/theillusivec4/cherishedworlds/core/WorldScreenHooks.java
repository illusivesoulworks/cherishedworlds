package top.theillusivec4.cherishedworlds.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;

public class WorldScreenHooks {

  private static final Identifier STAR_ICON = new Identifier(CherishedWorlds.MODID,
      "textures/gui/staricon.png");
  private static final Identifier EMPTY_STAR_ICON = new Identifier(CherishedWorlds.MODID,
      "textures/gui/emptystaricon.png");

  public static void init(SelectWorldScreen screen) {
    WorldListWidget selectionList = CherishedWorlds.getInstance().getAccessor()
        .getWorldList(screen);
    TextFieldWidget textField = CherishedWorlds.getInstance().getAccessor().getTextField(screen);

    if (selectionList != null) {
      FavoriteWorlds.loadFavoritesList();
      textField.setChangedListener((s) -> refreshList(selectionList, () -> s));
      refreshList(selectionList);
    }
  }

  public static void render(SelectWorldScreen screen, MatrixStack matrices, int mouseX,
      int mouseY) {
    Accessor accessor = CherishedWorlds.getInstance().getAccessor();
    WorldListWidget selectionList = accessor.getWorldList(screen);

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldListWidget.Entry entry = selectionList.children().get(i);

        if (entry != null) {
          LevelSummary summary = accessor.getWorldSummary(entry);

          if (summary != null) {
            boolean isFavorite = FavoriteWorlds.isFavorite(summary.getName());
            Identifier icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
            int top = (int) (accessor.getTop(selectionList) + 15 + 36 * i - selectionList
                .getScrollAmount());
            int x = screen.width / 2 - 148;

            if (top < (accessor.getBottom(selectionList) - 8) && top > accessor
                .getTop(selectionList)) {
              MinecraftClient.getInstance().getTextureManager().bindTexture(icon);
              DrawableHelper.drawTexture(matrices, x, top, 0, 0, 9, 9, 9, 9);
            }

            if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
              TranslatableText component = new TranslatableText(
                  "selectWorld." + CherishedWorlds.MODID + "." + (isFavorite ? "unfavorite"
                      : "favorite"));
              screen.renderTooltip(matrices, Collections.singletonList(component), mouseX, mouseY);
            }
          }
        }
      }
    }
  }

  public static void checkMouseClick(SelectWorldScreen screen, double mouseX, double mouseY) {
    Accessor accessor = CherishedWorlds.getInstance().getAccessor();
    WorldListWidget worldList = accessor.getWorldList(screen);

    if (worldList != null) {

      for (int i = 0; i < worldList.children().size(); i++) {
        WorldListWidget.Entry entry = worldList.children().get(i);

        if (entry != null) {
          LevelSummary summary = accessor.getWorldSummary(entry);

          if (summary != null) {
            boolean isFavorite = FavoriteWorlds.isFavorite(summary.getName());
            int top = (int) (accessor.getTop(worldList) + 15 + 36 * i - worldList
                .getScrollAmount());
            int x = screen.width / 2 - 148;

            if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
              String s = summary.getName();

              if (isFavorite) {
                FavoriteWorlds.removeFavorite(s);
              } else {
                FavoriteWorlds.addFavorite(s);
              }
              FavoriteWorlds.saveFavoritesList();
              refreshList(worldList);
              return;
            }
          }
        }
      }
    }
  }

  public static void disableDeleteButton(SelectWorldScreen screen) {
    WorldListWidget selectionList = CherishedWorlds.getInstance().getAccessor()
        .getWorldList(screen);

    if (selectionList != null) {
      WorldListWidget.Entry entry = selectionList.getSelected();

      if (entry != null) {
        ButtonWidget deleteButton = CherishedWorlds.getInstance().getAccessor()
            .getDeleteButton(screen);
        disableDeletingFavorites(entry, deleteButton);
      }
    }
  }

  private static void refreshList(WorldListWidget worldList) {
    refreshList(worldList, null);
  }

  private static void refreshList(WorldListWidget worldList, Supplier<String> supplier) {
    MinecraftClient mc = MinecraftClient.getInstance();
    LevelStorage saveformat = mc.getLevelStorage();
    List<LevelSummary> list;

    try {
      list = saveformat.getLevelList();
    } catch (LevelStorageException saveexception) {
      CherishedWorlds.LOGGER.error("Couldn't load level list", saveexception);
      mc.openScreen(new FatalErrorScreen(new TranslatableText("selectWorld.unable_to_load"),
          new LiteralText(saveexception.getMessage())));
      return;
    }

    List<WorldListWidget.Entry> entries = worldList.children();
    entries.clear();
    Iterator<LevelSummary> iter = list.listIterator();
    List<LevelSummary> favorites = new ArrayList<>();

    while (iter.hasNext()) {
      LevelSummary summ = iter.next();

      if (FavoriteWorlds.isFavorite(summ.getName())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(list);
    String s = supplier == null ? "" : supplier.get().toLowerCase(Locale.ROOT);

    for (LevelSummary worldsummary : favorites) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(worldList.new Entry(worldList, worldsummary, mc.getLevelStorage()));
      }
    }

    for (LevelSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(worldList.new Entry(worldList, worldsummary, mc.getLevelStorage()));
      }
    }

    WorldListWidget.Entry entry = worldList.getSelected();

    if (entry != null) {
      ButtonWidget deleteButton = CherishedWorlds.getInstance().getAccessor()
          .getDeleteButton(worldList.getParent());
      disableDeletingFavorites(entry, deleteButton);
    }
  }

  private static void disableDeletingFavorites(WorldListWidget.Entry entry,
      ButtonWidget deleteButton) {
    LevelSummary summary = CherishedWorlds.getInstance().getAccessor().getWorldSummary(entry);
    boolean isFavorite = summary != null && FavoriteWorlds.isFavorite(summary.getName());
    deleteButton.active = !isFavorite;
  }
}
