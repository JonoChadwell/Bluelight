package bluelight;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Bluelight2 extends KeyAdapter {

   private static BluetoothConnection connection;
   private static LightController controller;

   public static void main(String args[]) {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 600);
      frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
      Bluelight2 bluelight = new Bluelight2();
      frame.addKeyListener(bluelight);
      ImageIcon image = new ImageIcon(bluelight.getClass().getClassLoader().getResource(
            "resources/ghostbusters.png"));
      frame.add(new JLabel(image));
      connection = new BluetoothConnection(null);
      controller = new LightController(connection);
      frame.setVisible(true);
   }

   private static Clip current;

   @Override
   public void keyPressed(KeyEvent e) {
      System.out.println(e.getKeyCode());
      if (current != null) {
         current.stop();
      }

      try {
         current = AudioSystem.getClip();

         switch (e.getKeyCode()) {
         case 10:
            controller.setLights(0, 0, 0);
            break;

         case 110:
            controller.randomize(false);
            break;

         case 96:
            controller.fade();
            break;

         case 99:
            controller.rainbow();
            break;

         case 98:
            controller.randomizeRepeatFast();
            break;

         case 97:
            controller.randomizeRepeatSlow();
            break;

         case 8:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/gun.wav")));
            break;

         case 102:
            controller.internet();
            break;

         case 101:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/song.wav")));
            break;

         case 100:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/staypuft.wav")));
            break;

         case 107:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/sayyes.wav")));
            break;

         case 105:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/onlyzuul.wav")));
            break;

         case 104:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/nuclear.wav")));
            break;

         case 103:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/kicked.wav")));
            break;

         case 109:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/hobbies.wav")));
            break;

         case 106:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/hasno.wav")));
            break;

         case 111:
            current.open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(
                  "resources/bad.wav")));
            break;
         }

         if (current != null) {
            current.start();
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
