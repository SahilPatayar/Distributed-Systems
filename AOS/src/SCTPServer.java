import java.net.*;
import com.sun.nio.sctp.*;
import java.nio.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SCTPServer implements Runnable {
	public static final int MESSAGE_SIZE = 1024;
	public String host;
	public int port;
	public static File file;
	public static String name;

	/*
	 * Constructor for creating the Sctp Server
	 */
	public SCTPServer(String host, int port) {
		this.host = host; // host name of the machine
		this.port = port; // port number to run server socket

		name = "process_" + Project1.getProcessId() + ".txt"; // creating name to
															// store information
															// in file

		Thread t = new Thread(this, "server"); // thread which will be listening
												// for requests
		t.start(); // starting the thread
	}

	public static String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			int count = Project1.noOfProcess - 1;

			ByteBuffer byteBuffer = ByteBuffer.allocate(MESSAGE_SIZE);
			String message = "";
			// Open a server channel
			SctpServerChannel sctpServerChannel = SctpServerChannel.open();

			InetSocketAddress serverAddr = new InetSocketAddress(host, port);

			System.out.println("Server Address : " + serverAddr.getPort()
					+ " Server Name : " + serverAddr.getHostName()
					+ " is running.\n");
			sctpServerChannel.bind(serverAddr);
			// Server goes into a permanent loop accepting connections from
			// clients

			writeToFile(SCTPServer.getName(),
					"----------------------------------------------", false);

			while (true) {

				System.out.println("Count " + count);

				if (count <= 0) {
					// System.out.println("In Count condition  ");
					Thread.sleep(20000);
					System.out.println("Node " + Project1.getProcessId()
							+ " has final vector : "
							+ FidgeMattern.vectorToString(Project1.getVector()));
					writeToFile(SCTPServer.getName(),
							"-------------------------------------------\n",
							true);
					writeToFile(
							SCTPServer.getName(),
							"Node "
									+ Project1.getProcessId()
									+ " has final vector : "
									+ FidgeMattern.vectorToString(Project1
											.getVector()) + "\n", true);
					writeToFile(SCTPServer.getName(),
							"-------------------------------------------\n",
							true);
					break;
				}

				System.out.println("In Waiting State : ");

				SctpChannel sctpChannel = sctpServerChannel.accept();

				MessageInfo messageInfo = sctpChannel.receive(byteBuffer, null,
						null);

				// retrieving the old vector
				int[] mes = Project1.getVector();

				System.out.println("Node " + Project1.getProcessId()
						+ " has vector before receiving  message : "
						+ FidgeMattern.vectorToString(mes) + "\n");

				message = byteToString(byteBuffer);

				writeToFile(SCTPServer.getName(), "Node " + Project1.getProcessId()
						+ " has vector before receiving  message : "
						+ FidgeMattern.vectorToString(mes), true);

				System.out.println("Message Recieved from  "
						+ sctpChannel.getRemoteAddresses() + " : " + message);

				writeToFile(SCTPServer.getName(), "Message Recieved from "
						+ sctpChannel.getRemoteAddresses() + " : " + message,
						true);

				FidgeMattern protocol = new FidgeMattern(message);

				int[] vector = FidgeMattern.compareVectors(Project1.getVector(),
						protocol.newV, Project1.getProcessId());

				Project1.setVector(vector);

				System.out.println("Node " + Project1.getProcessId()
						+ " with updated vector after receiving : "
						+ FidgeMattern.vectorToString(Project1.getVector()));

				writeToFile(SCTPServer.getName(), "Node " + Project1.getProcessId()
						+ " with updated vector after receiving : "
						+ FidgeMattern.vectorToString(Project1.getVector()), true);

				writeToFile(SCTPServer.getName(),
						"----------------------------------------------", true);

				count--;

				byteBuffer.clear();

				sctpChannel.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Writing the server info to file
	 */

	public void writeToFile(String path, String message, boolean flag) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path,
					flag));

			if (!flag) {
				writer.write(message);
				writer.newLine();
				writer.close();
			} else {
				writer.append(message);
				writer.newLine();
				writer.close();
			}

		} catch (Exception e) {
			System.out.println("Error in Writing File. ");
			e.printStackTrace();
		}
	}

	/*
	 * Converting buffer message to string
	 */
	public String byteToString(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
		byteBuffer.limit(MESSAGE_SIZE);
		byte[] bufArr = new byte[byteBuffer.remaining()];
		byteBuffer.get(bufArr);
		return new String(bufArr);
	}
}
