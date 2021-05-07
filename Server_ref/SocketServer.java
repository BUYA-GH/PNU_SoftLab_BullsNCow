package teamp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) {
        int portNumber = 5555;

        try {
            System.out.println("서버를 시작합니다...");
            ServerSocket serverSocket = new ServerSocket(portNumber); //포트번호를 매개변수로 전달하면서 서버 소켓 열기
            System.out.println("포트 " + portNumber + "에서 요청 대기중...");

            while(true) {
                Socket socket = serverSocket.accept(); //클라이언트가 접근했을 때 accept() 메소드를 통해 클라이언트 소켓 객체 참조
                InetAddress clientHost = socket.getLocalAddress();
                int clientPort = socket.getPort();
                System.out.println("클라이언트 연결됨. 호스트 : " + clientHost + ", 포트 : " + clientPort);

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); //소켓의 입력 스트림 객체 참조
                Object obj = instream.readObject(); // 입력 스트림으로부터 Object 객체 가져오기
                System.out.println("클라이언트로부터 받은 데이터 : " + obj); // 가져온 객체 출력

                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 객체 참조
                outstream.writeObject(obj + " from server"); //출력 스트림에 응답 넣기
                outstream.flush(); // 출력
                socket.close(); //소켓 해제
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}