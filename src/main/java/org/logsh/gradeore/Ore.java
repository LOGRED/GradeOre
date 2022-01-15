package org.logsh.gradeore;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ore {
    private int type; // 0 --> 석탄, 1 --> 철, 2 --> 금, 3 --> 에메랄드, 4 --> 다이아몬드
    private String name; // 이름
    private int probability; // 확률

    final private Material[] material = {Material.COAL, Material.RAW_IRON, Material.RAW_GOLD, Material.EMERALD, Material.DIAMOND};


    public Ore(int type, String name, int probability) {
        this.type = type;
        this.name = name;
        this.probability = probability;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        item.setItemMeta(meta);

        return item;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getProbability() {
        return probability;
    }

    public Material getMaterial() {
        return material[type];
    }

}
