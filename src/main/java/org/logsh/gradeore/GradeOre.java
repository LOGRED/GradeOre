package org.logsh.gradeore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.logsh.gradeore.event.BlockBreak;

import java.util.Vector;

public final class GradeOre extends JavaPlugin implements Listener, CommandExecutor {
    public static GradeOre plugin;
    public Vector<Ore> ore = new Vector<Ore>(); // 등급 광물 변수

    @Override
    public void onEnable() {
        getLogger().info("GradeOre 가 로드 되었습니다.");

        /* 이벤트 등록 */
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);

        /* 명령어 등록 */
        getCommand("Ore").setExecutor(this);

        /* 광물 추가 */
        loadOre();

        /* static plugin */
        plugin = this;
    }

    @Override
    public void onDisable() {
        getLogger().info("GradeOre 플러그인이 사용종료 되었습니다." );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("Ore")) {
            Player player = (Player) sender;
            if(args.length >= 1) {

                if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) { // 광물 만들기

                    /* 잘못된 명령어를 입력했을 때*/
                    if(!(args.length >= 4)) {
                        player.sendMessage("다시 입력해주세요. \n/Ore add <타입> <이름> <확률>");
                        return  true;
                    }

                    /* 정수 아닐 경우 제외 */
                    if(!isNum(args[1]) && !isNum(args[3])) {
                        player.sendMessage("다시 입력해주세요. \n/Ore add <타입> <이름> <확률>");
                        return true;
                    }

                    /* 없는 광물의 번호를 입력했을 때 제외 */
                    if(Integer.parseInt(args[1]) > 4) {
                        player.sendMessage("타입을 '0 ~ 4' 으로 입력해주세요 \nOre add <타입> <이름> <확률>");
                        return true;
                    }

                    /* 새로운 광물 등록 */
                    for(int i = 0; true ; i++) {
                        if(!getConfig().isConfigurationSection("Ores." + i)) {
                            getConfig().set("Ores." + i + ".Type" ,Integer.parseInt(args[1]));
                            getConfig().set("Ores." + i + ".Name", args[2]);
                            getConfig().set("Ores." + i + ".Probability" , Integer.parseInt(args[3]));
                            saveConfig();

                            loadOre();

                            player.sendMessage("등록완료");
                            return true;
                        }
                    }

                } else if(args[0].equalsIgnoreCase("remove")) { // 삭제 기능
                    /* 잘못 입력 했을 경우*/
                    if(!(args.length >= 2)) {
                        player.sendMessage("다시 입력해주세요. \n/Ore remove  <번호>");
                        return true;
                    }
                    /* 정수 아닐 경우 제외 */
                    if(!isNum(args[1])) {
                        player.sendMessage("정수를 입력해주세요 \n/Ore give <번호>");
                        return true;
                    }
                    /* 없는 광물의 번호를 입력했을 때 제외 */
                    if(Integer.parseInt(args[1]) >= ore.size()) {
                        player.sendMessage("존재하지않은 광물의 번호입니다.. \n'/Ore list' 로 다시 확인해주세요");
                        return true;
                    }

                    int target = Integer.parseInt(args[1]);

                    getConfig().set("Ores." + target + ".Type" , null);
                    getConfig().set("Ores." + target + ".Name", null);
                    getConfig().set("Ores." + target  + ".Probability" , null);
                    saveConfig();

                    loadOre();

                    player.sendMessage("삭제 완료");

                    return true;

                }  else if(args[0].equalsIgnoreCase("give")) { // 광물 받기
                    if(args.length >= 2) {

                        /* 정수 아닐 경우 제외 */
                        if(!isNum(args[1])) {
                            player.sendMessage("정수를 입력해주세요 \n/Ore give <번호>");
                            return true;
                        }

                        /* 없는 광물의 번호를 입력했을 때 제외 */
                        if(Integer.parseInt(args[1]) >= ore.size()) {
                            player.sendMessage("존재하지않은 광물의 번호입니다.. \n'/Ore list' 로 다시 확인해주세요");
                            return true;
                        }

                        player.getInventory().addItem(ore.get(Integer.parseInt(args[1])).getItemStack());
                    } else {
                        player.sendMessage("/Ore give <번호>");
                    }
                } else if(args[0].equalsIgnoreCase("list")) { // 광물 리스트
                    for(int i = 0; i < ore.size(); i++) {
                        player.sendMessage("[" + i + "] " + ore.get(i).getName());
                    }
                } else if(args[0].equalsIgnoreCase("reload")) { // 광물 리로드
                   loadOre();
                }
            }
        }
        return true;
    }

    /* 등급 광물 추가 함수 */
    public void loadOre() {
        ore.clear();

        for(int i = 0; true; i++) {
            /* 콘피그에 없으면 리턴 */
            if(!getConfig().isConfigurationSection("Ores." + i)) {
                getLogger().info(i + "개의 광물이 등록 되었습니다.");
                return;
            }

            int type = getConfig().getInt("Ores." + i + ".Type");
            String name = getConfig().getString("Ores." + i + ".Name");
            int probability = getConfig().getInt("Ores." + i + ".Probability");

            ore.add(new Ore(type, name, probability));
        }

    }

    /* String -> int 숫자 인지 확인 해주는 함수 */
    public boolean isNum(String str) {
        try{
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException nfe) {
            return false;
        }
    }

}
