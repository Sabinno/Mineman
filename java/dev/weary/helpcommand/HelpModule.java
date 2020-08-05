package dev.weary.helpcommand;

import dev.weary.module.Module;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class HelpModule extends Module {
    public static final String HELP_CATEGORIES = "categories";
    public static final String HELP_MAIN_CATEGORY = "main-category";
    public static final String HELP_NO_CATEGORY_MESSAGE = "no-category-message";

    public static HelpModule instance;

    private static String mainCategory;
    private static BaseComponent[] noCategoryMessage;
    public static Map<String, BaseComponent[]> helpCategoryMap;
    public static List<String> helpTopics;

    public static BaseComponent[] getMainHelpMessage() {
        return getHelpMessage(mainCategory);
    }

    public static BaseComponent[] getHelpMessage(String topic) {
        return helpCategoryMap.getOrDefault(topic, noCategoryMessage);
    }

    public HelpModule() {
        super("HelpCommand", "Manages a new configurable /help command", "help.yml");
        instance = this;
    }

    @Override
    protected boolean loadModule(JavaPlugin plugin) {
        this.initializeDefaultConfig(HelpSettings.values());
        plugin.getCommand("help").setExecutor(new HelpCommand());
        return true;
    }

    @Override
    protected void onConfigLoaded() {
        String mainCategory = HelpSettings.MAIN_CATEGORY.asString();
        if (mainCategory == null) {
            throw new HelpException("No main category found in the config");
        }

        HelpModule.mainCategory = mainCategory;

        ConfigurationSection allCategories = yamlConfig.getConfigurationSection(HELP_CATEGORIES);
        if (allCategories == null) {
            throw new HelpException("No categories found in the config");
        }

        Set<String> categoryNames = allCategories.getKeys(false);
        if (!categoryNames.contains(mainCategory)) {
            throw new HelpException("Main category '" + mainCategory + "' was not found in categories");
        }

        Object noCategoryText = HelpSettings.NO_CATEGORY_FOUND.asObject();
        if (noCategoryText == null) {
            throw new HelpException("No category text is empty");
        }

        HelpModule.noCategoryMessage = parseCategoryEntry(noCategoryText);

        HashMap<String, BaseComponent[]> newTopics = new HashMap<>();
        for (String categoryName: categoryNames) {
            Object categoryText = yamlConfig.get(HELP_CATEGORIES + "." + categoryName);
            if (categoryText == null) {
                throw new HelpException("Category '" + categoryName + "' contains no text");
            }

            newTopics.put(categoryName, parseCategoryEntry(categoryText));
        }

        helpCategoryMap = Collections.unmodifiableMap(newTopics);

        List<String> topics = new ArrayList<>(helpCategoryMap.keySet());
        topics.remove(mainCategory);
        helpTopics = Collections.unmodifiableList(topics);
    }

    @SuppressWarnings("unchecked")
    private @NotNull BaseComponent[] parseCategoryEntry(Object stringOrList) {
        if (stringOrList instanceof String) {
            return textToComponents((String) stringOrList);
        }
        else if (stringOrList instanceof List) {
            List<String> jsonOrTextList = (List<String>) stringOrList;
            return jsonOrTextList.stream()
                .map(this::textToComponents)
                .flatMap(Arrays::stream)
                .toArray(BaseComponent[]::new);
        }

        throw new HelpException("Expected a string or list of strings as a category entry");
    }

    private @NotNull BaseComponent[] textToComponents(String jsonOrText) {
        if (looksLikeJson(jsonOrText)) {
            return ComponentSerializer.parse(jsonOrText);
        }

        return TextComponent.fromLegacyText(jsonOrText.replaceAll("&([a-f0-9klmnor])", "§$1"));
    }

    private boolean looksLikeJson(String string) {
        String trimmedString = string.replaceAll("^\\s+|\\s+$", "");
        boolean inCurlyBrackets = trimmedString.startsWith("{") && trimmedString.endsWith("}");
        boolean inSquareBrackets = trimmedString.startsWith("[") && trimmedString.endsWith("]");

        return inCurlyBrackets || inSquareBrackets;
    }

    @Override
    protected void unloadModule() {}
}
