package com.github.dairycode.udp2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpSender {
    public static void main(String[] args) throws Exception {

        DatagramSocket socket = new DatagramSocket(8888);

        // 准备数据: 控制台读取 System.in
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String data = reader.readLine();
            byte[] datas = data.getBytes();
            DatagramPacket packet = new DatagramPacket(datas, 0, datas.length, new InetSocketAddress("localhost", 6666));

            socket.send(packet);
            if (data.equals("bye")) {
                break;
            }
        }
        socket.close();
    }
}
