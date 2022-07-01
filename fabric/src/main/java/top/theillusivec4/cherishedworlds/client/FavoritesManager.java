package top.theillusivec4.cherishedworlds.client;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Collections;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;
import top.theillusivec4.cherishedworlds.integration.CompactUiModule;

public interface FavoritesManager<T extends Screen> {

  Identifier STAR_ICON = new Identifier(CherishedWorldsMod.MOD_ID,
      "textures/gui/staricon.png");
  Identifier EMPTY_STAR_ICON = new Identifier(CherishedWorldsMod.MOD_ID,
      "textures/gui/emptystaricon.png");

  void init(T screen);

  void render(T screen, MatrixStack matrices, int mouseX, int mouseY);

  void click(T screen, double mouseX, double mouseY);

  void clicked(T screen);

  int getOffset();

  default void renderIcon(T screen, MatrixStack matrices, int mouseX, int mouseY,
                          double scrollAmount, int index, int topOffset, int bottomOffset,
                          boolean isFavorite) {
    Identifier icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
    int topOffsetMod = 15;
    int height = 36;

    if (CompactUiModule.isLoaded()) {
      Pair<Integer, Integer> result = CompactUiModule.getOffsets(height);
      topOffsetMod = result.getLeft();
      height = result.getRight();
    }
    int top = (int) (topOffset + topOffsetMod + height * index - scrollAmount);
    int x = screen.width / 2 - getOffset();

    if (top < (bottomOffset - 8) && top > topOffset) {
      RenderSystem.setShaderTexture(0, icon);
      DrawableHelper.drawTexture(matrices, x, top, 0, 0, 9, 9, 9, 9);
    }

    if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
      TranslatableText component = new TranslatableText(
          "selectWorld." + CherishedWorldsMod.MOD_ID + "." +
              (isFavorite ? "unfavorite" : "favorite"));
      screen.renderTooltip(matrices, Collections.singletonList(component), mouseX, mouseY);
    }
  }
}
