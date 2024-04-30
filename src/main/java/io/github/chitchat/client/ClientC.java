package io.github.chitchat.client;

import java.io.*;
import java.net.Socket;

public class ClientC implements Runnable
{
    private Socket s;
    private BufferedReader reader;
    //private PrintWriter writer;
    private ObjectOutputStream writer;

    public ClientC()
    {
        try
        {
            s = new Socket("localhost", 7777);

            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //writer = new PrintWriter(s.getOutputStream());
            writer = new ObjectOutputStream(s.getOutputStream());

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        new Thread(this).start();
        listenUInput.start();
    }


    public void run()
    {
        String messageIn = null;

        try
        {
            while((messageIn = reader.readLine()) != null)
            {
                System.out.println(s.getInetAddress() + ": " + messageIn);
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private Thread listenUInput = new Thread()
    {
        private String message = null;
        private BufferedReader conReader = new BufferedReader(new InputStreamReader(System.in));

        public void run()
        {
            try
            {
                while((message = conReader.readLine()) != null)
                {
                    send(message);
                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    };

    public void send(String messageOut)
    {
        //writer.println(messageOut);
        //writer.flush();

        try
        {
            writer.writeObject(messageOut);
            writer.flush();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
