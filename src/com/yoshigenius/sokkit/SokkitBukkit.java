package com.yoshigenius.sokkit;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.yoshigenius.sokkit.SokkitAPI.SokkitHandler;

public class SokkitBukkit extends JavaPlugin implements SokkitHandler {
    
    @Override
    public void onEnable() {
        if ( !new File( this.getDataFolder(), "config.yml" ).exists() ) {
            this.saveDefaultConfig();
        }
        boolean disabled = false;
        String sport = this.getConfig().getString( "port", "8080" );
        try {
            int port = Integer.parseInt( sport );
            if ( port < 0 || port > 0xFFFF ) {
                this.getLogger().severe( "Port supplied in config (" + sport + ") must be between 0 and 0xFFFF." );
                disabled = true;
            }
        } catch ( Exception ex ) {
            this.getLogger().severe( "Port supplied in config (" + sport + ") is not an integer." );
            disabled = true;
        }
        if ( this.getAPIKey().isEmpty() || this.getAPIKey().equals( "NOT SET" ) ) {
            this.getLogger().severe( this.getAPIKey() + " is not a valid API key." );
            disabled = true;
        }
        if ( this.getAllowedIPs() == null || this.getAllowedIPs().isEmpty() ) {
            this.getLogger().severe( "There are no allowed IPs set." );
            disabled = true;
        }
        if ( disabled ) {
            this.getServer().getPluginManager().disablePlugin( this );
            return;
        }
        SokkitAPI.handler = this;
    }
    
    @Override
    public void onDisable() {
        for ( SokkitAPI api : SokkitAPI.instances.values() ) {
            if ( api.getServerSocket() != null ) {
                try {
                    api.getServerSocket().getSocket().close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public String getAPIKey() {
        return this.getConfig().getString( "apikey", "NOT SET" );
    }
    
    @Override
    public List<String> getAllowedIPs() {
        List<String> ips = this.getConfig().getStringList( "allowed-ips" );
        return ips == null ? new LinkedList<String>() : ips;
    }
    
    @Override
    public boolean isDisabled() {
        return false;
    }
    
}
