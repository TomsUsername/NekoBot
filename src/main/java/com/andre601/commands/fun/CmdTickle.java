package com.andre601.commands.fun;

import com.andre601.commands.Command;
import com.andre601.commands.server.CmdPrefix;
import com.andre601.util.constants.Emojis;
import com.andre601.util.messagehandling.EmbedUtil;
import com.andre601.util.HttpUtil;
import com.andre601.util.PermUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CmdTickle implements Command {

    public void usage(Message msg){
        msg.getTextChannel().sendMessage(String.format(
                "%s Please mention a user at the end of the command to tickle!\n" +
                "Example: `%stickle @*Purr*#6875`",
                msg.getAuthor().getAsMention(),
                CmdPrefix.getPrefix(msg.getGuild())
        )).queue();
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        TextChannel tc = e.getTextChannel();
        Message msg = e.getMessage();

        if (!PermUtil.canWrite(tc))
            return;

        if(!PermUtil.canSendEmbed(tc)){
            tc.sendMessage("I need the permission, to embed Links in this Channel!").queue();
            if(PermUtil.canReact(tc))
                e.getMessage().addReaction("🚫").queue();

            return;
        }

        if(args.length < 1){
            usage(e.getMessage());
            return;
        }

        String link = HttpUtil.getTickle();

        List<User> user = msg.getMentionedUsers();

        if(user.isEmpty()){
            usage(e.getMessage());
            return;
        }

        if(user.size() == 1){
            User u = user.get(0);
            if(u == msg.getJDA().getSelfUser()){
                if(PermUtil.canReact(tc))
                    e.getMessage().addReaction("😂").queue();

                tc.sendMessage(String.format("N-no... I can't lau- \\*laughs*",
                        msg.getMember().getAsMention())).queue();
                return;
            }
            if(u == msg.getAuthor()){
                tc.sendMessage("Why are you tickling yourself?").queue();
                return;
            }
            String name = u.getAsMention();
            tc.sendMessage(Emojis.IMG_LOADING + " Getting a tickle-gif...").queue(message -> {
                if(link != null)
                    message.editMessage("\u200B").embed(EmbedUtil.getEmbed().setDescription(MessageFormat.format(
                            "{0} tickles {1}",
                            msg.getMember().getEffectiveName(),
                            name
                    )).setImage(link).build()).queue();
                else
                    message.editMessage(MessageFormat.format(
                            "{0} tickles {1}",
                            msg.getMember().getEffectiveName(),
                            name
                    )).queue();
            });
        }else{
            String users = user.stream().map(User::getAsMention).collect(Collectors.joining(", "));
            tc.sendMessage(Emojis.IMG_LOADING + " Getting a tickle-gif...").queue(message -> {
                if(link != null)
                    message.editMessage("\u200B").embed(EmbedUtil.getEmbed().setDescription(MessageFormat.format(
                            "{0} tickles {1}",
                            msg.getMember().getEffectiveName(),
                            users
                    )).setImage(link).build()).queue();
                else
                    message.editMessage(MessageFormat.format(
                            "{0} tickles {1}",
                            msg.getMember().getEffectiveName(),
                            users
                    )).queue();
            });
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
