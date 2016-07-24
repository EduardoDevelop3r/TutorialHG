package me.netindev.utils;

import java.util.Random;
import me.netindev.Main;
import me.netindev.manager.Manager;
import me.netindev.timer.Iniciando;
import me.netindev.timer.Invencibilidade;
import me.netindev.timer.Jogo;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player jogador = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("iniciar")) {
			if (jogador.hasPermission("cmd.iniciar")) {
				if (Main.estado == Estado.INICIANDO) {
					Iniciando.task.cancel();
					Manager.iniciarInvencibilidade();
				} else {
					jogador.sendMessage("�cTorneio j� iniciou.");
				}
			} else {
				jogador.sendMessage("�cSem permiss�o.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("feast")) {
			if (jogador.hasPermission("cmd.feast")) {
				if (Main.estado == Estado.JOGO) {
					if (!Feast.spawnou) {
						Feast.spawnou = true;
						int x = new Random().nextInt(80);
						int z = new Random().nextInt(80);
						int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
						Location random = new Location(Bukkit.getWorld("world"), x, y, z);
						Feast.prepararFeast(random);
					} else if (Feast.task != null) {
						Feast.tempo = 5;
					} else {
						jogador.sendMessage("�cFeast j� spawnou.");
					}
				} else {
					jogador.sendMessage("�cVoc� precisa est� em jogo.");
				}
			} else if (Feast.localFeast != null) {
				jogador.sendMessage("�aB�ssola apontando para o feast.");
				jogador.setCompassTarget(Feast.localFeast);
			} else {
				jogador.sendMessage("�cFeast ainda n�o spawnou.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("kit")) {
			if (args.length == 0) {
				jogador.sendMessage("�cUse: /kit [kit].");
			} else if (args.length == 1) {
				if (Main.estado != Estado.INICIANDO) {
					jogador.sendMessage("�cVoc� s� pode pegar kit no est�gio iniciando.");
				} else {
					boolean achou = false;
					for (int i = 0; i < Kits.values().length; i++) {
						if (args[0].equalsIgnoreCase(Kits.values()[i].toString())) {
							if (jogador.hasPermission("kit." + Kits.values()[i].toString().toLowerCase())) {
								Manager.setarKit(jogador, Kits.values()[i].toString().toLowerCase(),
										StringUtils.capitalise(Kits.values()[i].toString().toLowerCase()));
								jogador.sendMessage("�aVoc� pegou o kit: "
										+ StringUtils.capitalise(Kits.values()[i].toString().toLowerCase()) + ".");
								achou = true;
								break;
							}
							jogador.sendMessage("�cVoc� n�o tem permiss�o.");
							return true;
						}
					}
					if (!achou) {
						jogador.sendMessage("�cEsse kit n�o existe.");
						return true;
					}
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("tempo")) {
			if (jogador.hasPermission("cmd.tempo")) {
				if (args.length == 1) {
					if ((seInt(args[0])) && (!args[0].contains("-"))) {
						Integer tempo = Integer.parseInt(args[0]);
						if (Main.estado == Estado.INICIANDO) {
							Iniciando.tempo = tempo.intValue();
						} else if (Main.estado == Estado.INVENCIBILIDADE) {
							Invencibilidade.tempo = tempo.intValue();
						} else if (Main.estado == Estado.JOGO) {
							Jogo.tempo = tempo.intValue();
							Jogo.passou = 3600 - tempo;
						}
						jogador.sendMessage("�aVoc� trocou o tempo.");
					} else {
						jogador.sendMessage("�cVoc� n�o pode usar -.");
					}
				} else {
					jogador.sendMessage("�cUse: /tempo [tempo].");
				}
			} else {
				jogador.sendMessage("�cSem permiss�o.");
			}
		}
		return false;
	}

	private boolean seInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}
}
