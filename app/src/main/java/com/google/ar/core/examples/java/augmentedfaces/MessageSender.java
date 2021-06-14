package com.google.ar.core.examples.java.augmentedfaces;

import android.os.AsyncTask;
import android.app.Service;
import android.os.Process;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;
import static android.os.Process.THREAD_PRIORITY_DEFAULT;
import static android.os.Process.THREAD_PRIORITY_DISPLAY;

public class MessageSender extends AsyncTask<ServerMessage,Void,Void>
{
    private static final int TCP_SERVER_PORT = 9886;
    Socket S;
    DataOutputStream dos;
    PrintWriter pw;

@Override
protected Void doInBackground(ServerMessage... FB)
{
    Process.setThreadPriority(THREAD_PRIORITY_DISPLAY /*+ THREAD_PRIORITY_MORE_FAVORABLE*/);
    AugmentedFacesActivity ClientHolder = FB[0].ClientIDHolder;

   // if(ClientHolder.ServerSoc.accept()==null)
   //     ClientHolder.ClientSocket=null;

    if(ClientHolder.ClientSocket==null) {
        try {
            ClientHolder.ClientSocket = ClientHolder.ServerSoc.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    if(ClientHolder.ClientSocket!=null)
    {

        ByteBuffer byteBuffer = ByteBuffer.allocate(468*3* 4);//468 vector : 3 x 32bits floats
        byteBuffer.asFloatBuffer().put(FB[0].FB);

        byte[] bytearray = byteBuffer.array();

       // ByteBuffer.allocate(468*3*4).asFloatBuffer(FB[0].FB);
        /*
        byte[] bytearray = new byte[10];
        bytearray[0]=0;
        bytearray[8]=1;
        */

        try {
            ClientHolder.ClientSocket.getOutputStream().write(bytearray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   // ClientHolder.ClientSet2.set(true);


    /*
    FloatBuffer FloatB = FB[0].FB.duplicate();
    ServerSocket ss = FB[0].Server;
    //FloatB.limit(468*3);//for whatever reason the buffer

    ByteBuffer byteBuffer = ByteBuffer.allocate(468*3* 4);//468 vector : 3 x 32bits floats
    byteBuffer.asFloatBuffer().put(FloatB);

    byte[] bytearray = byteBuffer.array();
*/
    /*
    try{
        //ServerSocket ss = new ServerSocket(TCP_SERVER_PORT);

        //ss.setSoTimeout(10000);
        //accept connections
        if(FB[0].Client != null)
        {
            //S = ss.accept();
            FB[0].Client.getOutputStream().write(bytearray);
            //S.close();
        }
        //ss.close();
        //S = ss.accept();

       // S= new Socket("192.168.1.10",7800);



        //pw = new PrintWriter(S.getOutputStream());
       // pw.write
    }catch(IOException e)
    {
        e.printStackTrace();
    }*/



    return null;
}
}


