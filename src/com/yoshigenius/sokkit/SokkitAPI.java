package com.yoshigenius.sokkit;

import java.io.IOException;
import java.net.Proxy;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.yoshigenius.sokkit.api.SokkitCallback;
import com.yoshigenius.sokkit.api.SokkitChannel;
import com.yoshigenius.sokkit.api.SokkitClient;
import com.yoshigenius.sokkit.api.SokkitMessage;
import com.yoshigenius.sokkit.api.SokkitServer;
import com.yoshigenius.sokkit.impl.SokkitClientImpl;
import com.yoshigenius.sokkit.impl.SokkitServerImpl;

public final class SokkitAPI {
    
    public static final List<String> ALL_IPS_ALLOWED = ImmutableList.<String> of();
    
    protected static interface SokkitHandler {
        
        String getAPIKey();
        
        default List<String> getAllowedIPs() {
            return SokkitAPI.ALL_IPS_ALLOWED;
        }
        
        boolean isDisabled();
        
        Logger getLogger();
        
    }
    
    protected static SokkitHandler handler;
    protected static Map<SokkitChannel, SokkitAPI> instances = new HashMap<>();
    
    public static SokkitAPI getInstance( SokkitChannel channel ) {
        if ( channel == null ) {
            return null;
        }
        return SokkitAPI.instances.get( channel );
    }
    
    public static SokkitAPI createInstance( SokkitChannel channel ) {
        if ( SokkitAPI.getInstance( channel ) != null ) {
            return null;
        }
        SokkitAPI.instances.put( channel, new SokkitAPI( channel ) );
        return SokkitAPI.getInstance( channel );
    }
    
    private SokkitServer server;
    private SokkitClient client;
    private SokkitChannel channel;
    private SokkitCallback callback;
    
    private SokkitAPI( SokkitChannel channel ) {
        this.channel = channel;
    }
    
    public SokkitChannel getChannel() {
        return this.channel;
    }
    
    public SokkitServer setupServerSocket( int port ) {
        if ( SokkitAPI.handler.isDisabled() ) {
            return null;
        }
        if ( this.server != null ) {
            return this.server;
        }
        try {
            this.server = new SokkitServerImpl( new ServerSocket( port ) );
            return this.server;
        } catch ( IOException e ) {
            SokkitAPI.handler.getLogger().severe( "Failed to bind to port " + port + " : " + e.getMessage() );
            return null;
        }
    }
    
    public SokkitClient setupClientSocket() {
        return this.setupClientSocket( null );
    }
    
    public SokkitClient setupClientSocket( Proxy proxy ) {
        if ( SokkitAPI.handler.isDisabled() ) {
            return null;
        }
        if ( this.client != null ) {
            return this.client;
        }
        this.client = proxy == null ? new SokkitClientImpl() : new SokkitClientImpl( proxy );
        return this.client;
    }
    
    public void setCallback( SokkitCallback callback ) {
        this.callback = callback;
    }
    
    public SokkitServer getServerSocket() {
        if ( SokkitAPI.handler.isDisabled() ) {
            return null;
        }
        return this.server;
    }
    
    public SokkitClient getClientSocket() {
        if ( SokkitAPI.handler.isDisabled() ) {
            return null;
        }
        return this.client;
    }
    
    public void onServerReceive( SokkitMessage message ) {
        if ( this.callback != null ) {
            this.callback.onServerReceive( message );
        }
    }
    
    public SokkitMessage getServerReply( SokkitMessage message ) {
        if ( this.callback != null ) {
            return this.callback.getServerReply( message );
        }
        return null;
    }
    
    public void onClientSend( SokkitMessage message ) {
        if ( this.callback != null ) {
            this.callback.onClientSend( message );
        }
    }
    
    public void onClientReceive( SokkitMessage message ) {
        if ( this.callback != null ) {
            this.callback.onClientReceive( message );
        }
    }
    
}
