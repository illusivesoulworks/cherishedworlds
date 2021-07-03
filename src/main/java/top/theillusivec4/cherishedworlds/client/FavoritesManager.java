package top.theillusivec4.cherishedworlds.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;

public interface FavoritesManager<T extends Screen> {

  Identifier STAR_ICON = new Identifier(CherishedWorldsMod.MOD_ID,
      "textures/gui/staricon.png");
  Identifier EMPTY_STAR_ICON = new Identifier(CherishedWorldsMod.MOD_ID,
      "textures/gui/emptystaricon.png");

  void init(T screen);

  void render(T screen, MatrixStack matrices, int mouseX, int mouseY);

  void click(T screen, double mouseX, double mouseY);

  void clicked(T screen);
}
