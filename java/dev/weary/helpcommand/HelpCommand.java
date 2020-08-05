package dev.weary.helpcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    private static final List<String> NO_SUGGESTIONS = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(HelpModule.getMainHelpMessage());
        }
        else {
            String fullTopic = String.join(" ", args);
            sender.sendMessage(HelpModule.getHelpMessage(fullTopic));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) {
            return HelpModule.helpTopics;
        }

        return NO_SUGGESTIONS;
    }
}
