package com.conutik.helpers;

import com.conutik.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

public class Utils {

    public static boolean isInSkyblock = false;
    public static long purse = 0;
    public static Thread checkerThread;

    public static boolean isWorking = true;
    private static long nextChange = System.currentTimeMillis();

    public static void stopChecker() {
        if(checkerThread != null) {
            checkerThread = null;
        }
    }

    public static void initiateChecker() {
        nextChange = (System.currentTimeMillis() + ((long) Settings.maxFlipTime *60*1000));
        isWorking = true;
        checkerThread = new Thread(() -> {
            setTimeout(Utils::check, 10000);
        });
        checkerThread.start();
    }

    public static void check() {
        if(nextChange <= System.currentTimeMillis()) {
            if(isWorking) {
                nextChange = (System.currentTimeMillis() + ((long) Settings.minFlipTime *60*1000));
                isWorking = !isWorking;
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 50, 2f);
                Helpers.sendChatMessage(EnumChatFormatting.RED + "FLIPS HAVE NOW BEEN STOPPED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.RED + "FLIPS HAVE NOW BEEN STOPPED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.RED + "FLIPS HAVE NOW BEEN STOPPED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.RED + "FLIPS HAVE NOW BEEN STOPPED BY ANTI-BAN");
            } else {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 50, 2f);
                nextChange = (System.currentTimeMillis() + ((long) Settings.maxFlipTime *60*1000));
                isWorking = !isWorking;
                Helpers.sendChatMessage(EnumChatFormatting.GREEN + "FLIPS HAVE NOW BEEN RESUMED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.GREEN + "FLIPS HAVE NOW BEEN RESUMED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.GREEN + "FLIPS HAVE NOW BEEN RESUMED BY ANTI-BAN");
                Helpers.sendChatMessage(EnumChatFormatting.GREEN + "FLIPS HAVE NOW BEEN RESUMED BY ANTI-BAN");
            }
        }
    }


    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    static HashSet<Integer> getGroups(String format)
    {
        Pattern pattern = Pattern.compile("(?:\\{(\\d+?)\\+?\\})");
        Matcher matcher = pattern.matcher(format);

        HashSet<Integer> matched = new HashSet<>();
        while(matcher.find())
        {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                int parsed = Integer.parseInt(matcher.group(i));
                if(!matched.contains(parsed))
                    matched.add(parsed);
            }
        }
        return matched;
    }

    public static Optional<HashMap<Integer, String>> extractParameters(String format, String text)
    {
        String clean = Pattern.quote(format).replaceAll("(\\{(\\d+?)\\})", "\\\\E(?<group$2>.+?)\\\\Q").replaceAll("(\\{(\\d+?)\\+\\})", "\\\\E(?<group$2>.+)\\\\Q").replaceAll("(\\{\\!\\})", "\\\\E(?:.+?)\\\\Q").replaceAll("(\\{\\!\\+\\})", "\\\\E(?:.+)\\\\Q");

        Matcher matcher = Pattern.compile(clean).matcher(text);
        if(!matcher.find())
        {
            return Optional.empty();
        }

        HashMap<Integer, String> matches = new HashMap<>();
        HashSet<Integer> groups = getGroups(format);
        for(int key : groups)
        {
            try {
                matches.put(key, matcher.group("group" + key));
            } catch (Exception ignored) {}
        }
        return Optional.of(matches);
    }

    private static void checkIfInSkyblock(String s) throws URISyntaxException {
        if (s.contains("SKYBLOCK") && !isInSkyblock) {
            isInSkyblock = true;
            if(Settings.antiBan) Utils.initiateChecker();
            if(Variables.getWebsocket() == null && Settings.hIDDEN) Variables.initWebsocket(Minecraft.getMinecraft().thePlayer.getDisplayNameString());
        } else if (!s.contains("SKYBLOCK") && isInSkyblock && Settings.autoLobby && isWorking) {
            setTimeout(() -> {
                Helpers.sendDebugMessage("Found outside of skyblock, taking you back in O_O");
                (Minecraft.getMinecraft()).thePlayer.sendChatMessage("/play sb");
            }, 5000);
            isInSkyblock = false;
        }
    }

    private static List<String> getScoreboard() {
        ArrayList<String> scoreboardAsText = new ArrayList<>();
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null) {
            return scoreboardAsText;
        }
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective sideBarObjective = scoreboard.getObjectiveInDisplaySlot(1);
        if (sideBarObjective == null) {
            return scoreboardAsText;
        }
        String scoreboardTitle = sideBarObjective.getDisplayName();
        scoreboardTitle = EnumChatFormatting.getTextWithoutFormattingCodes(scoreboardTitle);
        scoreboardAsText.add(scoreboardTitle);
        Collection<Score> scoreboardLines = scoreboard.getSortedScores(sideBarObjective);
        for (Score line : scoreboardLines) {
            String playerName = line.getPlayerName();
            if (playerName == null || playerName.startsWith("#")) {
                continue;
            }
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(playerName);
            String lineText = EnumChatFormatting.getTextWithoutFormattingCodes(
                    ScorePlayerTeam.formatPlayerName(scorePlayerTeam, line.getPlayerName()));
            scoreboardAsText.add(lineText.replace(line.getPlayerName(), ""));
        }
        return scoreboardAsText;
    }

    public static void ScoreboardData() throws URISyntaxException {
        String s;
        try {
            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            s = EnumChatFormatting.getTextWithoutFormattingCodes(objective.getDisplayName());
        } catch (Exception e) {
            return;
        }
        checkIfInSkyblock(s);

        List<String> scoreBoardLines = getScoreboard();
        int size = scoreBoardLines.size() - 1;
        for (int i = 0; i < scoreBoardLines.size(); i++) {
            String line = EnumChatFormatting.getTextWithoutFormattingCodes(scoreBoardLines.get(size - i).toLowerCase());
                ProcessScoreboard(line);
            if (line.contains("⏣") && !line.equals(" ⏣ your island")) {
                    if(Settings.autoIsland && isWorking) setTimeout(() -> {
                        Helpers.sendDebugMessage("Why you not at your island, go back to your \"workers\"");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/is");
                    }, 5000);
            }
        }
    }

    private static void ProcessScoreboard(String line) {
        if (line.contains("purse") || line.contains("piggy")) {
            long purse_ = 0;
            try {
                purse_ = parseLong(line.split(" ")[1].replace(",", "").split("\\.")[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Helpers.sendDebugMessage("unparsable purse: " + line);
            }
            if (purse != purse_) {
                purse = purse_;
            }
        }
    }
}
