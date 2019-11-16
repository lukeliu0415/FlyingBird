/* Luke Liu  Friday January 18, 2019
    This program is a 2d single player game, imitating the once viral game of flappy
bird. The game is a side-scroller where the player controls a flying bird, who
moves continuously to the right between sets of green Mario-like pipes.
 */
package flyingbird;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;

public class FlyingBird extends JPanel implements KeyListener, ActionListener {

    Random k = new Random(); //Initializes the random used in the game

    //Declares the buttons used in the game
    JButton inst; //Instructions
    JButton high; //High Scores
    JButton easy; //Easy level
    JButton inter; //Intermediate level
    JButton hard; //Hard level
    JButton back; //Back to the home screen
    JButton again; //Play again
    JButton ret; //Return to the home screen
    JButton store; //Store
    JButton purchase[] = new JButton[3]; //Purchasing birds
    JButton select[] = new JButton[3]; //Selecting birds

    //Declares the images used in the game
    Image background; //Background
    Image stamp; //High score stamp
    Image coin; //Coin
    Image bird; //Bird used
    Image birds[] = new Image[3]; //3 available birds
    Image birdslarge[] = new Image[3]; //3 available birds enlarged in the store
    Image tubeUpper[] = new Image[5]; //5 differnet upper tubes
    Image tubeLower[] = new Image[5]; //5 different lower tubes

    //Initializes integer variables used in the game
    int score = 0; //Current score
    int coinscore = 0; //Amount of coins
    int easyh = 0; //High score for easy level
    int interh = 0; //High score for intermediate level
    int hardh = 0; //High score for hard level
    int vel = 0; //Bird's vertical velocity
    int by = 175; //Bird's vertical position
    int tube[] = {500, 450, 400, 350, 300}; //Tubes' vertical positions
    int coinp[] = {150, 200, 250, 300, 350, 400}; //Coins' vertical positions
    ArrayList<Integer> tx = new ArrayList<Integer>(); //Tubes' horizontal positions
    ArrayList<Integer> tc = new ArrayList<Integer>(); //Type of tubes
    ArrayList<Integer> cx = new ArrayList<Integer>(); //Coins' horizontal positions
    ArrayList<Integer> cc = new ArrayList<Integer>(); //Type of coin

    //Iniitalizes boolean variables used in the game
    boolean play = false; //Whether the game is in play
    boolean die = false; //Whether the bird died
    boolean easyb = false; //Whether the user selected easy level
    boolean interb = false; //Whether the user selected intermediate level
    boolean hardb = false; //Whether the user selected hard level
    boolean storeb = false; //Whether the user selected the store
    boolean instb = false; //Whether the user selected instructions
    boolean highb = false; //Whether the user selected high scores
    boolean released = true; //Whether the space key is released
    boolean stampb = false; //Whether the stamp needs to be displayed
    boolean[] selected = {true, false, false}; //Whether the birds are selected
    boolean[] purchased = {true, false, false}; //Whether the birds are purchased
    ArrayList<Boolean> eat = new ArrayList<Boolean>(); //Whether the user collected the coin

