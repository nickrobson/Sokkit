package com.yoshigenius.sokkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import net.md_5.bungee.api.plugin.Plugin;

import com.yoshigenius.sokkit.SokkitAPI.SokkitHandler;

public class SokkitBungee extends Plugin implements SokkitHandler {
    
    private String apikey;
    private List<String> allowedIPs = new LinkedList<>();
    
    private boolean disabled = false;
    
    @Override
    public void onEnable() {
        File file = new File( this.getDataFolder(), "config.cfg" );
        try {
            if ( file.exists() ) {
                BufferedReader reader = Files.newBufferedReader( file.toPath(), StandardCharsets.UTF_8 );
                String line = null;
                while ( ( line = reader.readLine() ) != null ) {
                    if ( line.startsWith( "apikey=" ) ) {
                        this.apikey = line.replaceFirst( "apikey=", "" );
                    } else if ( line.startsWith( "allowedip=" ) ) {
                        this.allowedIPs.add( line.replaceFirst( "allowedip=", "" ) );
                    }
                }
            }
        } catch ( IOException ignored ) {
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
        return this.apikey;
    }
    
    @Override
    public List<String> getAllowedIPs() {
        return this.allowedIPs;
    }
    
    @Override
    public boolean isDisabled() {
        return this.disabled;
    }
    
}
