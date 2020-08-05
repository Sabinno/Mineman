package dev.weary.limitedcommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandListHandler implements Listener {

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().hasPermission(LimitedCommandsSettings.LIMITED_COMMANDS_PERMISSION.asString())) {
            return;
        }

        event.getCommands().removeIf(command -> LimitedCommandsModule.isCommandHidden("/" + command));
    }
}
