package com.yoshigenius.sokkit.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.yoshigenius.sokkit.SokkitAPI;
import com.yoshigenius.sokkit.SokkitProtocol;
import com.yoshigenius.sokkit.api.SokkitAuth;
import com.yoshigenius.sokkit.api.SokkitChannel;
import com.yoshigenius.sokkit.api.SokkitMessage;
import com.yoshigenius.sokkit.api.SokkitServer;

public class SokkitServerImpl implements SokkitServer {
    
    private ServerSocket server;
    
    public SokkitServerImpl( ServerSocket server ) {
        this.server = server;
        
        Runnable bootstrapRunnable = ( ) -> {
            try {
                new WorkerThread( server.accept() ).start();
            } catch ( Exception ex ) {
            }
        };
        Thread bootstrapThread = new Thread( bootstrapRunnable );
        bootstrapThread.start();
    }
    
    @Override
    public ServerSocket getSocket() {
        return this.server;
    }
    
    public static class WorkerThread extends Thread {
        
        private Socket client;
        
        protected WorkerThread( Socket client ) {
            this.client = client;
        }
        
        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter( this.client.getOutputStream() );;
                BufferedReader reader = new BufferedReader( new InputStreamReader( this.client.getInputStream() ) );
                SokkitMessage message = CommonImpl.getSM( reader );
                SokkitAPI inst = SokkitAPI.getInstance( message.getChannel() );
                inst.onServerReceive( message );
                SokkitMessage send = inst.getServerReply( message );
                
                SokkitChannel sendch = send.getChannel();
                SokkitAuth auth = sendch.getAuth();
                
                writer.println( SokkitProtocol.SIG_BEGIN_SOKKIT );
                writer.println( SokkitProtocol.PROTOCOL_STRING );
                
                writer.println( sendch.getName() );
                
                if ( auth != null ) {
                    auth.print( writer );
                }
                
                send.print( writer );
                
                writer.println( SokkitProtocol.SIG_END_SOKKIT );
                reader.close();
                writer.close();
                this.client.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
    
}
