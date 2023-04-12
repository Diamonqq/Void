package com.conutik.helpers;

import com.conutik.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Long.parseLong;

public class Utils {

    public static boolean isInSkyblock = false;
    public static long purse = 0;
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

    private static void checkIfInSkyblock(String s) {
        if (s.contains("SKYBLOCK") && !isInSkyblock) {
            isInSkyblock = true;
        } else if (!s.contains("SKYBLOCK") && isInSkyblock && Settings.autoLobby) {
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

    public static void ScoreboardData() {
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
                    if(Settings.autoIsland) setTimeout(() -> {
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