    FlyingBird() {
        this.setLayout(null); //Sets the layout manager to be null
        addKeyListener(this); //Adds keylistener to the program
        setFocusable(true); //Sets the focus of the keyboard

        inst = new JButton("INSTRUCTIONS");
        inst.setBounds(420, 150, 150, 100);
        inst.addActionListener(this);
        add(inst); //Adds instructions button to the frame in the specified position

        high = new JButton("HIGH SCORES");
        high.setBounds(620, 150, 150, 100);
        high.addActionListener(this);
        add(high); //Adds high scores button to the frame in the specified position

        store = new JButton("STORE");
        store.setBounds(820, 150, 150, 100);
        store.addActionListener(this);
        add(store); //Adds store button to the frame in the specified position

        for (int i = 1; i < 3; i++) {
            purchase[i] = new JButton("PURCHASE");
            purchase[i].setBounds(200 + 300 * i, 400, 175, 100);
            purchase[i].addActionListener(this);
            add(purchase[i]);
            purchase[i].setVisible(false); //Sets the purchase buttons to be invisible
        } //Adds purchase buttons to the frame in the specified position

        for (int i = 0; i < 3; i++) {
            select[i] = new JButton("SELECT");
            select[i].setBounds(200 + 300 * i, 400, 175, 100);
            select[i].addActionListener(this);
            add(select[i]);
            select[i].setVisible(false); //Sets the select buttons to be invisible
        } //Adds select buttons to the frame in the specified position

        easy = new JButton("EASY");
        easy.setBounds(420, 300, 150, 100);
        easy.addActionListener(this);
        add(easy); //Adds easy button to the frame in the specified position

        inter = new JButton("INTERMEDIATE");
        inter.setBounds(620, 300, 150, 100);
        inter.addActionListener(this);
        add(inter); //Adds intermediate button to the frame in the specified position

        hard = new JButton("HARD");
        hard.setBounds(820, 300, 150, 100);
        hard.addActionListener(this);
        add(hard); //Adds hard button to the frame in the specified position

        back = new JButton("BACK");
        back.setBounds(400, 300, 150, 100);
        back.addActionListener(this);
        add(back); //Adds back button to the frame in the specified position
        back.setVisible(false); //Set the back button to be invisible

        again = new JButton("PLAY AGAIN");
        again.setBounds(200, 300, 150, 100);
        again.addActionListener(this);
        add(again); //Adds play again button to the frame in the specified position
        again.setVisible(false); //Sets the play again button to be invisible

        ret = new JButton("RETURN");
        ret.setBounds(1040, 80, 150, 100);
        ret.addActionListener(this);
        add(ret); //Adds return button to the frame in the specified position
        ret.setVisible(false); //Sets the return button to be invisible

        try {
            Image tubeu = ImageIO.read(new File("tubeupper.png")); //Reads the image upper tube
            tubeUpper[0] = tubeu.getScaledInstance(40, 300, Image.SCALE_SMOOTH);
            tubeUpper[1] = tubeu.getScaledInstance(40, 250, Image.SCALE_SMOOTH);
            tubeUpper[2] = tubeu.getScaledInstance(40, 200, Image.SCALE_SMOOTH);
            tubeUpper[3] = tubeu.getScaledInstance(40, 150, Image.SCALE_SMOOTH);
            tubeUpper[4] = tubeu.getScaledInstance(40, 100, Image.SCALE_SMOOTH);
            //Scale the upper tube image to create 5 different types of upper tube

            Image tubel = ImageIO.read(new File("tubelower.png")); //Reads the image lower tube
            tubeLower[0] = tubel.getScaledInstance(40, 100, Image.SCALE_SMOOTH);
            tubeLower[1] = tubel.getScaledInstance(40, 150, Image.SCALE_SMOOTH);
            tubeLower[2] = tubel.getScaledInstance(40, 200, Image.SCALE_SMOOTH);
            tubeLower[3] = tubel.getScaledInstance(40, 250, Image.SCALE_SMOOTH);
            tubeLower[4] = tubel.getScaledInstance(40, 300, Image.SCALE_SMOOTH);
            //Scale the lower tube image to create 5 different types of lower tube

            birds[0] = ImageIO.read(new File("bird1.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH); //Reads and scales bird 1
            birds[1] = ImageIO.read(new File("bird2.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH); //Reads and scales bird 2
            birds[2] = ImageIO.read(new File("bird3.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH); //Reads and scales the bird 3
            birdslarge[0] = ImageIO.read(new File("bird1.png")).getScaledInstance(175, 175, Image.SCALE_SMOOTH); //Reads and scales bird 1
            birdslarge[1] = ImageIO.read(new File("bird2.png")).getScaledInstance(175, 175, Image.SCALE_SMOOTH); //Reads and scales bird 2
            birdslarge[2] = ImageIO.read(new File("bird3.png")).getScaledInstance(175, 175, Image.SCALE_SMOOTH); //Reads and scales bird 3
            background = ImageIO.read(new File("background.png")).getScaledInstance(1200, 600, Image.SCALE_SMOOTH); //Scales the background image to fit the frame
            stamp = ImageIO.read(new File("new.png")).getScaledInstance(150, 150, Image.SCALE_SMOOTH); //Reads and scales the stamp
            coin = ImageIO.read(new File("coin.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH); //Reads and scales the coin

            bird = birds[0]; //Initialize the bird to be bird 1
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(1); //Exits the program if the file is not found
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource(); //Gets the button that the user clicked

        //Home screen menu
        if (action.equals(easy) || action.equals(inter) || action.equals(hard) || action.equals(inst) || action.equals(high) || action.equals(store)) {
            inst.setVisible(false);
            easy.setVisible(false);
            inter.setVisible(false);
            hard.setVisible(false);
            high.setVisible(false);
            store.setVisible(false); //Hides the buttons

            if (action.equals(easy)) {
                play = true;
                easyb = true;
                for (int i = 0; i < 999; i++) {
                    tc.add(k.nextInt(5));
                    tx.add(-300 * i); //Adds 999 randomly selected tubes with spacing of 300 if easy level is selected
                    cc.add(k.nextInt(6));
                    cx.add(-150 - 300 * i); //Adds 999 coins randomly placed in the vertical position
                    eat.add(false); //The coins are not collected yet
                }
            } else if (action.equals(inter)) {
                play = true;
                interb = true;
                for (int i = 0; i < 999; i++) {
                    tc.add(k.nextInt(5));
                    tx.add(-240 * i); //Adds 999 randomly selected tubes with spacing of 240 if intermediate level is selected
                    cc.add(k.nextInt(6));
                    cx.add(-120 - 240 * i); //Adds 999 coins randomly placed in the vertical position
                    eat.add(false); //The coins are not collected yet
                }
            } else if (action.equals(hard)) {
                play = true;
                hardb = true;
                for (int i = 0; i < 999; i++) {
                    tc.add(k.nextInt(5));
                    tx.add(-200 * i); //Adds 999 randomly selected tubes with spacing of 200 if hard level is selected
                    cc.add(k.nextInt(6));
                    cx.add(-100 - 200 * i); //Adds 999 coins randomly placed in the vertical position
                    eat.add(false); //The coins are not collected yet
                }
            } else if (action.equals(inst)) {
                instb = true;
                ret.setVisible(true);
            } else if (action.equals(high)) {
                highb = true;
                ret.setVisible(true);
            } else if (action.equals(store)) {
                storeb = true;
                ret.setVisible(true);
                for (int i = 0; i < 3; i++) { //For each bird available
                    if (!selected[i] && purchased[i]) {
                        select[i].setVisible(true); //Shows the select button if it is owned
                    } else if (!selected[i] && !purchased[i]) {
                        purchase[i].setVisible(true); //Shows the purchase button if it is not owned
                    }
                }
            } //Sets relevant boolean variables to be true and display appropriate buttons based on user selection
        }

        //Store menu
        for (int i = 0; i < 3; i++) { //For each bird available
            if (action.equals(purchase[i])) { //If the user selects purchase button
                if (coinscore < 20 * i) {
                    JOptionPane.showMessageDialog(null, "Unfortunately, you do not have enough coins. \n"
                            + "Keep playing to earn more coins!"); //Informs the user - not enough money
                } else {
                    coinscore -= 20 * i; //Decrease coins by the price
                    purchased[i] = true; //Sets purchased to be true
                    purchase[i].setVisible(false); //Hides the purchase button
                    select[i].setVisible(true); //Shows the select button
                    JOptionPane.showMessageDialog(null, "Congratulations on owning your new bird! \n"
                            + "Select it to use it in game."); //Informs the user - purchase successful
                }
            } else if (action.equals(select[i])) { //if the user selects select button
                selected[i] = true; //Sets selected to be true
                select[i].setVisible(false); //Hides the select button
                bird = birds[i]; //Sets the bird used to be the bird just selected
                for (int j = 0; j < 3; j++) {
                    if (j != i) { //For each other button not selected
                        select[j].setVisible(true); //Shows select button
                        selected[j] = false;  //Sets selected to be false
                    }
                }
            }
        }

        //Returning to the home screen from instructions page/high score page/store
        if (action.equals(ret)) {
            instb = false;
            highb = false;
            storeb = false;
            inst.setVisible(true);
            easy.setVisible(true);
            inter.setVisible(true);
            hard.setVisible(true);
            high.setVisible(true);
            store.setVisible(true);
            ret.setVisible(false);
            for (int i = 1; i < 3; i++) {
                purchase[i].setVisible(false);
            }
            for (int i = 0; i < 3; i++) {
                select[i].setVisible(false);
            }
        } //Sets appropriate boolean variables to be false, shows home screen buttons and hides return button

        //Menu after dying
        if (action.equals(back) || action.equals(again)) {
            die = false;
            stampb = false;
            by = 175;
            vel = 0;
            score = 0; //Resets variables to the initialized stage
            back.setVisible(false);
            again.setVisible(false); //Hides the buttons
            tx.clear();
            tc.clear(); //Clears the tubes
            cx.clear();
            cc.clear(); //Clears the coins
            eat.clear(); //Clears the boolean variables for coins

            if (action.equals(again)) { //If the user selects play again
                if (easyb) {
                    for (int i = 0; i < 999; i++) {
                        tc.add(k.nextInt(5));
                        tx.add(-300 * i); //Adds 999 randomly selected tubes with spacing of 300 if easy level is selected
                        cc.add(k.nextInt(6));
                        cx.add(-150 - 300 * i); //Adds 999 coins randomly placed in the vertical position
                        eat.add(false); //The coins are not collected yet
                    }
                } else if (interb) {
                    for (int i = 0; i < 999; i++) {
                        tc.add(k.nextInt(5));
                        tx.add(-240 * i); //Adds 999 randomly selected tubes with spacing of 240 if intermediate level is selected
                        cc.add(k.nextInt(6));
                        cx.add(-120 - 240 * i); //Adds 999 coins randomly placed in the vertical position
                        eat.add(false); //The coins are not collected yet
                    }
                } else if (hardb) {
                    for (int i = 0; i < 999; i++) {
                        tc.add(k.nextInt(5));
                        tx.add(-200 * i); //Adds 999 randomly selected tubes with spacing of 200 if hard level is selected
                        cc.add(k.nextInt(6));
                        cx.add(-100 - 200 * i); //Adds 999 coins randomly placed in the vertical position
                        eat.add(false); //The coins are not collected yet
                    }
                }
            }

            if (action.equals(back)) { //If the user selects back
                play = false;
                easyb = interb = hardb = false; //Resets variables to the initialized stage
                inst.setVisible(true);
                easy.setVisible(true);
                inter.setVisible(true);
                hard.setVisible(true);
                high.setVisible(true);
                store.setVisible(true); //Shows the buttons on the home screen
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        char key = (char) e.getKeyCode(); //Gets the key that is pressed
        if (key == e.VK_SPACE && play && !die && released) {
            vel = -13; //Sets velocity to -13 when the player presses space key and the game isin play
            released = false; //Sets released variable to be false
        }
        if (key == e.VK_ESCAPE || key == 'Q') {
            System.exit(0); //Exits the game when the player presses ESC or Q
        }
    }

    public void keyReleased(KeyEvent e) {
        char key = (char) e.getKeyCode(); //Gets the key that is released
        if (key == e.VK_SPACE) {
            released = true; //Sets released variable to be true when the player releases the space key
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, null); //Paints the background

        if (!play && !instb && !highb && !storeb) { //Paints the front page
            g.setFont(new Font("Trajan", Font.BOLD, 60));
            g.drawString("Flying Bird", 410, 90);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Levels:", 220, 360);
            g.drawImage(bird, 300, by, null);
        }

        if (instb) { //Paints the instructions page
            g.setFont(new Font("Trajan", Font.BOLD, 60));
            g.drawString("Instructions", 410, 90);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("✻ Press the space key to make the bird fly up, who falls due to gravity.", 160, 160);
            g.drawString("✻ Everytime the bird passes through a pair of pipes, you earn one point.", 160, 200);
            g.drawString("✻ The game ends when the bird hits the pipe or falls on the ground.", 160, 240);
            g.drawString("✻ Earn coins in game and redeem them in the store with new birds.", 160, 280);
            g.drawString("✻ Select one of the three levels to play.", 160, 320);
            g.drawString("✻ Press ESC or Q at anytime to quit the game.", 160, 360);
            g.drawString("✻ Good luck and have fun!", 160, 400);
        }

        if (highb) { //Paints the high scores page
            g.setFont(new Font("Trajan", Font.BOLD, 60));
            g.drawString("High Scores", 410, 90);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Easy: " + easyh, 200, 200);
            g.drawString("Intermediate: " + interh, 200, 275);
            g.drawString("Hard: " + hardh, 200, 350);
        }

        if (storeb) { //Paints the store
            g.setFont(new Font("Trajan", Font.BOLD, 60));
            g.drawString("Store", 500, 90);
            g.drawImage(birdslarge[0], 200, 120, null);
            g.drawImage(birdslarge[1], 500, 120, null);
            g.drawImage(birdslarge[2], 800, 120, null); //Draws all three available birds
            g.setFont(new Font("Arial", Font.BOLD, 25));
            for (int i = 0; i < 3; i++) {
                if (selected[i]) {
                    g.drawString("Selected", 230 + 300 * i, 350);
                } else if (!selected[i] && purchased[i]) {
                    g.drawString("Owned", 250 + 300 * i, 350);
                } else if (!selected[i] && !purchased[i]) {
                    g.drawString(20 * i + " Coins", 230 + 300 * i, 350);
                }
            } //Display appropriate text based on whether the bird is purchased/selected
        }

        if (play) { //Paints the playing board when in play
            for (int i = 0; i < tx.size(); i++) {
                g.drawImage(tubeUpper[tc.get(i)], 1200 - tx.get(i), 0, null);
                g.drawImage(tubeLower[tc.get(i)], 1200 - tx.get(i), tube[tc.get(i)], null);
                if (!eat.get(i)) {
                    g.drawImage(coin, 1200 - cx.get(i), coinp[cc.get(i)], null);
                }
            }
            moveBird(); //Calls the moveBird method to move the birds
            g.drawImage(bird, 300, by, null);
        }

        if (play && !die) { //Paints the playing board while the bird is alive
            g.setColor(Color.white);
            g.fillRect(0, 0, 150, 45);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Score: " + score, 10, 30);
            movePipe(); //Calls the movePipe method to move the pipes
            collisionDetection(); //Calls the collisionDetecion to detect collision between birds and pipes
        }

        if (play && die) { //Paints the playing board while the bird is dead
            g.setColor(Color.white);
            g.fillRect(200, 150, 400, 125);
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Your Score: " + score, 200, 200);
            if (easyb) {
                g.drawString("High Score: " + easyh, 200, 260);
            } else if (interb) {
                g.drawString("High Score: " + interh, 200, 260);
            } else if (hardb) {
                g.drawString("High Score: " + hardh, 200, 260);
            } //Paints the appropriate high score based on the level
            if (stampb) {
                g.drawImage(stamp, 545, 200, null); //Draws the new stamp only if a high score is broken
            }
        }

        g.setColor(Color.white);
        g.fillRect(1040, 0, 200, 45);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Coins: " + coinscore, 1050, 30); //Draws the number of coins the player has

        delay(15); //Delays the repaint by 15 milliseconds
        repaint(); //Schedules a call to repaint
    }

    public void moveBird() { //Method responsible for moving the bird
        vel = vel + 1; //Increases the bird's velocity downwards
        by += vel; //The bird accelerates downwards
    }

    public void movePipe() { //Method responsible for moving the pipes
        if (tx.get(0) == 1400) {
            tx.remove(0);
            tc.remove(0); //If the pipe reached the leftmost boundary, remove the pipe
            cx.remove(0);
            cc.remove(0); //If the coin reached the leftmost boundary, remove the coin
            eat.remove(0); //If the coin reached the leftmost boundary, remove the boolean variable
        }

        for (int i = 0; i < tx.size(); i++) {
            if (easyb) {
                tx.set(i, tx.get(i) + 5);
                cx.set(i, cx.get(i) + 5);
            } else if (interb) {
                tx.set(i, tx.get(i) + 8);
                cx.set(i, cx.get(i) + 8);
            } else if (hardb) {
                tx.set(i, tx.get(i) + 10);
                cx.set(i, cx.get(i) + 10);
            }
        } //Moves the pipes and coins using the appropriate velocity based on the levels

        if (tx.get(0) == 880 || tx.get(1) == 880 || tx.get(2) == 880) {
            score += 1; //When the bird passes through the pipe, the score increases by 1
        }
    }

    public void collisionDetection() { //Method responsible for detecting the collision between the bird and the pipe
        for (int i = 0; i < 3; i++) { //Check for collision for each pipe and coin
            if ((tx.get(i) > 850 && tx.get(i) < 940 && (by < tube[tc.get(i)] - 200 || by > tube[tc.get(i)] - 50)) || by > 520 || score == 999) {
                die = true;
                vel = 0; //If the bird hits the tube, set die variable to be true and set its initial velocity to be 0
                back.setVisible(true);
                again.setVisible(true); //Shows the back and play again buttons
                if (easyb && score > easyh) {
                    easyh = score;
                    stampb = true;
                } else if (interb && score > interh) {
                    interh = score;
                    stampb = true;
                } else if (hardb && score > hardh) {
                    hardh = score;
                    stampb = true;
                } //Adjusts the high score and shows the new stamp as appropriate
            }
            if (cx.get(i) > 850 && cx.get(i) < 950 && by < coinp[cc.get(i)] + 50 && by > coinp[cc.get(i)] - 50 && !eat.get(i)) {
                eat.set(i, true);
                coinscore++; //If the bird hits the coin, set eat variable to be true and increases the coin count by 1
            }
        }
    }

    public static void delay(int mili) { //Delay method so that the repaint could be delayed
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            System.out.println("ERROR IN SLEEPING");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flying Bird"); //Names the frame
        frame.getContentPane().add(new FlyingBird()); //Adds the frame
        frame.setSize(1200, 600); //Sets the size of the frame
        frame.setVisible(true); //Makes the frame visible
        frame.setResizable(false); //Sets the frame so that it is not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exits the program when it is closed
    }
}