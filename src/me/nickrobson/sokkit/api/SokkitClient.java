package me.nickrobson.sokkit.api;

import java.net.Socket;

public interface SokkitClient {
    
    public SokkitMessage send( SokkitMessage message, Socket server );
    
}
