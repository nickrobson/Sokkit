package me.nickrobson.sokkit.api;

import java.io.PrintWriter;

public final class SokkitAuth {
    
    private final String username, password, salt;
    
    public SokkitAuth( String username, String password, String salt ) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getSalt() {
        return this.salt;
    }
    
    public boolean requireSalt() {
        return this.salt != null;
    }
    
    public void print( PrintWriter writer ) {
        writer.println( this.getUsername() );
        writer.println( this.getPassword() );
        if ( this.requireSalt() ) {
            writer.println( this.getSalt() );
        }
    }
    
}
