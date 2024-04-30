package io.github.chitchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerC implements Runnable
{
    private ServerSocket ss;
    private Proxy proxy;
    private ArrayList<Proxy> p = new ArrayList<Proxy>();

    public ServerC()
    {
        new Thread(this).start();
    }

    public void run() // accepts socket connection with proxy
    {
        try
        {
            ss = new ServerSocket(7777);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        while (true)
        {
            try
            {
                proxy = new Proxy(this, ss.accept());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            p.add(proxy);
        }
    }

    public void broadcast(String message) // broadcast message to all proxy
    {
        for(Proxy proxy : p)
        {
            proxy.send(message);
        }
    }
}
