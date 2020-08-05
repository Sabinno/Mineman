package dev.weary.helpcommand;

import dev.weary.module.IConfigSetting;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

import static dev.weary.helpcommand.HelpModule.*;

public enum HelpSettings implements IConfigSetting {
    MAIN_CATEGORY(HELP_MAIN_CATEGORY, "main"),
    NO_CATEGORY_FOUND(HELP_NO_CATEGORY_MESSAGE, "This category does not exist."),
    CATEGORIES(HELP_CATEGORIES, new HashMap<String, String>()),
    CATEGORY_MAIN(HELP_CATEGORIES + ".main", "This is the default message for the main help category.\nIf you have the \"main-category\" option set to \"main\" in\nconfig, then this message will be displayed by default.");

    private String yamlName;
    private Object defaultValue;

    HelpSettings(String yamlName, Object defaultValue) {
        this.yamlName = yamlName;
        this.defaultValue = defaultValue;
    }

    public String asString() {
        return (String) HelpModule.instance.yamlConfig.get(yamlName, defaultValue);
    }

    public Object asObject() {
        return HelpModule.instance.yamlConfig.get(yamlName, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> asMap() {
        return (HashMap<String, String>) HelpModule.instance.yamlConfig.get(yamlName, defaultValue);
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
