package myGUI;

import CRUD.Query;


import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class Feed extends JFrame implements ActionListener {
    String userName;
    File file;
    JFrame frame = new JFrame();
    Query query = new Query();
    String sql;
    final int FRAME_WIDTH = 500;
    final int FRAME_HEIGHT = 888;
    final float FRAME_WIDTH_WITH_GAP = 333;
    final float FRAME_HEIGHT_WITH_GAP = 592;
    long[] friends;

    public Feed(File file, String userName){
        this.file = file;
        this.userName = userName;
        frame.setTitle("Feed");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        int panel_height = 0;
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 70));



        //получение айди пользователя
        sql = "SELECT `userID` FROM `User` WHERE `username` = '" + userName + "'";
        Object[][] userID = new Object[1][1];
        userID = query.retrieve(sql);


        //получение айди картинки
        sql = "SELECT MAX(`imageID`) FROM `Image`";
        Object[][] imageID = new Object[1][1];
        imageID = query.retrieve(sql);


        sql = "INSERT INTO `BeReal`(`imageID`, `userID`) VALUES ("+ imageID[0][0] +"," + userID[0][0] +")";
        query.execute(sql);

        //составление списка друзей
        sql = "SELECT `userID2` FROM `User_to_User` WHERE `userID1` = '" + userID[0][0] + "'";
        Object[][] arr;
        arr = query.retrieve(sql);
        friends = new long[arr.length];
        for (int i = 0; i < arr.length; i ++ ){
            friends[i] = (long) arr[0][i];
        }


//        //вывод моей пикчи
//        JLabel label = new JLabel();
//        Image image = new ImageIcon(file.getAbsolutePath()).getImage();
//        float width = image.getWidth(null);
//        float height = image.getHeight(null);
//        System.out.println(width);
//        System.out.println(height);
//        float heightDivisor = (height/FRAME_HEIGHT_WITH_GAP);
//        float widthDivisor = (width/FRAME_WIDTH_WITH_GAP);
//        if(width > FRAME_WIDTH_WITH_GAP){
//            height = height/widthDivisor;
//            width = width/widthDivisor;
//        }
//        if(height > FRAME_HEIGHT_WITH_GAP){
//            width = width/heightDivisor;
//            height = height/heightDivisor;
//        }
//        int rdWidth =  Math.round(width);
//        int rdHeight = Math.round(height);
//        panel_height += rdHeight;
//        int xLeftTopCorner = (int)(FRAME_WIDTH/2) - (rdWidth/2);
//        int yLeftTopCorner = (int)(FRAME_HEIGHT/2) - (rdHeight/2);
//        label.setBounds(xLeftTopCorner, yLeftTopCorner, rdWidth, rdHeight);
//        Image scaledImage = image.getScaledInstance(rdWidth, rdHeight, Image.SCALE_SMOOTH);
//        label.setIcon(new ImageIcon(scaledImage));
//        label.setText("My Picture");
//        label.setFont(new Font("JetBrains Mono", Font.BOLD,40));
//        label.setForeground(Color.white);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setVerticalTextPosition(JLabel.TOP);
//        panel.add(label);

        //добавление чужих пикчей-пикчей
        try {
            for (int i = 0; i < friends.length; i++) {
                JLabel otherLabel = new JLabel();
                sql = "SELECT i.image " +
                        "FROM Image i " +
                        "JOIN BeReal b ON i.imageID = b.imageID " +
                        "WHERE b.userID = " + friends[i] +
                        " AND b.beRealID = ( " +
                        "    SELECT MAX(beRealID) " +
                        "    FROM BeReal " +
                        "    WHERE userID = " + friends[i] +
                        " );";
                System.out.println("USER ID: " + friends[i]);
                Blob blob = query.blobRetrieve(sql);
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());

                // Convert byte array to Image
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                Image otherImage = ImageIO.read(bis);

                float otherWidth = otherImage.getWidth(null);
                float otherHeight = otherImage.getHeight(null);
                System.out.println(otherWidth);
                System.out.println(otherHeight);
                float otherHeightDivisor = (otherHeight / FRAME_HEIGHT_WITH_GAP);
                float otherWidthDivisor = (otherWidth / FRAME_WIDTH_WITH_GAP);
                if (otherWidth > FRAME_WIDTH_WITH_GAP) {
                    otherHeight = otherHeight / otherWidthDivisor;
                    otherWidth = otherWidth / otherWidthDivisor;
                }
                if (otherHeight > FRAME_HEIGHT_WITH_GAP) {
                    otherWidth = otherWidth / otherHeightDivisor;
                    otherHeight = otherHeight / otherHeightDivisor;
                }
                int otherRdWidth = Math.round(otherWidth);
                int otherRdHeight = Math.round(otherHeight);
                panel_height += otherRdHeight;
                int otherXLeftTopCorner = (int) (FRAME_WIDTH / 2) - (otherRdWidth / 2);
                int otherYLeftTopCorner = (int) (FRAME_HEIGHT / 2) - (otherRdHeight / 2);
                otherLabel.setBounds(otherXLeftTopCorner, otherYLeftTopCorner, otherRdWidth, otherRdHeight);
                Image otherscaledImage = otherImage.getScaledInstance(otherRdWidth, otherRdHeight, Image.SCALE_SMOOTH);
                otherLabel.setIcon(new ImageIcon(otherscaledImage));
                sql = "SELECT (`username`) FROM `User` WHERE `userID` = " + friends[i];
                String name = (String) query.retrieve(sql)[0][0];
                otherLabel.setText(name);
                otherLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
                otherLabel.setForeground(Color.white);
                otherLabel.setHorizontalTextPosition(JLabel.CENTER);
                otherLabel.setVerticalTextPosition(JLabel.TOP);
                panel.add(otherLabel);
            }
        }
        catch (SQLException | IOException e){
            e.printStackTrace();
        }

        panel.setPreferredSize(new Dimension(490, panel_height));
        panel.setBackground(Color.black);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBackground(Color.black);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // изменение скорости скроллирования

        frame.getContentPane().add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
}
