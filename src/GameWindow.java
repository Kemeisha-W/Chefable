import javax.swing.*;            // need this for GUI objects
import java.awt.*;            // need this for certain AWT classes
import java.awt.event.*;
import java.io.IOException;

public class GameWindow extends JFrame implements
		KeyListener,
		MouseListener,
		ActionListener,
		MouseMotionListener
{


	private volatile boolean isRunning = false;        // used to stop the game thread


	private volatile boolean isOverQuitButton = false;

	private volatile boolean isOverPauseButton = false;
	private volatile boolean isPaused = false;

	private volatile boolean isOverStopButton = false;

	private volatile boolean isStopped = false;

	private GamePanel gamePanel;
	private JPanel mainPanel;


// declare buttons
private JButton pauseB;
	private JButton endB;
	private JButton startNewB;
	private JButton exitB;
	private SoundManager soundManager;

	public GameWindow() throws IOException {

		super("Chefable Project");
		setTitle ("Chefable Project");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize (screenSize.width, screenSize.height);


		// create mainPanel
		mainPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		GridLayout gridLayout;




		// create buttonPanel
		ImageIcon icon =new ImageIcon("Assets/Menu Buttons/Square Buttons/Square Buttons/Pause Square Button.png");
		Image i = icon.getImage();
		int width = screenSize.width/4;
		Image newimg = i.getScaledInstance(width-300, 50,  Image.SCALE_DEFAULT);
		icon = new ImageIcon(newimg);

		pauseB = new JButton (icon);
		pauseB.setOpaque(false);
		pauseB.setContentAreaFilled(false);
		pauseB.setBorderPainted(false);

		icon =new ImageIcon("Assets/Menu Buttons/Large Buttons/Large Buttons/Quit Button.png");
		i = icon.getImage();
		newimg = i.getScaledInstance(width-20, 50,  Image.SCALE_DEFAULT);
		icon = new ImageIcon(newimg);
		endB = new JButton (icon);
		endB.setOpaque(false);
		endB.setContentAreaFilled(false);
		endB.setBorderPainted(false);

		icon =new ImageIcon("Assets/Menu Buttons/Large Buttons/Large Buttons/New game Button.png");
		i = icon.getImage();
		newimg = i.getScaledInstance(width-20, 50,  Image.SCALE_DEFAULT);
		icon = new ImageIcon(newimg);
		startNewB = new JButton (icon);
		startNewB.setOpaque(false);
		startNewB.setContentAreaFilled(false);
		startNewB.setBorderPainted(false);

		icon =new ImageIcon("Assets/Menu Buttons/Large Buttons/Colored Large Buttons/Exit  col_Button.png");
		i = icon.getImage();
		newimg = i.getScaledInstance(width-20, 50,  Image.SCALE_DEFAULT);
		icon = new ImageIcon(newimg);

		exitB = new JButton (icon);
		exitB.setOpaque(false);
		exitB.setContentAreaFilled(false);
		exitB.setBorderPainted(false);


		// add listener to each button (same as the current object)
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		startNewB.addActionListener(this);
		exitB.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(1, 4);
		buttonPanel.setLayout(gridLayout);
		buttonPanel.setOpaque(false);

		// add buttons to buttonPanel
		buttonPanel.add (startNewB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (exitB);

		// create the gamePanel for game entities
		gamePanel = new GamePanel(100);

		// add sub-panels with GUI objects to mainPanel and set its colour
		mainPanel.add (buttonPanel,BorderLayout.SOUTH);
		mainPanel.add(gamePanel,BorderLayout.CENTER);

		//Add listeners
		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		ImageIcon imageIcon = new ImageIcon("Assets/Logo.png");
		Image image = imageIcon.getImage(); // transform it
		Image newImg = image.getScaledInstance(screenSize.width,(screenSize.height-buttonPanel.getHeight()),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		imageIcon = new ImageIcon(newImg);  // transform it back
		mainPanel.setBackground(new Color(110,186,197));
		gamePanel.setBackground(new Color(110,186,197));
		gamePanel.add(new JLabel(imageIcon));


		Container c = getContentPane();
		c.add(mainPanel);

		// set properties of window
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		soundManager = SoundManager.getInstance();
		soundManager.playSound("background2",true);

	}

	// implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (e.getSource() ==pauseB) {
			gamePanel.pauseGame();
			if (gamePanel.isPaused) {
				ImageIcon icon =new ImageIcon("Assets/Menu Buttons/Square Buttons/Square Buttons/Play Square Button.png");
				Image i = icon.getImage();
				int width = getWidth()/4;
				Image newimg = i.getScaledInstance(width-300, 50,  Image.SCALE_DEFAULT);
				icon = new ImageIcon(newimg);
				pauseB.setIcon(icon);
			}else
				pauseB.setText ("Pause Game");

		}

		if (e.getSource() == endB) {
			gamePanel.endGame();
		}

		if (e.getSource() == startNewB){
			soundManager.stopSound("background2");
			mainPanel.setBackground(new Color(110,186,197));
			gamePanel.startNewGame();
		}

		if (e.getSource() == exitB)
			System.exit(0);

		mainPanel.requestFocus();
	}


	// implementation of methods in KeyListener interface

	public void keyPressed (KeyEvent e) {

		int keyCode = e.getKeyCode();
		switch (keyCode) {
			case KeyEvent.VK_LEFT -> gamePanel.moveLeft();
			case KeyEvent.VK_RIGHT -> gamePanel.moveRight();
			case KeyEvent.VK_SPACE -> gamePanel.jump();
			case KeyEvent.VK_UP -> gamePanel.use();
		}
	}


	public void keyReleased (KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT||keyCode == KeyEvent.VK_RIGHT) {
			gamePanel.idle();
		}
	}


	public void keyTyped (KeyEvent e) {

	}


	// implement methods of MouseListener interface

	public void mouseClicked(MouseEvent e) {
	}


	public void mouseEntered(MouseEvent e) {

	}


	public void mouseExited(MouseEvent e) {

	}


	public void mousePressed(MouseEvent e) {
		testMousePress(e.getX(), e.getY());
	}


	public void mouseReleased(MouseEvent e) {

	}


	// implement methods of MouseMotionListener interface

	public void mouseDragged(MouseEvent e) {

	}


	public void mouseMoved(MouseEvent e) {
	}


    /** This method handles mouse clicks on one of the buttons
	 (Pause, Stop, Start Anim, Pause Anim, and Quit).
	 */

	private void testMousePress(int x, int y) {

		if (isStopped && !isOverQuitButton)     // don't do anything if game stopped
			return;

		if (isOverStopButton) {            // mouse click on Stop button
			isStopped = true;
			isPaused = false;
		} else if (isOverPauseButton) {        // mouse click on Pause button
			isPaused = !isPaused;         // toggle pausing
		} else if (isOverQuitButton) {        // mouse click on Quit button
			isRunning = false;        // set running to false to terminate
		}
	}


}