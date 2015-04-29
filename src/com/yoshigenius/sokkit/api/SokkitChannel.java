package com.yoshigenius.sokkit.api;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public final class SokkitChannel {
    
    private static Map<String, SokkitChannel> channels = new HashMap<>();
    
    public static SokkitChannel get( String name ) {
        return SokkitChannel.channels.get( name );
    }
    
    public static SokkitChannel createNoAuth( String name ) {
        return SokkitChannel.create( name, null );
    }
    
    public static SokkitChannel create( String name, SokkitAuth auth ) {
        SokkitChannel channel = new SokkitChannel( name, auth );
        if ( SokkitChannel.channels.containsKey( name ) ) {
            return null;
        }
        SokkitChannel.channels.put( name, channel );
        return channel;
    }
    
    private final String name;
    private final SokkitAuth auth;
    
    private SokkitChannel( String name, SokkitAuth auth ) {
        this.name = name;
        this.auth = auth;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SokkitAuth getAuth() {
        return this.auth;
    }
    
    public void print( PrintWriter writer ) {
        writer.println( this.getName() );
    }
    
}
