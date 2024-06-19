Hier kommen dann die jeweiligen Klassen etc. um die Lesebestätigung nutzen zu können.

Es könnte ein Char in der DB unter "Nachrichten" abgespeichert werden, welche folgende Kennungen nutzen können:
F = Failed
S = Send
R = Read

Je nachdem wie die Abfrage darüber stattindet, werden diese Chars bei der Zwischentabelle zwischen User und Nachrichten abgeändert.
z.B.
MessageID     UserSendID      UserReceiveID     State
Nachricht1      User1         User2         Lesezustand
