package me.nickrobson.sokkit.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import me.nickrobson.sokkit.SokkitAPI;
import me.nickrobson.sokkit.SokkitProtocol;
import me.nickrobson.sokkit.api.SokkitClient;
import me.nickrobson.sokkit.api.SokkitMessage;

public class SokkitClientImpl implements SokkitClient {
    
    private final Proxy proxy;
    
    public SokkitClientImpl( Proxy proxy ) {
        this.proxy = proxy;
    }
    
    public SokkitClientImpl() {
        this( null );
    }
    
    @Override
    public SokkitMessage send( SokkitMessage message, Socket server ) {
        try {
            
            Socket socket = this.proxy == null ? new Socket() : new Socket( this.proxy );
            
            socket.setSoTimeout( 1000 );
            socket.connect( new InetSocketAddress( server.getInetAddress(), server.getPort() ) );
            
            PrintWriter writer = new PrintWriter( socket.getOutputStream() );
            
            writer.println( SokkitProtocol.SIG_BEGIN_SOKKIT );
            
            writer.println( message.getChannel() );
            
            if ( message.getChannel().getAuth() != null ) {
                message.getChannel().getAuth().print( writer );
            }
            
            message.print( writer );
            
            writer.println( SokkitProtocol.SIG_END_SOKKIT );
            
            writer.flush();
            
            SokkitAPI.getInstance( message.getChannel() ).onClientSend( message );
            
            BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            
            SokkitMessage msg = CommonImpl.getSM( reader );
            
            if ( msg == SokkitMessage.BAD_AUTH ) {
                
            } else {
                SokkitAPI.getInstance( message.getChannel() ).onClientReceive( msg );
            }
            
            reader.close();
            writer.close();
            socket.close();
            
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }
}
