package com.andre601.core;

import com.andre601.util.ConfigUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/*
 * |------------------------------------------------------------------|
 *   Code from Gary (GitHub: https://github.com/help-chat/Gary)
 *
 *   Used with permission of PiggyPiglet
 *   Original Copyright (c) PiggyPiglet 2018 (https://piggypiglet.me)
 * |------------------------------------------------------------------|
 */

public class GFile {

    private ConfigUtil cutil = new ConfigUtil();
    private Map<String, File> gFiles;

    public void make(String name, String externalPath, String internalPath){
        if(gFiles == null){
            gFiles = new HashMap<>();
        }

        File file = new File(externalPath);
        String[] externalSplit = externalPath.split("/");

        try{
            if(!file.exists()){
                if((externalSplit.length == 2 && !externalSplit[0].equals(".") || (externalSplit.length >= 3 &&
                externalSplit[0].equals(".")))){
                    if(!file.getParentFile().mkdirs()){
                        System.out.println("[WARN] Failed to create directory: " + externalSplit[1]);
                        return;
                    }
                }
                if(file.createNewFile()){
                    if(cutil.exportResource(PurrBotMain.class.getResourceAsStream(internalPath), externalPath)){
                        System.out.println("[INFO] " + name + " successfully created!");

                    }else{
                        System.out.println("[WARN] Failed to create " + name);
                    }
                }
            }else{
                System.out.println("[INFO] " + name + " successfully loaded!");
                gFiles.put(name, file);
            }
        }catch (Exception ignored){
        }
    }

    public String getItem(String fileName, String item){
        File file = gFiles.get(fileName);
        try{
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            Map<String, String> data = gson.fromJson(reader, LinkedTreeMap.class);
            if (data.containsKey(item)){
                return data.get(item);
            }
        }catch (Exception ignored){
        }
        return item + " not found in " + fileName;
    }
}
