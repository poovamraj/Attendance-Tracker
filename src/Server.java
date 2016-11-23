import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	public static void main(String[] args) {
		
		
		try {
			
			serverSocket = new ServerSocket(9998);
			

		} catch (IOException e) {
			System.out.println("Could not listen on port: 80");
		}

		System.out.println("Server started. Listening to the port 80");
		
        while(true)
        {
            try{
            clientSocket = serverSocket.accept();
            new Thread(new Concurrent(clientSocket)).start();
            }
            catch(IOException EX)
            {

            }
        
        }
    
	}
}