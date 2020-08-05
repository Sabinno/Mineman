package dev.weary.mineman;

import dev.weary.module.Module;
import org.bukkit.plugin.java.JavaPlugin;

public class MinemanModule extends Module {


    public MinemanModule() {
        super("Mineman", "Adds a shortcut to list all modules and reload configuration", "mineman.yml");
    }

    @Override
    protected boolean loadModule(JavaPlugin plugin) {
        plugin.getCommand("mineman").setExecutor(new MinemanCommandHandler());
        return true;
    }

    @Override
    protected void unloadModule() {}
}
