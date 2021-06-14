package com.google.ar.core.examples.java.augmentedfaces;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.FloatBuffer;

public class ServerMessage {
    FloatBuffer FB;
    ServerSocket Server;
    AugmentedFacesActivity ClientIDHolder;

    public ServerMessage()
    {

    }
    public ServerMessage(AugmentedFacesActivity Client_)
    {
        this.ClientIDHolder=Client_;
    }

    public ServerMessage(FloatBuffer FB_, ServerSocket Server_, AugmentedFacesActivity Client_)
    {
        this.FB=FB_;
        this.Server=Server_;
        this.ClientIDHolder=Client_;
    }
}
