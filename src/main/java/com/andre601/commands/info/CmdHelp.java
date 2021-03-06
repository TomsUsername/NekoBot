package com.andre601.commands.info;

import com.andre601.commands.server.CmdPrefix;
import com.andre601.util.PermUtil;
import com.jagrosh.jdautilities.menu.Paginator;
import com.andre601.commands.Command;
import com.andre601.util.messagehandling.EmbedUtil;
import com.andre601.util.messagehandling.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.andre601.core.PurrBotMain.waiter;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class CmdHelp implements Command {

    private static Paginator.Builder pBuilder =
            new Paginator.Builder().setEventWaiter(waiter).setTimeout(1, TimeUnit.MINUTES);

    public static void usage(Message msg, String command, String use, String description, String args,
                             String permission){
        msg.getChannel().sendTyping().queue();
        EmbedBuilder uEmbed = EmbedUtil.getEmbed(msg.getAuthor())
                .setTitle(String.format(
                        "Command: %s",
                        command
                ))
                .setDescription(String.format(
                        "**Usage**: `%s%s`\n" +
                        "\n" +
                        "**Description**:\n" +
                        "%s\n" +
                        "\n" +
                        "**Arguments**:\n" +
                        "%s\n" +
                        "\n" +
                        "**Required Permission**:\n" +
                        "%s",
                        CmdPrefix.getPrefix(msg.getGuild()),
                        use,
                        description,
                        args,
                        permission
                ));

        msg.getChannel().sendMessage(uEmbed.build()).queue();
    }

    private static void usage(Message msg){

        User user = msg.getAuthor();
        Guild g = msg.getGuild();
        TextChannel tc = msg.getTextChannel();
        String prefix = CmdPrefix.getPrefix(g);

        Paginator page = pBuilder
                .setItems(MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "```\n" +
                        "Pages:\n" +
                        "  Fun\n" +
                        "  Informative\n" +
                        "  NSFW\n" +
                        "  Server\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        prefix,
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Fun**:\n" +
                        "```\n" +
                        "Command:   Argument(s):\n" +
                        "\n" +
                        "Cuddle     <@user ...>\n" +
                        "Fakegit\n" +
                        "Gecg\n" +
                        "Hug        <@user ...>\n" +
                        "Kiss       <@user ...>\n" +
                        "Kitsune\n" +
                        "Neko       [-gif]\n" +
                        "           [-slide]\n" +
                        "Pat        <@user ...>\n" +
                        "Poke       <@user ...>\n" +
                        "Slap       <@user ...>\n" +
                        "Tickle     <@user ...>\n" +
                        "\n" +
                        "[optional]\n" +
                        "<required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        prefix,
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Informative**:\n" +
                        "```\n" +
                        "Command:   Argument(s):\n" +
                        "\n" +
                        "Emote      <Emote>\n" +
                        "Help       [command]\n" +
                        "Info       [-here]\n" +
                        "Invite     [-here]\n" +
                        "Ping       [-api]\n" +
                        "Quote      <messageID> <#channel>\n" +
                        "Server\n" +
                        "Stats\n" +
                        "User       [@user]\n" +
                        "\n" +
                        "[optional]\n" +
                        "<required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        prefix,
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**NSFW**:\n" +
                        "```\n" +
                        "Command:   Argument(s):\n" +
                        "\n" +
                        "Fuck       <@user>\n" +
                        "Lewd       [-gif]\n" +
                        "           [-slide]\n" +
                        "Lesbian\n" +
                        "\n" +
                        "[optional]\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        prefix,
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Server**:\n" +
                        "```\n" +
                        "Command:   Argument(s):\n" +
                        "\n" +
                        "Debug\n" +
                        "Prefix     [set <prefix>]\n" +
                        "           [reset]\n" +
                        "Welcome    [color <rgb:r,g,b|hex:#code>]\n" +
                        "           [reset]\n" +
                        "           [set <ChannelID> [image]]\n" +
                        "           [test [image]]\n" +
                        "\n" +
                        "[optional]\n" +
                        "<required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        prefix,
                        MessageUtil.getFact()
                ))
                .setText("")
                .setUsers(user, g.getOwner().getUser())
                .setItemsPerPage(1)
                .waitOnSinglePage(true)
                .setFinalAction(message -> {
                    if(message != null)
                        message.delete().queue();
                })
                .build();

        page.display(tc);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        Message msg = e.getMessage();
        TextChannel tc = e.getTextChannel();

        if (!PermUtil.canWrite(tc))
            return;

        if(!PermUtil.canSendEmbed(tc)){
            e.getTextChannel().sendMessage("I need the permission, to embed Links in this Channel!").queue();
            if(PermUtil.canReact(tc))
                e.getMessage().addReaction("🚫").queue();

            return;
        }

        if(!PermUtil.canReact(tc)){
            tc.sendMessage(String.format(
                    "%s I need permission, to add reactions in this channel!"
            )).queue(del -> del.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        if(PermUtil.canDeleteMsg(tc))
            msg.delete().queue();

        if(args.length == 0){
            usage(msg);
            return;
        }

        switch (args[0].toLowerCase()){

            case "help":
                usage(msg, "Help", "help [command]",
                        "Shows you all available commands.",
                        "`[command]` Gives info about a command.",
                        "`none`"
                );
                break;

            case "info":
                usage(msg, "info", "info [-here]",
                        "Sends you basic info about the bot (A small description, version, used " +
                        "Library, ect)",
                        "`[-here]` Sends the message in the channel in which you've run the command.",
                        "`none`"
                );
                break;

            case "invite":
                usage(msg, "Invite", "invite [-here]",
                        "Sends you the invite-Links for the bot and for the official Discord.",
                        "`[-here]` Sends the message in the channel in which you've run the command.",
                        "none"
                );
                break;

            case "server":
                usage(msg, "Server",  "server",
                        "Gives you basic server-infos like users online, verification level, ect.",
                        "`none`",
                        "`none`"
                );
                break;

            case "user":
                usage(msg, "User", "user [@user]",
                        "Gives basic information about the mentioned user.",
                        "`<@user>` The user to get infos about (as mention).",
                        "`none`"
                );
                break;

            case "neko":
                usage(msg, "Neko", "neko [-gif] [-slide]",
                        "Sends a cute neko. 'nuf said.",
                        "`[-gif]` Sends a cute neko-gif.\n" +
                        "`[-slide]` Creates a slideshow with 30 images\n" +
                        "\n" +
                        "You can use both arguments at the same time!",
                        "`none`"
                );
                break;

            case "hug":
                usage(msg, "Hug", "hug <@user ...>",
                        "Gives the mentioned user a hug.",
                        "`<@user ...>` The user to hug (as mention).",
                        "`none`"
                );
                break;

            case "pat":
                usage(msg, "Pat", "pat <@user ...>",
                        "Gives the mentioned user a pat.",
                        "`<@user ...>` The user(s) to pat (as mention).",
                        "`none`"
                );
                break;

            case "slap":
                usage(msg, "Slap", "slap <@user ...>",
                        "Slaps the mentioned user.",
                        "`<@user ...>` The user(s) to slap (as mention).",
                        "`none`"
                );
                break;

            case "lewd":
                usage(msg, "Lewd", "lewd [-gif] [-slide]",
                        "Sends a lewd neko.\n" +
                        "Can only be used in NSFW-Channels.",
                        "`[-gif]` Sends a lewd neko-gif.\n" +
                        "`[-slide]` Creates a slideshow with 30 images\n" +
                        "\n" +
                        "You can use both arguments at the same time!",
                        "`none`"
                );
                break;

            case "prefix":
                usage(msg, "Prefix", "prefix [set <prefix>|reset]",
                        "Shows the currently used prefix in this Discord, if no argument is given.",
                        "`[set <prefix>]` Sets the prefix to the provided one.\n" +
                        "`[reset]` Resets the prefix to the default one.",
                        "`MANAGE_SERVER` for setting or resetting the prefix.");
                break;

            case "cuddle":
                usage(msg, "Cuddle", "cuddle <@user ...>",
                        "Cuddles the mentioned user.",
                        "`<@user ...>` The user(s) to cuddle (as mention).",
                        "`none`"
                );
                break;

            case "tickle":
                usage(e.getMessage(), "Tickle", "tickle <@user ...>",
                        "Tickles the mentioned user.",
                        "`<@user ...>` The user(s) to tickle (as mention).",
                        "`none`"
                );
                break;

            case "welcome":
                usage(e.getMessage(), "Welcome", "welcome [color <rgb:r,g,b|hex:#code>|reset|set " +
                                "<ChannelID> [image]|test [image] [color]]",
                        "Shows, sets or resets the Welcome-channel.",
                        "[color <rgb:r,g,b|hex:#code>] Sets the text-color in either RGB or Hexadecimal.\n" +
                        "`[reset]` Resets (removes) the welcome-channel.\n" +
                        "`[test [image] [color]]` Creates a welcome-image in the channel you currently " +
                        "are. [image] is the image to use. [color] is the text-color.\n" +
                        "`[set <ChannelID> [image]]` Sets a Channel as Welcome-Channel. [image] lets you set " +
                        "a different image.",
                        "`MANAGE_SERVER`"
                );
                break;

            case "stats":
            case "stat":
                usage(e.getMessage(), "Stats", "stats",
                        "Shows some statistics of \\*Purr*",
                        "`none`",
                        "`none`"
                );
                break;

            case "kiss":
                usage(e.getMessage(), "Kiss", "kiss <@user ...>",
                        "Lets you kiss someone.",
                        "`<@user ...>` The user(s) you want to kiss (as mention).",
                        "`none`"
                );
                break;

            case "quote":
                usage(e.getMessage(), "Quote", "quote <messageID> <#channel>",
                        "Lets you quote a message.",
                        "`<messageID>` The ID of the message to quote.\n" +
                        "`<#channel>` The mention of the channel, where the message is located.",
                        "`none`"
                );
                break;

            case "debug":
                usage(msg, "Debug", "debug",
                        "Will create debug-files and post them on [debug.scarsz.me](https://debug.scarsz.me",
                        "`none`",
                        "`MANAGE_SERVER`");
                break;

            case "ping":
                usage(msg, "Ping", "ping [-api]",
                        "Will respond with the respond-time in ms.",
                        "`[-api]` Checks the Ping of the JDA-API.",
                        "`none`");
                break;

            case "poke":
                usage(msg, "Poke", "poke <@user ...>",
                        "Will poke the mentioned user(s).",
                        "`<@user ...>` The user(s) you want to poke (as mention)",
                        "`none`");
                break;

            case "gecg":
                usage(msg, "Gecg", "gecg",
                        "Posts a image from [gecfdo.com](https://gecfdo.com).",
                        "`none`",
                        "`none`");
                break;

            case "lesbian":
            case "les":
                usage(msg, "Lesbian", "lesbian",
                        "Posts a lesbian-gif.\n" +
                        "Can only be used in NSFW-labeled channels!",
                        "`none`",
                        "`none`");
                break;

            case "kitsune":
            case "foxgirl":
                usage(msg, "Kitsune", "kitsune",
                        "Posts a image of a kitsune (foxgirl).",
                        "`none`",
                        "`none`"
                );
                break;

            case "fakegit":
            case "git":
                usage(msg, "Fakegit", "fakegit",
                        "Creates a fake github-message.",
                        "`none`",
                        "`none`");
                break;

            case "fuck":
                usage(msg, "Fuck", "fuck <@user>",
                        "Sends a request to the mentioned user, who can accept it.\n" +
                        "To accept, you have to type `>accept`!\n" +
                        "\n" +
                        "Can only be used in NSFW-labeled channels.",
                        "`<@user>` The user you want to fuck.",
                        "`none`");
                break;

            default:
                usage(msg);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {

    }

    @Override
    public String help() {
        return null;
    }
}
