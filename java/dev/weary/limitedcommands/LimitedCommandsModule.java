package dev.weary.limitedcommands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.weary.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LimitedCommandsModule extends Module {
    static LimitedCommandsModule instance;
    static ProtocolManager protocolManager;

    public LimitedCommandsModule() {
        super("LimitedCommands", "Disallows players without specified permissions to run and see any commands", "commands.yml");
        protocolManager = ProtocolLibrary.getProtocolManager();
        instance = this;
    }

    public static boolean isCommandHidden(String baseCommand) {
        return !LimitedCommandsSettings.LIMITED_COMMANDS_EXCEPTIONS.asStringList().contains(baseCommand);
    }

    public static String getBaseCommandPart(String fullCommandText) {
        int firstSpace = fullCommandText.indexOf(" ");
        if (firstSpace == -1) {
            return fullCommandText;
        }

        return fullCommandText.substring(0, firstSpace);
    }

    @Override
    protected boolean loadModule(JavaPlugin plugin) {
        this.initializeDefaultConfig(LimitedCommandsSettings.values());
        protocolManager.addPacketListener(new TabCompleteClientAdapter(plugin));
        plugin.getServer().getPluginManager().registerEvents(new CommandListHandler(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CommandPreprocessHandler(), plugin);
        return true;
    }

    @Override
    protected void onConfigLoaded() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.updateCommands();
        }
    }

    @Override
    protected void unloadModule() {}
}
