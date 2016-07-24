package me.netindev.timer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.netindev.Main;
import me.netindev.manager.Manager;
import me.netindev.utils.Feast;

public class Jogo {

	public static BukkitTask task;
	public static int tempo;
	public static int passou;

	public Jogo() {
		tempo = 3600;
		passou = 0;
		task = new BukkitRunnable() {
			public void run() {
				++passou;
				--tempo;
				Manager.checarWinner();
				for (Player jogadores : Bukkit.getOnlinePlayers()) {
					Manager.scoreboardJogo(jogadores);
				}
				if (tempo == 2500) {
					if (!Feast.spawnou) {
						final int x = new Random().nextInt(80), z = new Random().nextInt(80);
						final int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
						Feast.prepararFeast(new Location(Bukkit.getWorld("world"), x, y, z));
					}
				}
				if (tempo > 0 && tempo % 30 == 0 && tempo < 200) {
					Bukkit.broadcastMessage("> §cEncerrando torneio em: " + tempo + " segundos.");
				} else if (tempo < 6 && tempo > 1) {
					Bukkit.broadcastMessage("> §cEncerrando torneio em: " + tempo + " segundos.");
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 1.0F);
					}
				} else if (tempo == 1) {
					Bukkit.broadcastMessage("> §cEncerrando torneio em: " + tempo + " segundo.");
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 1.0F);
					}
				} else if (tempo == 0) {
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						jogadores.kickPlayer("§cNinguém ganhou, partida encerrada.");
					}
					Bukkit.shutdown();
				}
			}
		}.runTaskTimer(Main.plugin, 0L, 20L);
	}

}
