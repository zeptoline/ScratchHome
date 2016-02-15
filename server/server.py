# coding=utf-8

import socket
import threading
import time
import select


host = socket.gethostbyname('localhost')
port = 2016
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.settimeout(240)
sock.bind((host, port))
sock.listen(1)


while True:

    print ("Waiting for connection")
    conn, addr = sock.accept()



    while True :
        text = raw_input("Donnez le prochain envois : ");
        if(text =="end") :
            break;
        print(text+"\n")
        conn.send(text+"\n")

    conn.close()
