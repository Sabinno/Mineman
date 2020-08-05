package dev.weary.limitedcommands;

import dev.weary.module.IConfigSetting;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum LimitedCommandsSettings implements IConfigSetting {
    LIMITED_COMMANDS_PERMISSION("limited-commands.permission", "mineman.all-commands"),
    LIMITED_COMMANDS_MESSAGE("limited-commands.message", "Unknown command. Type \"/help\" for help."),
    LIMITED_COMMANDS_EXCEPTIONS("limited-commands.exceptions", new ArrayList<String>());

    private String yamlName;
    private Object defaultValue;

    LimitedCommandsSettings(String yamlName, Object defaultValue) {
        this.yamlName = yamlName;
        this.defaultValue = defaultValue;
    }

    public String asString() {
        return (String) LimitedCommandsModule.instance.yamlConfig.get(yamlName, defaultValue);
    }
    public String asColoredString() {
        return ChatColor.RESET + this.asString().replaceAll("&([a-f0-9klmnor])", "§$1");
    }
    public List<String> asStringList() {
        return LimitedCommandsModule.instance.yamlConfig.getStringList(yamlName);
    }

    @NotNull
    public String getYamlName() {
        return this.yamlName;
    }

    @NotNull
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}
