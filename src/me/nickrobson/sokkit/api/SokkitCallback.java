package me.nickrobson.sokkit.api;

public interface SokkitCallback {
    
    // receiving a Sokkit message request from a Sokkit client
    // return a message to send back to the client
    public void onServerReceive( SokkitMessage message );
    
    // sending a Sokkit message response to a Sokkit client
    public SokkitMessage getServerReply( SokkitMessage message );
    
    // sending a Sokkit message request to a Sokkit server
    public void onClientSend( SokkitMessage message );
    
    // receiving a Sokkit message response from a Sokkit server
    public void onClientReceive( SokkitMessage message );
    
}
