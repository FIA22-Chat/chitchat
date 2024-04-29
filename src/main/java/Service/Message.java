package Service;
public class Message
{
    private Message type;
    private String content;


    public Message(Message type, String content)
    {
        this.type = type;
        this.content = content;
    }

    public Message getType()
    {
        return this.type;
    }

    public String getContent()
    {
        return this.content;
    }
}
