import javax.swing.*;            // need this for GUI objects
import java.awt.*;            // need this for certain AWT classes
import java.awt.event.*;

public class GameWindow extends JFrame implements
		KeyListener,
		MouseListener,
		ActionListener,
		MouseMotionListener
{


	private volatile boolean isRunning = false;        // used to stop the game thread


	private volatile boolean isOverQuitButton = false;
	private Rectangle quitButtonArea;        // used by the quit button

	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseButtonArea;        // used by the pause 'button'
	private volatile boolean isPaused = false;

	private volatile boolean isOverStopButton = false;
	private Rectangle stopButtonArea;        // used by the stop 'button'

	private volatile boolean isStopped = false;

	private GamePanel gamePanel;
	private InfoPanel infoPanel;
	private JPanel mainPanel;


// declare buttons
	private JButton pauseB;
	private JButton endB;
	private JButton startNewB;
	private JButton exitB;

	public GameWindow() {

		super("Chefable Project");
		setTitle ("Chefable Project");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize (screenSize.width, screenSize.height);


		// create mainPanel
		mainPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel();

		// Add Objects to InfoPanel
		infoPanel = new InfoPanel();

		// create buttonPanel
		// create buttons

//		startB = new JButton ("Start Game");
		pauseB = new JButton ("Pause Game");
		endB = new JButton ("End Game");
		startNewB = new JButton ("Start New Game");
		exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)

//		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		startNewB.addActionListener(this);
		exitB.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(1, 4);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel

		buttonPanel.add (startNewB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (exitB);

		// add sub-panels with GUI objects to mainPanel and set its colour
		mainPanel.add(infoPanel,BorderLayout.NORTH);
		mainPanel.add(gamePanel,BorderLayout.CENTER);
		mainPanel.add (buttonPanel,BorderLayout.SOUTH);

		mainPanel.setBackground(Color.WHITE);

		//Add listeners
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener( this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface
		Container c = getContentPane();
		c.add(mainPanel);

		// set properties of window
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
			if (command.equals("Pause Game"))
				pauseB.setText ("Resume");
			else
				pauseB.setText ("Pause Game");

		}

		if (command.equals(endB.getText())) {
			gamePanel.endGame();
		}

		if (command.equals(startNewB.getText()))
			gamePanel.startNewGame();

		if (command.equals(exitB.getText()))
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
		testMouseMove(e.getX(), e.getY());
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
		}
		else if (isOverQuitButton) {        // mouse click on Quit button
			isRunning = false;        // set running to false to terminate
		}
	}


    /** This method checks to see if the mouse is currently moving over one of
	   the buttons (Pause, Stop, Show Anim, Pause Anim, and Quit). It sets a
	   boolean value which will cause the button to be displayed accordingly.
	*/

	private void testMouseMove(int x, int y) {
		if (isRunning) {
			isOverPauseButton = pauseButtonArea.contains(x, y);
			isOverStopButton = stopButtonArea.contains(x, y);
			isOverQuitButton = quitButtonArea.contains(x, y);
		}
	}

}