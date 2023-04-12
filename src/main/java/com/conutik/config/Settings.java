package com.conutik.config;

import com.conutik.helpers.Helpers;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Settings extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Buy",
            description = "Should auto buy be on?"
    )
    public static boolean autoBuy = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Open",
            description = "Should auto open be on? \nP.S: Full skip is turned on with this :D"
    )
    public static boolean autoOpen = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Claim",
            description = "Turn on or off auto claiming auctions"
    )
    public static boolean autoClaim = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Debug",
            description = "Should debug be on?"
    )
    public static boolean debug = false;

    @Property(
            type = PropertyType.NUMBER,
            category = "Settings",
            subcategory = "Advanced",
            name = "Bed Delay",
            description = "Should auto open be on?",
            min = 0,
            max = 1_00_000
    )
    public static int bedDelay = 75;

    @Property(
            type = PropertyType.NUMBER,
            category = "Settings",
            subcategory = "Advanced",
            name = "Full Skip Delay",
            description = "What should full skip delay be?",
            min = 0,
            max = 1_00_000
    )
    public static int fullSkipDelay = 10;

    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Flip",
            name = "Flip Webhook",
            description = "Turn on or off flip webhooks"
    )
    public static boolean flipWebhooks = false;
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Flip",
            name = "URL",
            description = "Flip Webhook URL"
    )
    public static String flipWebhookURL = "";
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Flip",
            name = "Custom Message",
            description = "Add a custom message to be displayed in the webhook"
    )
    public static String flipWebhookDescription = "";

    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "Webhook",
            description = "Turn on or off purchase webhooks"
    )
    public static boolean purchaseWebhooks = false;
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "URL",
            description = "Sold Auctions Webhook URL"
    )
    public static String purchaseWebhookURL = "";
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "Custom Message",
            description = "Add a custom message to be displayed in the webhook"
    )
    public static String purchaseWebhookDescription = "";

    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Captcha",
            name = "Captcha Webhook",
            description = "Turn on or off captcha webhooks"
    )
    public static boolean captchaWebhooks = false;
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Captcha",
            name = "URL",
            description = "Captcha Webhook URL"
    )
    public static String captchaWebhookURL = "";
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Captcha",
            name = "Custom Message",
            description = "Add a custom message to be displayed in the webhook"
    )
    public static String captchaWebhookDescription = "";

    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Misc",
            name = "Format",
            description = "Flipper message format (This is needed to show flips in webhook)"
    )
    public static String msgFormat = "";

    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Misc",
            name = "Discord ID",
            description = "Your discord ID if you want to be pinged with webhooks"
    )
    public static String discordID = "";

    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Anti-Limbo",
            description = "Leaves limbo and joins skyblock"
    )
    public static boolean antiLimbo = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Anti-Lobby",
            description = "Leaves any lobby and takes you back to skyblock"
    )
    public static boolean autoLobby = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Auto-Island",
            description = "Joins island after leaving limbo (Must have anti-limbo enabled)"
    )
    public static boolean autoIsland = false;


    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Auto Reconnect",
            description = "Joins hypixel after disconnecting"
    )
    public static boolean autoConnect = false;





    public Settings() {
        super(CONFIG_FILE, "Void Flipper Configuration", new JVMAnnotationPropertyCollector());
        initialize();
        registerListener("autoBuy", choice -> {
            Helpers.sendChatMessage("Autobuy " + (autoBuy ? "off" : "on"));
        });
        registerListener("autoOpen", choice -> {
            Helpers.sendChatMessage("Autoopen " + (autoOpen ? "off" : "on"));
        });
        registerListener("bedDelay", choice -> {
            Helpers.sendChatMessage("New bed click delay: " + (bedDelay+1));
        });
        registerListener("fullSkipDelay", choice -> {
            Helpers.sendChatMessage("New full skip delay: " + (fullSkipDelay+1));
        });
        registerListener("debug", choice -> {
            Helpers.sendChatMessage("Debug " + (debug ? "off" : "on"));
        });
        registerListener("antiLimbo", choice -> {
            Helpers.sendChatMessage("Anti-Limbo " + (antiLimbo ? "off" : "on"));
        });
        registerListener("autoIsland", choice -> {
            Helpers.sendChatMessage("Auto-Island " + (autoIsland ? "off" : "on"));
        });
        registerListener("autoLobby", choice -> {
            Helpers.sendChatMessage("Auto-Lobby " + (autoLobby ? "off" : "on"));
        });
        registerListener("flipWebhooks", choice -> {
            Helpers.sendChatMessage("Flip Webhooks  " + (flipWebhooks ? "off" : "on"));
        });
        registerListener("captchaWebhooks", choice -> {
            Helpers.sendChatMessage("Captcha Webhooks  " + (captchaWebhooks ? "off" : "on"));
        });
        registerListener("purchaseWebhooks", choice -> {
            Helpers.sendChatMessage("Purchase Webhooks  " + (purchaseWebhooks ? "off" : "on"));
        });
        registerListener("autoClaim", choice -> {
            Helpers.sendChatMessage("Auto Claiming Auctions  " + (autoClaim ? "off" : "on"));
        });
        registerListener("autoConnect", choice -> {
            Helpers.sendChatMessage("Auto Reconnect  " + (autoConnect ? "off" : "on"));
        });
    }

    public static final File CONFIG_FILE = new File("config/void.toml");
}
