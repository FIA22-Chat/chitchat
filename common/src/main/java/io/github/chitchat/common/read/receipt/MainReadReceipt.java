package io.github.chitchat.common.read.receipt;

import java.util.ArrayList;

public class MainReadReceipt
{
    private String roomType = "";
    private ReadReceiptGroup rrg;
    private ReadReceiptSingle rrs;

    private User user;
    private Group group;

    //OnClick aus GUI beim Klick auf Chatraum. Aufruf von checkStatus mit Übergabe von roomID und userID
    private void checkRoomType()
    {
        if(roomType == "single")
        {
            rrg.checkStatusSingle(int userID, int roomID);
        }
        else if (roomType == "group")
        {
            rrs.checkStatusGroup(ArrayList<User> userList, int roomID);
        }
    }
}
