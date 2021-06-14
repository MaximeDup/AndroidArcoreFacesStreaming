package com.google.ar.core.examples.java.augmentedfaces;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpServerService extends Service {

    static String DataString = "";

    static byte[] DataBytes;

    static FloatBuffer DataToSend;

    // Vector to store active clients
    static Vector<TcpClientHandler> ar = new Vector<>();
    static Vector<AtomicBoolean> ar_DataSent = new Vector<>();
    static Vector<Socket> ar_Socket = new Vector<>();

    // counter for clients
    static int j = 0;

    boolean ClientDataUptoDate[]= new boolean[5];
    Socket ClientSockets[]= new Socket[5];
    TcpClientHandler ClientsThreads[]= new TcpClientHandler[5];


    private static final int TCP_SERVER_PORT = 9886;
    private ServerSocket SSocket = null;
    private AtomicBoolean Working = new AtomicBoolean(true);
    private Runnable Runn = new Runnable() {
        @Override
        public void run() {

            try
            {
                SSocket = new ServerSocket(TCP_SERVER_PORT);
                Socket s;
                while(true)
                {
                    s = SSocket.accept();

                    System.out.println("New client request received : " + s);

                    // obtain input and output streams
                   // InputStream INtream = s.getInputStream();
                   // OutputStream OStream = s.getOutputStream();

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Creating a new handler for this client...");

                    // Create a new handler object for handling this request.
                    TcpClientHandler mtch = new TcpClientHandler(s,"client " + j, dis, dos);

                    // Create a new Thread with this object.
                    Thread t = new Thread(mtch);

                    System.out.println("Adding this client to active client list");

                    // add this client to active clients list
                    ar.add(mtch);

                    AtomicBoolean SentLatestData = new AtomicBoolean(true);
                    ar_DataSent.add(SentLatestData);

                    ar_Socket.add(s);

                    // start the thread.
                    t.start();

                    // increment i for new client.
                    // i is used for naming only, and can be replaced
                    // by any naming scheme
                    j++;
                }

            }
            catch(IOException e)
            {

            }
            /*
            for(int i = 0;i<5; i+=1)
            {
                ClientDataUptoDate[i]=false;
                ClientSockets[i]=null;
                ClientsThreads[i]=null;

            }

            try{
                SSocket = new ServerSocket(TCP_SERVER_PORT);
                while (Working.get()) {
                    if (SSocket != null) {

                            for(int i=0;i<5;i+=1)
                            {


                                if(ClientSockets[i]==null)
                                {
                                    ClientSockets[i] = SSocket.accept();
                                    InputStream INtream = ClientSockets[i].getInputStream();
                                    OutputStream OStream = ClientSockets[i].getOutputStream();
                                    ClientsThreads[i] = new TcpClientHandler(INtream, OStream);
                                    Thread t = ClientsThreads[i];
                                    t.start();
                                }
                                else
                                {


                                    //TODO Heartbeat function to monitor client disconnections , stop appropriate client thread, and close client Socket, setting them both back to null
                                }
                            }





                    } else {
                       // Log.e(TAG, "Couldn't create ServerSocket!")
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                try {
                    for(int i=0;i<5;i+=1)
                    {
                        if(ClientSockets[i]!=null)
                            ClientSockets[i].close();
                    }

                } catch (IOException ex) {
                ex.printStackTrace();
            }
            }*/
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(Runn).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Working.set(false);
    }

    Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {


            for(int i = ar.size()-1;i>=0;i = i-1)
            {
                if(!ar.get(i).ConnectionRelevant.get())
                {
                    try {
                        ar_Socket.get(i).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ar_Socket.remove(i);
                    ar.remove(i);
                    ar_DataSent.remove(i);

                    System.out.println("removing closed socket from client list");
                }

            }




            // the idea here is that per client threads are running at a much higher frequency than we have updates
            // ideally we should verify that every client has already sent DataBytes and isn't currently reading it to avoid any race condition
            // therefore here we assume that all threads are up to date and aren't reading DataBytes
            Bundle ReceivedData = msg.getData();
            DataBytes = ReceivedData.getByteArray("data");

            for(AtomicBoolean bool :ar_DataSent)
            {
                bool.set(false);
            }

            super.handleMessage(msg);
        }
    }

}
