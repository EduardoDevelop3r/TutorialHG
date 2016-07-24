package me.netindev;

import java.io.File;
import me.netindev.listener.Eventos;
import me.netindev.manager.Manager;
import me.netindev.utils.Comandos;
import me.netindev.utils.Estado;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Plugin plugin;
	public static Estado estado;

	public void onLoad() {
		plugin = this;
		deletarMundo(new File("world"));
	}

	public void onEnable() {
		Manager.iniciarServidor();
		registrarComandos();
		registrarEventos();
		getLogger().info("/ Plugin ativado.");
	}

	private void registrarEventos() {
		PluginManager e = getServer().getPluginManager();
		e.registerEvents(new Eventos(), this);
	}

	private void registrarComandos() {
		getCommand("feast").setExecutor(new Comandos());
		getCommand("iniciar").setExecutor(new Comandos());
		getCommand("kit").setExecutor(new Comandos());
		getCommand("tempo").setExecutor(new Comandos());
	}

	private void deletarMundo(File arquivo) {
		if (arquivo.isDirectory()) {
			String[] lista = arquivo.list();
			for (int i = 0; i < lista.length; i++) 
				deletarMundo(new File(arquivo, lista[i]));
		}
		arquivo.delete();
	}
}
