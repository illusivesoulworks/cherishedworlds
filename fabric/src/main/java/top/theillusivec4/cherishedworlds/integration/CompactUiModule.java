package top.theillusivec4.cherishedworlds.integration;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Pair;

public class CompactUiModule {

  private static Boolean isLoaded;

  public static Pair<Integer, Integer> getOffsets(int height) {
    int newHeight = (height - 4) / 3 + 4;
    int newTopOffset = newHeight / 2 - 3;
    return new Pair<>(newTopOffset, newHeight);
  }

  public static boolean isLoaded() {

    if (isLoaded == null) {
      isLoaded = FabricLoader.getInstance().isModLoaded("compact-ui");
    }
    return isLoaded;
  }
}
