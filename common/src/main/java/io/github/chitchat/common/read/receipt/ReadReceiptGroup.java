package io.github.chitchat.common.read.receipt;

public class ReadReceiptGroup
{
    public void checkStatusGroup()
    {
        foreach(User user in userList)
        {
            //RUFE SCHREIB METHODE VON DB AUF UM WERT ZU ÄNDERN. NUTZEN VON USERID DES ANDEREN TEILNEHMERS
            //DBMETHODE(roomID, userID);

            //RUFE CHANGE METHODE VON GUI AUF UM HAKEN AUF BLAU ZU ÄNDERN
            //GUIMETHODE(roomID, userID);
        }
    }
}
