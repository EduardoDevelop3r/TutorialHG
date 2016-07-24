package me.netindev.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.netindev.Main;
import me.netindev.manager.Manager;

public class Feast {

	public static Location localFeast;
	public static boolean spawnou = false;
	private static ArrayList<Block> localBaus = new ArrayList<>();

	public static void prepararFeast(Location loc) {
		localFeast = loc.clone().add(0, 1, 0);
		for (int x = -20; x <= 20; x++) {
			for (int z = -20; z <= 20; z++) {
				Location feast = new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z);
				if (feast.distance(loc) <= 20) {
					retirarBlocos(feast.getBlock());
					feast.getBlock().setType(Material.GRASS);
				}
			}
		}
		Location[] baus = { loc.clone().add(1, 1, 1), loc.clone().add(-1, 1, -1), loc.clone().add(1, 1, -1),
				loc.clone().add(-1, 1, 1), loc.clone().add(-2, 1, 2), loc.clone().add(-2, 1, -2),
				loc.clone().add(-2, 1, 0), loc.clone().add(2, 1, 0), loc.clone().add(0, 1, -2),
				loc.clone().add(0, 1, 2), loc.clone().add(+2, 1, -2), loc.clone().add(2, 1, 2) };
		for (Location blocos : baus) {
			localBaus.add(blocos.getBlock());
		}
		new Feast();
	}

	private static void retirarBlocos(Block bloco) {
		Location loc = bloco.getLocation().add(0, 1, 0);
		Block remover = loc.getBlock();
		do {
			remover.setType(Material.AIR);
			loc.setY(loc.getY() + 1);
			remover = loc.getBlock();
		} while (loc.getY() < loc.getWorld().getMaxHeight());
	}

	public static BukkitTask task;
	public static int tempo;

	public Feast() {
		tempo = 301;
		task = new BukkitRunnable() {
			public void run() {
				--tempo;
				if (tempo % 60 == 0 && tempo > 0) {
					Bukkit.broadcastMessage("§aFeast irá spawnar em: " + Manager.stringTimer(tempo) + ", em: " + "("
							+ localFeast.getBlockX() + ", " + localFeast.getBlockY() + ", " + localFeast.getBlockZ()
							+ ").");
				}
				if (tempo < 5 && tempo > 0) {
					Bukkit.broadcastMessage("§aFeast irá spawnar em: " + Manager.stringTimer(tempo) + ", em: " + "("
							+ localFeast.getBlockX() + ", " + localFeast.getBlockY() + ", " + localFeast.getBlockZ()
							+ ").");
				}
				if (tempo == 0) {
					Bukkit.broadcastMessage("§aFeast spawnou em: " + "(" + localFeast.getBlockX() + ", "
							+ localFeast.getBlockY() + ", " + localFeast.getBlockZ() + ").");
					spawnarFeast();
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0L, 20L);
	}

	@SuppressWarnings("deprecation")
	public static void spawnarFeast() {
		task = null;
		localFeast.getBlock().setType(Material.ENCHANTMENT_TABLE);
		for (Block baus : localBaus) {
			localFeast.getBlock().setType(Material.ENCHANTMENT_TABLE); // TODO pra q isso?
			baus.setType(Material.CHEST);
			Chest bau = (Chest) baus.getState();
			int[] itens = { 310, 311, 312, 313, 276, 261, 262, 278, 279, 322, 282, 326, 327 };
			for (int item : itens) {
				if (new Random().nextInt(100) < 13) {
					bau.getInventory().setItem(new Random().nextInt(27), new ItemStack(item));
				}
			}
		}
	}
}
