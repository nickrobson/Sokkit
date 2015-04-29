package com.yoshigenius.sokkit.api;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.yoshigenius.lib.serializable.Serializable;
import com.yoshigenius.lib.serializable.Serializer;

public final class SokkitMessage {
    
    public static final SokkitMessage BAD_AUTH = new SokkitMessage( null );
    
    private SokkitChannel channel;
    private Object[] payload;
    
    public SokkitMessage( SokkitChannel channel, Object... payload ) {
        this.channel = channel;
        List<Object> temp = new ArrayList<>();
        for ( Object o : payload ) {
            if ( o instanceof String ) {
                Serializable s = Serializer.deserialize( o.toString() );
                if ( s != null ) {
                    temp.add( s );
                } else {
                    temp.add( o );
                }
            } else {
                temp.add( o );
            }
        }
        payload = temp.toArray();
    }
    
    public SokkitChannel getChannel() {
        return this.channel;
    }
    
    public Object[] getPayload() {
        return this.payload;
    }
    
    public String getPayloadString() {
        if ( this == SokkitMessage.BAD_AUTH ) {
            return null;
        }
        String s = "";
        if ( this.payload == null || this.payload.length == 0 ) {
            return s;
        }
        for ( Object element : this.payload ) {
            if ( !s.isEmpty() ) {
                s += " ";
            }
            if ( element instanceof Serializable ) {
                s += ( (Serializable) element ).serialize();
            } else {
                s += element.toString();
            }
        }
        return s;
    }
    
    public String[] getPayloadStrings() {
        if ( this == SokkitMessage.BAD_AUTH ) {
            return new String[] {};
        }
        return this.getPayloadString().split( " " );
    }
    
    public void print( PrintWriter writer ) {
        if ( this == SokkitMessage.BAD_AUTH ) {
            return;
        }
        for ( String s : this.getPayloadStrings() ) {
            writer.println( s );
        }
    }
    
}
