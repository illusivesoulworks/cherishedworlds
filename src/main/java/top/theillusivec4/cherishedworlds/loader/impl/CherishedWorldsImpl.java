package top.theillusivec4.cherishedworlds.loader.impl;

import java.io.File;
import net.fabricmc.loader.api.FabricLoader;
import top.theillusivec4.cherishedworlds.core.Accessor;
import top.theillusivec4.cherishedworlds.core.CherishedWorlds;

public class CherishedWorldsImpl implements CherishedWorlds {

  public static CherishedWorlds INSTANCE = new CherishedWorldsImpl();

  private final Accessor accessor = new AccessorImpl();

  @Override
  public Accessor getAccessor() {
    return accessor;
  }

  @Override
  public File getConfigDir() {
    return FabricLoader.getInstance().getConfigDirectory();
  }
}
