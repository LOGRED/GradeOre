package org.logsh.gradeore.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.logsh.gradeore.GradeOre;
import org.logsh.gradeore.Ore;

import java.util.Random;

public class BlockBreak implements Listener {

    final private Material[] ores = {Material.DIAMOND_ORE , Material.DEEPSLATE_DIAMOND_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
    Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
    Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
    Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE};

    final private int[] materialType = {4, 4, 1, 1, 0, 0, 2, 2, 3, 3};

    final private Material[] oreTypeNumber = {Material.COAL, Material.RAW_IRON, Material.RAW_GOLD, Material.EMERALD, Material.DIAMOND};


    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
//        /* OP면 제외 */
//        if(e.getPlayer().isOp() == true)
//            return;

        for(int i = 0; i < ores.length; i++) {
            if(e.getBlock().getType() == ores[i]) {
                e.setDropItems(false);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), getRandomItem(materialType[i]));
            }
        }
    }

    /* 광물을 캐면 나오는 등급있는 광물을 랜덤으로 나오게 해주는 함수 */
    public ItemStack getRandomItem(int type) {
        int value = 0;

        /* 랜덤 최대 값 */
        for(Ore ore : GradeOre.plugin.ore) {
            if(ore.getType() == type) {
                value += ore.getProbability();
            }
        }

        /* 최댓값이 없으면 제외 */
        if(value == 0) {
            return new ItemStack(oreTypeNumber[type], 1);
        }

        Random random = new Random();

        int ran =  random.nextInt(value), i = 0;

        /* 계산 */
        for(Ore ore : GradeOre.plugin.ore) {
            if(ore.getType() == type) {
                if (i < ran && ran < (ore.getProbability() + i)) {
                    return ore.getItemStack();
                } else {
                    i += ore.getProbability();
                }
            }
        }
        return null;
    }

}
