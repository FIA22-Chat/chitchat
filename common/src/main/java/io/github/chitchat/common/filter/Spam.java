package io.github.chitchat.common.filter;
import java.util.HashMap;
import java.util.Map;

public class Spam {

    private static final int time = 60;
    private static final int max_Msg_Normal = 3;
    private static final int max_Msg_Priv = 30;
    private static int offenseCount = 0;
    private static final int punishTime = 5;
    private Map<String, Integer> msgCount;
    private Map<String, Long> lastMsgTimes;

    public Spam()
    {
        msgCount = new HashMap<>();
        lastMsgTimes = new HashMap<>();
    }

    public boolean isUserSpamming(String userid)
    {
        long currentTime = System.currentTimeMillis() /1000;

        cleanOutdatedMessage(currentTime);

        int currentCount = msgCount.getOrDefault(userid, 0) +1;
        msgCount.put(userid,currentCount);
        lastMsgTimes.put(userid,currentTime);


        return currentTime > max_Msg_Normal;
    }

    public boolean isAdminSpamming(String userid)
    {
        long currentTime = System.currentTimeMillis() /1000;

        cleanOutdatedMessage(currentTime);

        int currentCount = msgCount.getOrDefault(userid, 0) +1;
        msgCount.put(userid,currentCount);
        lastMsgTimes.put(userid,currentTime);
        //if user is spamming disable the textfield
        return currentTime > max_Msg_Priv;
    }

    public boolean handleSpam(boolean spamming) {
        if (!spamming) {
            return false;
        }

        offenseCount++;

        int timeoutDuration = punishTime * offenseCount * 1000;

//         SwingUtilities.invokeLater(() -> textField.setEnabled(false));

        new Thread(() -> {
            try {
                Thread.sleep(timeoutDuration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }  {
//                SwingUtilities.invokeLater(() -> textField.setEnabled(true));
            }
        }).start();

        return true;
    }


    public void cleanOutdatedMessage(long currentTime)
    {
        long windowStart = currentTime - time;
        for(String userid: lastMsgTimes.keySet())
        {
            long lastMessageTime = lastMsgTimes.get(userid);
            if(lastMessageTime<windowStart)
            {
                msgCount.remove(userid);
                lastMsgTimes.remove(userid);
            }

        }

    }
}
