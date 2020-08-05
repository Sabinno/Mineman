package dev.weary.limitedcommands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

public class TabCompleteClientAdapter extends PacketAdapter {
    public TabCompleteClientAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Client.TAB_COMPLETE);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPlayer().hasPermission(LimitedCommandsSettings.LIMITED_COMMANDS_PERMISSION.asString())) {
            return;
        }

        String fullCommandText = event.getPacket().getStrings().read(0);
        String baseCommand = LimitedCommandsModule.getBaseCommandPart(fullCommandText);
        if (LimitedCommandsModule.isCommandHidden(baseCommand)) {
            event.setCancelled(true);
        }
    }
}
