package com.andre601.listeners;

import com.andre601.core.PurrBotMain;
import com.andre601.util.DBUtil;
import com.andre601.util.PermUtil;
import com.andre601.util.messagehandling.MessageUtil;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter{

    private static String setBotGame(){
        return (PermUtil.isBeta() ? "My sister on %s Guilds!" : "https://purrbot.site | %s Guilds");
    }

    public static String getBotGame(){
        return setBotGame();
    }

    public void onReady(ReadyEvent e){

        String botID = e.getJDA().getSelfUser().getId();
        int guilds = e.getJDA().getGuilds().size();

        /*
         *  Create a new DB-entry for every guild that isn't in the DB.
         *  Fixes the issue, when someone invites the bot while it's offline.
         */
        for(Guild g : e.getJDA().getGuilds()){
            if(!DBUtil.hasGuild(g)){
                DBUtil.newGuild(g);
            }
        }

        System.out.println(String.format(
                "[INFO] Enabled Bot-User %s (%s)\n" +
                "  > Version: %s\n" +
                "  > JDA: %s\n" +
                "  > Discords loaded: %s",
                MessageUtil.getTag(e.getJDA().getSelfUser()),
                botID,
                PurrBotMain.getVersion(),
                JDAInfo.VERSION,
                e.getJDA().getGuilds().size()
        ));

        //  Sending update if Bot isn't beta
        if(!PermUtil.isBeta())
            PurrBotMain.getAPI().setStats(guilds);

        e.getJDA().getPresence().setPresence(OnlineStatus.ONLINE, Game.watching(String.format(
                getBotGame(),
                e.getJDA().getGuilds().toArray().length
        )));
    }
}
