package io.github.chitchat.common.read.receipt;

public class ReadReceiptSingle
{
    private void checkStatusSingle(int roomID, int userID)
    {
        //AUFRUF von DB Methode um herauslesen zu können, ob der USER bereits auf den Chat geklickt hat. Returnt BOOLEAN messageStatus Falls nicht:
        if(messageStatus != true)
        {
            //RUFE SCHREIB METHODE VON DB AUF UM WERT ZU ÄNDERN. NUTZEN VON USERID DES ANDEREN TEILNEHMERS
            //DBMETHODE(roomID, userID);

            //RUFE CHANGE METHODE VON GUI AUF UM HAKEN AUF BLAU ZU ÄNDERN
            //GUIMETHODE(roomID, userID);

        }
    }
}
