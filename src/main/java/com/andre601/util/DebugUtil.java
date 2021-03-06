package com.andre601.util;

import com.andre601.util.messagehandling.MessageUtil;
import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;
import com.andre601.core.PurrBotMain;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang.StringUtils;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * ---------------------------------------------------------------------
 * Code from DiscordSRV (https://github.com/Scarsz/DiscordSRV)
 *
 * Original Copyright (c) Scasrsz (Scarsz lol) 2018 (https://scarsz.me)
 * ---------------------------------------------------------------------
 */

public class DebugUtil {

    public static String run(User requester, TextChannel tc){
        Guild g = tc.getGuild();

        Map<String, String> files = new LinkedHashMap<>();
        try{
            files.put("Thank-You.yaml", String.join("\n", new String[]{
                    "#",
                    "# Thanks to Scarsz lol (http://scarsz.me/) for providing this site!",
                    "#",
                    "# GitHub: https://github.com/Scarsz",
                    "# SpigotMC: https://www.spigotmc.org/members/scarsz.149937/",
                    "# Discord: https://scarsz.me/discord",
                    "#"
            }));
            files.put("General-Info.yaml", String.join("\n", new String[]{
                    "#",
                    "# Info about who requested the debug.",
                    "#",
                    "",
                    "Requester: " + MessageUtil.getTag(requester),
                    "",
                    "#",
                    "# Info about the bot-Settings (Welcome-channel and prefix)",
                    "#",
                    "",
                    getWelcomeInfo(g),
                    "",
                    "Guild-Prefix: " + getGuildPrefix(g),
                    "Roles: " + g.getSelfMember().getRoles().size(),
                    "  " + getOwnRoles(g)
            }));
            files.put("Guild-Info.yaml", String.join("\n", new String[]{
                    "#",
                    "# Basic Guild-Info",
                    "#",
                    "",
                    "Guildname: "  + g.getName(),
                    "Guild-ID: " + g.getId(),
                    "Owner: " + MessageUtil.getTag(g.getOwner().getUser()) + "(" + g.getOwner().getUser().getId() + ")",
                    "",
                    "Users:",
                    "  Total: " + g.getMembers().toArray().length,
                    "  Humans: " + g.getMembers().stream().filter(user -> !user.getUser().isBot()).toArray().length,
                    "  Bots: " + g.getMembers().stream().filter(user -> user.getUser().isBot()).toArray().length,
                    "",
                    "TextChannels: " + g.getTextChannels().size(),
                    "  " + getChannels(g),
                    "",
                    "Roles: " + g.getRoles().size(),
                    "  " + getRoles(g)
            }));
            files.put("TextChannel-Permissions.yaml", String.join("\n", new String[]{
                    "#",
                    "# All Channels and their permission for the bot",
                    "#",
                    "",
                    getChannelPerms(g)
            }));
        }catch(Exception ignored){
            tc.sendMessage(MessageFormat.format(
                    "There was an issue with creating the files {0}.",
                    requester.getAsMention()
            )).queue();
        }

        return makeReport(files, requester);
    }

    private static String getChannels(Guild g){
        return g.getTextChannels().stream().map(TextChannel::toString).collect(Collectors.joining("\n  "));
    }

    private static String getRoles(Guild g){
        return g.getRoles().stream().map(Role::toString).collect(Collectors.joining("\n  "));
    }

    private static String getOwnRoles(Guild g){
        return g.getSelfMember().getRoles().stream().map(Role::toString).collect(Collectors.joining("\n  "));
    }

    private static String getChannelPerms(Guild g){
        List<String> output = new LinkedList<>();
        g.getTextChannels().forEach(textChannel -> {
            if(textChannel != null){
                List<String> channels = new LinkedList<>();
                channels.add("Read: " + PermUtil.canRead(textChannel));
                channels.add("Write: " + PermUtil.canWrite(textChannel));
                channels.add("Embed Links: " + PermUtil.canSendEmbed(textChannel));
                channels.add("Manage messages: " + PermUtil.canDeleteMsg(textChannel));
                channels.add("Add Reaction: " + PermUtil.canReact(textChannel));
                channels.add("Attach files: " + PermUtil.canUploadImage(textChannel));
                channels.add("Use external emojis: " + PermUtil.canUseCustomEmojis(textChannel));
                output.add(textChannel + ":\n  " + String.join("\n  ", channels));
            }
        });
        return String.join("\n", output);
    }

    private static String getWelcomeInfo(Guild g){
        String colorInfo = DBUtil.getColor(g);
        String colorType = colorInfo.split(":")[0];
        String colorValue = colorInfo.split(":")[1];
        return "Welcome-channel: " + DBUtil.getWelcome(g) + "\n" +
                "  Color-Type: " + colorType + "\n" +
                "  Color-Value: " + colorValue + "\n" +
                "  Image: " + DBUtil.getImage(g);
    }

    private static String getGuildPrefix(Guild g){
        return DBUtil.getPrefix(g);
    }

    private static String makeReport(Map<String, String> toUploadingFiles, User requester){
        if(toUploadingFiles.size() == 0)
            return MessageFormat.format("{0} The files are 0 for some reason...", requester.getAsMention());

        Map<String, String> files = new LinkedHashMap<>();
        toUploadingFiles.forEach((fileName, fileContent) -> files.put((files.size() + 1) + "-" + fileName,
                StringUtils.isNotBlank(fileContent) ? fileContent : "blank"));

        try{
            String url = uploadDebug(files);
            return "New Debug created! " + url;
        }catch (Exception ex){
            return MessageFormat.format("There was an issue with making the debug!\n" +
                    "Reason: {0}",
                    ex.getMessage());
        }
    }

    private static String uploadDebug(Map<String, String> files){
        HttpURLConnection connection = null;

        try{
            connection = (HttpURLConnection) new URL("https://debug.scarsz.me/post").openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Purr-Bot/" + PurrBotMain.getVersion());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            JsonObject payload = new JsonObject();
            payload.addProperty("description", "Purr-Bot Debug");

            JsonObject filesJson = new JsonObject();
            files.forEach((fileName, fileContent) -> {
                JsonObject file = new JsonObject();
                file.addProperty("content", fileContent);
                filesJson.add(fileName, file);
            });
            payload.add("files", filesJson);

            out.write(PurrBotMain.getGson().toJson(payload).getBytes(Charset.forName("UTF-8")));
            out.close();

            String rawOutput = CharStreams.toString(new InputStreamReader(connection.getInputStream()));
            connection.getInputStream().close();
            JsonObject output = PurrBotMain.getGson().fromJson(rawOutput, JsonObject.class);

            if(!output.has("url")) throw new RuntimeException("URL was not recieved. Debug failed!");
            return output.get("url").getAsString();
        }catch (Exception ex){
            if(connection != null) connection.disconnect();
            throw new RuntimeException(ex);
        }
    }
}
