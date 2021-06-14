package com.google.ar.core.examples.java.augmentedfaces;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpClientHandler extends Thread{

    public Handler mHandler ;//= new Handler();

    FloatBuffer DataToSend;
    private boolean DataToProcessed=false;
    private AtomicBoolean UpToDate = new AtomicBoolean(false);
    public AtomicBoolean ConnectionRelevant = new AtomicBoolean(true);
    private TcpServerService Owner;

    private String name;
    final DataInputStream InStream;
    final DataOutputStream OStream;
    Socket S;
    boolean isloggedin;





    TcpClientHandler(Socket S, String name, DataInputStream InStream_, DataOutputStream OStream_)
    {
        this.InStream = InStream_;
        this.OStream = OStream_;
        this.name = name;
        this.S = S;
        this.isloggedin=true;

    }

    @Override
    public void run() {
        super.run();

        String received;

        while (ConnectionRelevant.get()) {


            int Id = 0;
            for (TcpClientHandler mc : TcpServerService.ar)
            {
                if ( mc.isloggedin==true && mc.name.equals(name))
                {
                    break;
                    /*
                    try {
                        mc.OStream.writeUTF(this.name+" : "+TcpServerService.DataString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                    */
                }
                Id+=1;
            }

            if(!TcpServerService.ar_DataSent.get(Id).get())
            {
                try {
                    OStream.write(TcpServerService.DataBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    ConnectionRelevant.set(false);
                    break;
                }
                TcpServerService.ar_DataSent.get(Id).set(true);
            }

        }

        try {
            this.InStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.OStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }
}
