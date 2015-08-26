package net.eitr.gin.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.esotericsoftware.kryonet.*;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private Server server;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.start();
					server.bind(54555, 54777);
					output("Server started.");
				} catch (IOException e) {
					e.printStackTrace();
					output("ERROR: "+e.toString());
				}
			}
		});
		panel_1.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.stop();
				output("Server stopped.");
			}
		});
		panel_1.add(btnStop);

		textArea = new JTextArea();
		textArea.setEditable(false);
		contentPane.add(textArea, BorderLayout.CENTER);

		server = new Server();
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
//				if (object instanceof SomeRequest) {
//					SomeRequest request = (SomeRequest)object;
//					System.out.println(request.text);
//
//					SomeResponse response = new SomeResponse();
//					response.text = "Thanks";
//					connection.sendTCP(response);
//				}
			}
		});
		
		System.setOut(new PrintStream(new StreamCapturer(textArea, System.out)));
		System.setErr(new PrintStream(new StreamCapturer(textArea, System.err)));
	}
	
	private void output (String s) {
		textArea.append(s);
		textArea.append("\n");
	}

}
