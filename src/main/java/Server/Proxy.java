package Server;

import java.io.*;
import java.net.Socket;

public class Proxy implements Runnable
{
    private ServerC sc;
    private Socket s;

    private BufferedReader reader;
    private PrintWriter writer ;

    private String nkname;

    public String getNickname()
    {
        return nkname;
    }

    public Proxy(ServerC sc, Socket s)
    {
        this.sc = sc;
        this.s = s;

        try
        {
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(s.getOutputStream());

            new Thread(this).start();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void run() // listen on new messages
    {
        getMessage();

    }

    public String getMessage()
    {
        String messageIn = null;

        try
        {
            while((messageIn = reader.readLine()) != null)
            {
                sc.broadcast(messageIn);
                //return messageIn;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        return messageIn;
    }

    public void send(String messageOut) // sends to client
    {
        writer.println(messageOut);
        writer.flush();
    }
}
