package AwtSwing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.Random;

import javax.sound.sampled.*;
import java.io.File;

public class PairGame {
	static JPanel panelNorth; // Top View
	static JPanel panelCenter; // Game View
	static JLabel labelMessage;
	static JButton[] buttons = new JButton[16];
	static String[] images = { "animal01.png", "animal02.png", "animal03.png", "animal04.png", "animal05.png",
			"animal06.png", "animal07.png", "animal08.png", "animal01.png", "animal02.png", "animal03.png",
			"animal04.png", "animal05.png", "animal06.png", "animal07.png", "animal08.png" };

	static int openCount = 0; // Opened Card Count: 0,1,2
	static int buttonIndexSave1 = 0; // First Opened Card Index: 0-15
	static int buttonIndexSave2 = 0; // Second Opened Card Index: 0-15
	static Timer timer; // Timer
	static int tryCount = 0; // How many tried
	static int successCount = 0; // Bingo Count: 0-8

	static class MyFrame extends JFrame implements ActionListener {
		public MyFrame(String title) {
			super(title);
			this.setLayout(new BorderLayout());
			this.setSize(400, 500);
			this.setVisible(true);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			initUI(this); 
			mixCard();

			this.pack();
		}

		static void playSound(String filename) {
			File file = new File("./wav/" + filename);
			if (file.exists()) {
				try {
					AudioInputStream stream = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("File Not Found!");
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (openCount == 2) {
				return;
			}

			JButton btn = (JButton) e.getSource();
			int index = getButtonIndex(btn);
			btn.setIcon(changeImage(images[index]));

			openCount++;
			if (openCount == 1) {
				buttonIndexSave1 = index;
			} else if (openCount == 2) {
				buttonIndexSave2 = index;
				tryCount++;
				labelMessage.setText("Find Same Animals !!  " + "Try " + tryCount);

				boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
				if (isBingo == true) {
					playSound("success.wav");
					openCount = 0;
					successCount++;
					if (successCount == 8) {
						labelMessage.setText("Game Over  " + "Try " + tryCount);
					}
				} else {
					backToQuestion();
				}

			}
		}

		public void backToQuestion() {
			timer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					playSound("fail.wav");
					openCount = 0;
					buttons[buttonIndexSave1].setIcon(changeImage("question.png"));
					buttons[buttonIndexSave2].setIcon(changeImage("question.png"));
					timer.stop();
				}
			});
			timer.start();
		}

		public boolean checkCard(int index1, int index2) {
			if (index1 == index2) {
				return false;
			}
			if (images[index1].equals(images[index2])) {
				return true;
			} else {
				return false;
			}
		}

		public int getButtonIndex(JButton btn) {
			int index = 0;
			for (int i = 0; i < 16; i++) {
				if (buttons[i] == btn) {
					index = i;
				}
			}
			return index;
		}
	}

	static void mixCard() {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			int random = rand.nextInt(15) + 1;
			// swap
			String temp = images[0];
			images[0] = images[random];
			images[random] = temp;
		}

	}

	static void initUI(MyFrame myFrame) {
		panelNorth = new JPanel();
		panelNorth.setPreferredSize(new Dimension(400, 100));
		panelNorth.setBackground(Color.DARK_GRAY);
		labelMessage = new JLabel("Find Same Animals !!  " + "Try 0");
		labelMessage.setPreferredSize(new Dimension(400, 100));
		labelMessage.setForeground(Color.WHITE);
		labelMessage.setFont(new Font("CHERI", Font.BOLD, 25));
		labelMessage.setHorizontalAlignment(JLabel.CENTER);
		panelNorth.add(labelMessage);
		myFrame.add("North", panelNorth);

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(4, 4));
		panelCenter.setPreferredSize(new Dimension(400, 400));
		for (int i = 0; i < 16; i++) {
			buttons[i] = new JButton();
			buttons[i].setPreferredSize(new Dimension(100, 100));
			buttons[i].setIcon(changeImage("question.png"));
			buttons[i].addActionListener(myFrame);
			panelCenter.add(buttons[i]);
		}
		myFrame.add("Center", panelCenter);

	}

	static ImageIcon changeImage(String filename) {
		ImageIcon icon = new ImageIcon("./img/" + filename);
		Image originImage = icon.getImage();
		Image changedImage = originImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon icon_new = new ImageIcon(changedImage);
		return icon_new;

	}

	public static void main(String[] args) {
		new MyFrame("Pair Game");

	}

}
