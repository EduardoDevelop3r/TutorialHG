package me.netindev.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import me.netindev.Main;
import me.netindev.listener.Habilidades;
import me.netindev.timer.Iniciando;
import me.netindev.timer.Invencibilidade;
import me.netindev.timer.Jogo;
import me.netindev.utils.Estado;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public class Manager {
	public static HashMap<String, String> kit = new HashMap<String, String>();
	private static HashMap<String, String> display = new HashMap<String, String>();
	private static HashMap<String, String> cooldown = new HashMap<String, String>();
	public static HashMap<String, ItemStack> itens = new HashMap<String, ItemStack>();
	public static ArrayList<String> jogadores = new ArrayList<String>();
	public static ArrayList<String> espectadores = new ArrayList<String>();
	public static boolean ganhou = false;

	private static void setupItens() {
		itens.put("kangaroo", new ItemStack(Material.FIREWORK));
		itens.put("thor", new ItemStack(Material.WOOD_AXE));
	}

	public static String pegarKit(Player jogador) {
		return display.get(jogador.getName());
	}

	public static void setarKit(Player jogador, String nomeKit, String nomeDisplay) {
		kit.put(jogador.getName(), nomeKit);
		display.put(jogador.getName(), nomeDisplay);
	}

	public static void setarCooldown(Player jogador, final String nomeKit, int segundos) {
		cooldown.put(jogador.getName(), nomeKit);
		new BukkitRunnable() {
			public void run() {
				Manager.cooldown.remove(jogador.getName(), nomeKit);
			}
		}.runTaskLater(Main.plugin, 20 * segundos);
	}

	public static boolean comKit(Player jogador, String nomeKit) {
		if ((kit.get(jogador.getName()) != null) && ((kit.get(jogador.getName())).equals(nomeKit))) {
			return true;
		}
		return false;
	}

	public static boolean comCooldown(Player jogador, String nomeKit) {
		if ((cooldown.get(jogador.getName()) != null) && ((cooldown.get(jogador.getName())).equals(nomeKit))) {
			return true;
		}
		return false;
	}

	public static String stringTimer(int tempo) {
		int a = tempo / 60;
		int b = tempo % 60;
		String c = null;
		String d = null;
		if (a >= 10) {
			c = "" + a;
		} else {
			c = "0" + a;
		}
		if (b >= 10) {
			d = "" + b;
		} else {
			d = "0" + b;
		}
		return c + ":" + d;
	}

	public static void scoreboardIniciando(Player jogador) {
		Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = score.registerNewObjective("tutorial", "hg");
		obj.setDisplayName("§cIniciando: §f" + stringTimer(Iniciando.tempo));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score players = obj.getScore(Bukkit.getOfflinePlayer("§cJogadores:"));
		players.setScore(jogadores.size());
		jogador.setScoreboard(score);
	}

	public static void scoreboardInvencibilidade(Player jogador) {
		Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = score.registerNewObjective("tutorial", "hg");
		obj.setDisplayName("§cInvencibilidade: §f" + stringTimer(Invencibilidade.tempo));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score players = obj.getScore(Bukkit.getOfflinePlayer("§cJogadores:"));
		players.setScore(jogadores.size());
		jogador.setScoreboard(score);
	}

	public static void scoreboardJogo(Player jogador) {
		Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = score.registerNewObjective("tutorial", "hg");
		obj.setDisplayName("§cJogo: §f" + stringTimer(Jogo.passou));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score players = obj.getScore(Bukkit.getOfflinePlayer("§cJogadores:"));
		players.setScore(jogadores.size());
		jogador.setScoreboard(score);
	}

	public static void checarWinner() {
		if (jogadores.size() > 1) {
			return;
		}
		if (jogadores.size() == 1) {
			ganhou = true;
			Jogo.task.cancel();
			Player jogador = Bukkit.getPlayer(jogadores.get(0));
			jogador.setVelocity(new Vector().setY(2.1D));
			jogador.getInventory().clear();
			jogador.getInventory().setArmorContents(null);
			jogador.getInventory().addItem(new ItemStack(Material.MAP));
			new BukkitRunnable() {
				public void run() {
					Bukkit.broadcastMessage("§c" + jogador.getName() + " venceu a partida.");
				}
			}.runTaskTimer(Main.plugin, 0L, 80L);
			new BukkitRunnable() {
				public void run() {
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						if (jogadores.getName() != jogador.getName()) {
							jogadores.kickPlayer("§c" + jogador.getName() + " venceu a partida, reiniciando...");
						}
					}
					jogador.kickPlayer("§aVocê venceu a partida, reiniciando...");
					Bukkit.shutdown();
				}
			}.runTaskLater(Main.plugin, 400L);
		} else if (jogadores.size() == 0) {
			Bukkit.shutdown();
		}
	}

	public static void iniciarServidor() {
		Main.estado = Estado.INICIANDO;
		setupItens();
		new Iniciando();
		new BukkitRunnable() {
			public void run() {
				String[] mensagens = { "§aPlugin feito no tutorial do netin.",
						"§aCompre vip em: tutorialhg.buycraft.net" };
				Bukkit.broadcastMessage(mensagens[new Random().nextInt(mensagens.length)]);
			}
		}.runTaskTimer(Main.plugin, 0L, 800L);
	}

	public static void iniciarInvencibilidade() {
		Main.estado = Estado.INVENCIBILIDADE;
		Bukkit.broadcastMessage("§aTorneio iniciou, boa sorte a todos.");
		Main.plugin.getServer().getPluginManager().registerEvents(new Habilidades(), Main.plugin);
		for (Player jogadores : Bukkit.getOnlinePlayers()) {
			jogadores.setGameMode(GameMode.SURVIVAL);
			jogadores.setAllowFlight(false);
			jogadores.setExp(0.0F);
			jogadores.closeInventory();
			jogadores.getInventory().clear();
			jogadores.getInventory().setArmorContents(null);
			jogadores.getInventory().addItem(new ItemStack(Material.COMPASS));
			int x = new Random().nextInt(50);
			int z = new Random().nextInt(50);
			int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
			jogadores.teleport(new Location(Bukkit.getWorld("world"), x, y + 20, z));
			if (itens.containsKey(kit.get(jogadores.getName()))) {
				jogadores.getInventory().addItem(itens.get(kit.get(jogadores.getName())));
			}
		}
		new Invencibilidade();
	}

	public static void iniciarJogo() {
		Main.estado = Estado.JOGO;
		new Jogo();
	}
}
