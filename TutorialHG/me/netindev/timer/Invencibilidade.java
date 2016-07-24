package me.netindev.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.netindev.Main;
import me.netindev.manager.Manager;

public class Invencibilidade {

	public static BukkitTask task;
	public static int tempo;

	public Invencibilidade() {
		tempo = 121;
		task = new BukkitRunnable() {
			public void run() {
				--tempo;
				for (Player jogadores : Bukkit.getOnlinePlayers()) {
					Manager.scoreboardInvencibilidade(jogadores);
				}
				if (tempo % 20 == 0 && tempo > 0 && tempo < 121) {
					Bukkit.broadcastMessage("> §cEncerrando invencibilidade em: " + tempo + " segundos.");
				} else if (tempo < 6 && tempo > 1) {
					Bukkit.broadcastMessage("> §cEncerrando invencibilidade em: " + tempo + " segundos.");
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 1.0F);
					}
				} else if (tempo == 1) {
					Bukkit.broadcastMessage("> §cEncerrando invencibilidade em: " + tempo + " segundo.");
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 1.0F);
					}
				} else if (tempo == 0) {
					if (Manager.jogadores.size() > 0) {
						Manager.iniciarJogo();
						cancel();
					} else {
						Main.plugin.getServer().getLogger().info("/ Jogadores retorna 0, reiniciando partida.");
						Bukkit.shutdown();
					}
				}
			}
		}.runTaskTimer(Main.plugin, 0L, 20L);
	}

}
