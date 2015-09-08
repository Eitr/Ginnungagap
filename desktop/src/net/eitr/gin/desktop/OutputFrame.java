package net.eitr.gin.desktop;

import java.awt.BorderLayout;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class OutputFrame extends JFrame {

	private JPanel contentPane;
	
	public OutputFrame(String title) {
		setVisible(true);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JTextArea txtrOutput = new JTextArea();
		scrollPane.setViewportView(txtrOutput);
		txtrOutput.setEditable(false);
		txtrOutput.setVisible(true);
		scrollPane.setVisible(true);
		
		System.setOut(new PrintStream(new StreamCapturer(txtrOutput, System.out)));
		System.setErr(new PrintStream(new StreamCapturer(txtrOutput, System.err)));
		
		validate();
	}

}
