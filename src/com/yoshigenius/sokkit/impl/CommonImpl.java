package com.yoshigenius.sokkit.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.yoshigenius.sokkit.SokkitAPI;
import com.yoshigenius.sokkit.SokkitProtocol;
import com.yoshigenius.sokkit.api.SokkitChannel;
import com.yoshigenius.sokkit.api.SokkitMessage;

public final class CommonImpl {
    
    protected static SokkitMessage getSM( BufferedReader reader ) {
        try {
            if ( reader.readLine().equals( SokkitProtocol.SIG_BEGIN_SOKKIT ) ) { // check this is a Sokkit message
                if ( reader.readLine().equals( SokkitProtocol.PROTOCOL_STRING ) ) { // check we're on the same protocol version
                    SokkitChannel channel = SokkitChannel.get( reader.readLine() ); // get the SokkitChannel from the given name
                    if ( channel != null && SokkitAPI.getInstance( channel ) != null ) {
                        boolean badAuth = false;
                        if ( channel.getAuth() != null ) {
                            String username = reader.readLine();
                            String password = reader.readLine();
                            if ( !( channel.getAuth().getUsername().equals( username ) && channel.getAuth()
                                    .getPassword().equals( password ) ) ) {
                                badAuth = true;
                            } else {
                                if ( channel.getAuth().requireSalt() ) {
                                    String salt = reader.readLine();
                                    if ( !channel.getAuth().getSalt().equals( salt ) ) {
                                        badAuth = true;
                                    }
                                }
                            }
                        }
                        if ( badAuth ) {
                            return SokkitMessage.BAD_AUTH;
                        } else {
                            List<String> lines = new LinkedList<>();
                            String line;
                            while ( ( line = reader.readLine() ) != null
                                    && !line.equals( SokkitProtocol.SIG_END_SOKKIT ) && !line.isEmpty() ) {
                                lines.add( line );
                            }
                            if ( line.equals( SokkitProtocol.SIG_END_SOKKIT ) ) {
                                SokkitMessage message = new SokkitMessage( channel, lines.toArray() );
                                return message;
                            }
                            
                        }
                    }
                }
            }
        } catch ( IOException ex ) {
        }
        return null;
    }
    
}
