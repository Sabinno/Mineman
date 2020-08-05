package dev.weary.mineman;

import dev.weary.module.Module;
import dev.weary.module.ModuleStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinemanCommandHandler implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("reload", "modules");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) {
            return SUBCOMMANDS;
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Available subcommands:");
            sender.sendMessage("  /mineman modules - List enabled modules");
            sender.sendMessage("  /mineman reload  - Reload all configs");
            return true;
        }

        else if (args.length == 1) {
            String subcommand = args[0];
            if (subcommand.equals("modules")) {
                List<ModuleStatus> moduleStatusList = Module.getModuleStatusList();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Modules (");
                stringBuilder.append(moduleStatusList.size());
                stringBuilder.append(")");

                if (moduleStatusList.size() > 0) {
                    stringBuilder.append(": ");
                    stringBuilder.append(moduleStatusList.stream()
                        .map(status -> (status.isActive ? ChatColor.GREEN : ChatColor.RED) + status.moduleName + ChatColor.RESET)
                        .collect(Collectors.joining(", ")));
                }

                sender.sendMessage(stringBuilder.toString());
                return true;
            }

            else if (subcommand.equals("reload")) {
                Module.reloadConfigs();
                sender.sendMessage("Reloaded all configurations.");
                return true;
            }
        }

        sender.sendMessage("Subcommand not found.");
        return false;
    }
}
