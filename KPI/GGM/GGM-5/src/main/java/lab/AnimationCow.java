package lab;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.*;

public class AnimationCow implements ActionListener, KeyListener {
    private Button go;
    private TransformGroup wholePlane;
    private Transform3D translateTransform;
    private Transform3D rotateTransformX;
    private Transform3D rotateTransformY;
    private Transform3D rotateTransformZ;
    
    private JFrame mainFrame;
    
    private float sign=1.0f;
    private float zoom=0.5f;
    private float xloc=0.3f;
    private float yloc=-1.2f;
    private float zloc=0.0f;
    private int moveType=1;
    private Timer timer;
    
    public AnimationCow(TransformGroup wholePlane, Transform3D trans, JFrame frame){
        go = new Button("Корiвка пасеться");
        this.wholePlane=wholePlane;
        this.translateTransform=trans;
        this.mainFrame=frame;
        
        rotateTransformX= new Transform3D();
        rotateTransformY= new Transform3D();
        rotateTransformZ= new Transform3D();
        
        FirstMainClass.canvas.addKeyListener(this);
        timer = new Timer(100, this);
        
        Panel p =new Panel();
        p.add(go);
        mainFrame.add("North",p);
        go.addActionListener(this);
        go.addKeyListener(this);
    }

    private void initialPlaneState(){
        xloc=0.0f;
        yloc=0.0f;
        zloc=0.0f;
        zoom=0.2f;
        moveType=1;
        sign=1.0f;
        rotateTransformY.rotY(-Math.PI/2.8);
        translateTransform.mul(rotateTransformY);
        if(timer.isRunning()){timer.stop();}
        go.setLabel("Корiвка пасеться");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // start timer when button is pressed
       if (e.getSource()==go){
          if (!timer.isRunning()) {
             timer.start();
             go.setLabel("Корiвка не пасеться");
          }
          else {
              timer.stop();
              go.setLabel("Корiвка пасеться");
          }
       }
       else {
           Move(moveType);
           translateTransform.setScale(new Vector3d(zoom,zoom,zoom));
           translateTransform.setTranslation(new Vector3f(xloc,yloc,zloc));
           wholePlane.setTransform(translateTransform);
       }
    }

    private void Move(int mType){
        if(mType==1){ //fly forward and back
           xloc += 0.1 * sign;
           if (Math.abs(xloc *2) >= 1.2 ) {
               sign = -1.0f * sign;
               rotateTransformY.rotY(Math.PI);
               translateTransform.mul(rotateTransformY);
           }
        }
        if(mType==2){ //fly_away
          if(zoom<0.03){
              initialPlaneState();
          }
          else{
           xloc += 0.04;
           yloc += 0.003;
           zloc -=0.005;
           zoom-=0.005;
          }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
          //Invoked when a key has been typed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
         //Invoked when a key has been pressed.
        if (e.getKeyChar()=='s') {xloc = xloc + .05f;}
        if (e.getKeyChar()=='a') {xloc = xloc - .05f;}
        if (e.getKeyChar()=='w') {yloc = yloc + .05f;}
        if (e.getKeyChar()=='z') {yloc = yloc - .05f;}
        
        if (e.getKeyChar()=='1') {
            rotateTransformX.rotX(Math.PI/2);
            translateTransform.mul(rotateTransformX);
        }
        if (e.getKeyChar()=='2') {
            rotateTransformY.rotY(Math.PI/2);
            translateTransform.mul(rotateTransformY);
        }
        if (e.getKeyChar()=='3') {
            rotateTransformZ.rotZ(Math.PI/2);
            translateTransform.mul(rotateTransformZ);
        }
        if (e.getKeyChar()=='0'){
            rotateTransformY.rotY(Math.PI/2.8);
            translateTransform.mul(rotateTransformY);
            moveType=2;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }
    
}
