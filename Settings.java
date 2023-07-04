package com.conutik.config;

import com.conutik.helpers.Helpers;
import com.conutik.helpers.Utils;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;

import static com.conutik.helpers.Formatting.*;

public class Settings extends Vigilant {

    // SETTINGS
    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Buy",
            description = "When enabled, Void will buy any BIN auction that you enter."
    )
    public static boolean autoBuy = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Open",
            description = "When enabled, Void will open any flips that are received."
    )
    public static boolean autoOpen = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Auto Claim",
            description = "Enable/disable auto-claiming sold auctions."
    )
    public static boolean autoClaim = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Debug",
            description = "Shows debugging information in chat when enabled."
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

    /*
    @Property(
            type = PropertyType.SLIDER,
            category = "Settings",
            subcategory = "Advanced",
            name = "Bed Thread Kill Delay",
            description = "Set the bed thread kill delay",
            min = 50,
            max = 5000,
            increment = 50
    )
    */
    public static int bedThreadKillDelay = 750;

    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Full Skip",
            description = "Enable/disable full skip."
    )
    public static boolean fullSkip = false;

    @Property(
            type = PropertyType.NUMBER,
            category = "Settings",
            subcategory = "Advanced",
            name = "Full Skip Delay",
            description = "Full skip delay in milliseconds.",
            min = 0,
            max = 1_00_000
    )
    public static int fullSkipDelay = 10;

    @Property(
            type = PropertyType.TEXT,
            category = "Settings",
            subcategory = "Customization",
            name = "Mod Prefix",
            description = "Changes the mod's message prefix. Leave blank for the default format."
    )
    public static String modPrefix = "";

    /*
    No worky atm
    @Property(
            type = PropertyType.SWITCH,
            category = "Settings",
            subcategory = "Advanced",
            name = "Send Flip Buyer",
            description = "Enable/disable sending the flip buyer in chat when opening a bought flip."
    )
    */
    public static boolean sendFlipBuyer = false;

    // WEBHOOKS
    // FLIP WEBHOOK
    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Flip",
            name = "Flip Webhook",
            description = "Enable/disable webhook messages being sent when a flip is bought."
    )
    public static boolean buyWebhooks = false;
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Flip",
            name = "URL",
            description = "Flip Webhook URL"
    )
    public static String buyWebhookURL = "";
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Flip",
            name = "Custom Message",
            description = "Add a custom message to be sent in the webhook embed."
    )
    public static String buyWebhookDescription = "";
    @Property(
            type = PropertyType.COLOR,
            category = "Webhooks",
            subcategory = "Flip",
            name = "Webhook Color",
            description = "Change the color of the webhook."
    )
    public static Color buyWebhookColor = new Color(0, 0, 0);

    // PURCHASE WEBHOOK
    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "Webhook",
            description = "Enable/disable webhook messages being sent when auctions are sold."
    )
    public static boolean soldWebhooks = false;
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "URL",
            description = "Sold Auctions Webhook URL"
    )
    public static String soldWebhookURL = "";
    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "Custom Message",
            description = "Add a custom message to be displayed in the webhook."
    )
    public static String soldWebhookDescription = "";
    @Property(
            type = PropertyType.COLOR,
            category = "Webhooks",
            subcategory = "Sold Auctions",
            name = "Webhook Color",
            description = "Change the color of the webhook."
    )
    public static Color soldWebhookColor = new Color(0, 170, 255);

    // CAPTCHA WEBHOOK
    @Property(
            type = PropertyType.SWITCH,
            category = "Webhooks",
            subcategory = "Captcha",
            name = "Captcha Webhook",
            description = "Enable/disable webhook messages being sent when a Cofl captcha is encountered."
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
            description = "Add a custom message to be displayed in the webhook."
    )
    public static String captchaWebhookDescription = "";
    @Property(
            type = PropertyType.COLOR,
            category = "Webhooks",
            subcategory = "Captcha",
            name = "Captcha Webhook Color",
            description = "Change the color of the webhook."
    )
    public static Color captchaColor = new Color(255, 65, 65);

    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Misc",
            name = "Format",
            description = "Flipper message format (NOTE: This is needed to show flips in webhook)."
    )
    public static String msgFormat = "";

    @Property(
            type = PropertyType.TEXT,
            category = "Webhooks",
            subcategory = "Misc",
            name = "Discord ID",
            description = "Enter your Discord ID if you want to be pinged in webhook messages."
    )
    public static String discordID = "";

    // ANTI-AFK
    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Anti-Limbo",
            description = "Leaves limbo and joins SkyBlock."
    )
    public static boolean antiLimbo = false;

    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Anti-Lobby",
            description = "Leaves any lobby and takes you back to skyblock"
    )
    public static boolean antiLobby = false;

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

    @Property(
            type = PropertyType.SWITCH,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Anti-Ban",
            description = "A Ban prevention measure"
    )
    public static boolean antiBan = false;

    @Property(
            type = PropertyType.NUMBER,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Max Flip time",
            description = "Maximum time allowed to flip in hours (Ban prevention measure)",
            min = 0,
            max = 14
    )
    public static int maxFlipTime = 5;

    @Property(
            type = PropertyType.NUMBER,
            category = "Anti-AFK",
            subcategory = "Prevention",
            name = "Flip downtime",
            description = "When to return to flipping in hours. (Ban prevention measure)",
            min = 0,
            max = 10
    )
    public static int minFlipTime = 2;

    public Settings() {
        super(CONFIG_FILE, "Void Flipper Configuration", new JVMAnnotationPropertyCollector());
        initialize();
        registerListener("autoBuy", choice -> {
            Helpers.sendChatMessage("Autobuy " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("autoOpen", choice -> {
            Helpers.sendChatMessage("Autoopen " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("bedDelay", choice -> {
            Helpers.sendChatMessage("New bed click delay: " + (choice));
        });
//        registerListener("bedThreadKillDelay", choice -> {
//            Helpers.sendChatMessage("New bed thread kill delay: " + (choice));
//        });
        registerListener("fullSkipDelay", choice -> {
            Helpers.sendChatMessage("New full skip delay: " + (choice));
        });
        registerListener("maxFlipTime", choice -> {
            Helpers.sendChatMessage("Maximum flip time is now: " + maxFlipTime);
        });
        registerListener("minFlipTime", choice -> {
            Helpers.sendChatMessage("Flip downtime is now: " + minFlipTime);
        });
        registerListener("antiBan", choice -> {
            if (antiBan) Utils.stopChecker();
            else Utils.initiateChecker();
            Helpers.sendChatMessage("Anti-Ban " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("debug", choice -> {
            Helpers.sendChatMessage("Debug " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("antiLimbo", choice -> {
            Helpers.sendChatMessage("Anti-Limbo " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("autoIsland", choice -> {
            Helpers.sendChatMessage("Auto-Island " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("antiLobby", choice -> {
            Helpers.sendChatMessage("Anti-Lobby " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("buyWebhooks", choice -> {
            Helpers.sendChatMessage("Flip Webhooks " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r + "." + (msgFormat.isEmpty() ? " §c§lWARNING!§r You will need to put your Cofl flip format into the \"flip format\" text box for flips to both show up in chat and be sent to the webhook. If you do not, flips will be hidden." : ""));
        });
        registerListener("captchaWebhooks", choice -> {
            Helpers.sendChatMessage("Captcha Webhooks " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("soldWebhooks", choice -> {
            Helpers.sendChatMessage("Purchase Webhooks " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("autoClaim", choice -> {
            Helpers.sendChatMessage("Auto Claiming Auctions " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
        registerListener("autoConnect", choice -> {
            Helpers.sendChatMessage("Auto Reconnect " + ((Boolean) choice ? fmt_a + "enabled" : fmt_c + "disabled")+ fmt_r +".");
        });
    }

    public static final File CONFIG_FILE = new File("config/void.toml");
}
