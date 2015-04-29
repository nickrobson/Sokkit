package com.yoshigenius.sokkit;

/**
 * This class does nothing; it's just here to describe the protocol.
 * 
 * @author fusion
 */
public final class SokkitProtocol {
    
    public static final int PROTOCOL = 1;
    public static final String PROTOCOL_STRING = Integer.toString( SokkitProtocol.PROTOCOL );
    
    public static final String SIG_END_SOKKIT = "SIG_END_SOKKIT";
    public static final String SIG_BEGIN_SOKKIT = "SIG_BEGIN_SOKKIT";
    
    public static final String BAD_AUTHENTICATION = "Bad Authentication";
    
    /* +======================+
     * |= PROTOCOL VERSION 1 =|
     * +======================+
     * 
     * -> SIG_BEGIN_SOKKIT
     * -> Protocol Name/Version
     * -> ChannelName
     * If Auth {
     *     -> Username
     *     -> Password
     *     If Salt {
     *         -> Salt
     *     }
     * }
     * -> Message
     * -> SIG_END_SOKKIT
     * 
     */
    
}
