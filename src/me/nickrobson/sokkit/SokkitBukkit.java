package me.nickrobson.sokkit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.nickrobson.sokkit.SokkitAPI.SokkitHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class SokkitBukkit extends JavaPlugin implements SokkitHandler {
    
    @Override
    public void onEnable() {
        if ( !new File( this.getDataFolder(), "config.yml" ).exists() ) {
            this.saveDefaultConfig();
        }
        boolean disabled = false;
        int port = this.getConfig().getInt( "port", 25600 );
        try {
            if ( port < 0 || port > 0xFFFF ) {
                this.getLogger().severe( "Port supplied in config (" + port + ") must be between 0 and 0xFFFF." );
                disabled = true;
            }
        } catch ( Exception ex ) {
            this.getLogger().severe( "Port supplied in config (" + port + ") is not an integer." );
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
    public List<String> getAllowedIPs() {
        List<String> ips = this.getConfig().getStringList( "allowed-ips" );
        return ips == null ? SokkitAPI.ALL_IPS_ALLOWED : ips;
    }
    
    @Override
    public boolean isDisabled() {
        return false;
    }
    
}
