package me.netindev.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Seletor {
	public static void seletorKits(Player jogador) {
		Inventory inv = Bukkit.createInventory(jogador, 54, "§2Seletor de Kits:");
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, new ItemStack(Material.THIN_GLASS));
		}
		inv.setItem(4, criarItem(Material.ENDER_PEARL, "§aTutorialHG"));
		inv.addItem(criarItemPerm(jogador, Material.FIREWORK, "§aKangaroo"));
		inv.addItem(criarItemPerm(jogador, Material.WOOD_AXE, "§aThor"));
		for (ItemStack stack : inv.getContents()) {
			if (stack == null) {
				inv.setItem(inv.firstEmpty(), new ItemStack(Material.THIN_GLASS));
			}
		}
		jogador.openInventory(inv);
	}

	public static ItemStack criarItem(Material material, String display) {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(display);
		stack.setItemMeta(meta);
		return stack;
	}

	public static ItemStack criarItemPerm(Player jogador, Material material, String display) {
		if (jogador.hasPermission("kit." + display.toLowerCase().replace("§", ""))) {
			ItemStack stack = new ItemStack(material);
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName(display);
			stack.setItemMeta(meta);
			return stack;
		}
		return new ItemStack(Material.AIR);
	}
}
