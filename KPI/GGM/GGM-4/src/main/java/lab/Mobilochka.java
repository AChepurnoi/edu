package lab;


import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sasha on 4/22/17.
 */

public class Mobilochka implements ActionListener {
    public static void main(String[] args) {
        new Mobilochka();
    }

    private TransformGroup treeTransformGroup = new TransformGroup();
    private Transform3D treeTransform3D = new Transform3D();
    private Timer timer;
    private float angle = 0;

    public Mobilochka() {
        SimpleUniverse universe = new SimpleUniverse();
// створюємо групу, в яку додаємо об'єкти для відображення
        BranchGroup group = sceneSetup();
        timer = new Timer(100, this);
        timer.start();
        BranchGroup phone = phone();
        treeTransformGroup.setTransform(treeTransform3D);
        treeTransformGroup.addChild(phone);
        universe.getViewingPlatform().setNominalViewingTransform();


// додаємо створену групу у простір
        universe.addBranchGraph(group);
    }


    public BranchGroup phone() {
        BranchGroup group = new BranchGroup();
        Appearance appearance = new Appearance();
        ColoringAttributes attr = new ColoringAttributes();
        attr.setColor(new Color3f(1.7f, 1.6f, .0f));
        Color3f ambient = new Color3f(0.4f, 0.15f, .15f);
        Color3f diffuse = new Color3f(1.2f, 1.0f, .15f);
        Color3f specular = new Color3f(0.2f, 0.5f, 0.0f);
        appearance.setMaterial(new Material(ambient,
                new Color3f(0.5f, 1.5f, 0.9f), diffuse, specular, 1.0f));

        appearance.setColoringAttributes(attr);
        Box phone = new Box(0.3f, 0.7f, 0.1f, appearance);
        group.addChild(phone);
        group.addChild(buttons());
        group.addChild(display());
        return group;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        treeTransform3D.rotY(angle);
        treeTransformGroup.setTransform(treeTransform3D);
        angle -= 0.25;
    }


    public BranchGroup display() {
        BranchGroup group = new BranchGroup();
        Appearance appearance = new Appearance();
        ColoringAttributes attr = new ColoringAttributes();
        attr.setColor(new Color3f(1.7f, 1.6f, .0f));
        Color3f ambient = new Color3f(0.2f, 0.15f, .15f);
        Color3f diffuse = new Color3f(1.2f, 1.15f, .15f);
        Color3f specular = new Color3f(0.0f, 0.0f, 0.0f);
        appearance.setMaterial(new Material(ambient,
                new Color3f(1.2f, 0.5f, 0.5f), diffuse, specular, 1.0f));

        Box box = new Box(0.25f, 0.18f, 0.05f, appearance);
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        box.setAppearance(appearance);
        Vector3f vector = new Vector3f(0.01f, 0.4f, .06f);
        transform.setTranslation(vector);
        tg.setTransform(transform);
        tg.addChild(box);
        group.addChild(tg);
        return group;
    }

    public BranchGroup buttons() {
        BranchGroup group = new BranchGroup();

        Appearance appearance = new Appearance();
        ColoringAttributes attr = new ColoringAttributes();
        attr.setColor(new Color3f(0.5f, 1.0f, .3f));
        Color3f ambient = new Color3f(0.2f, 0.15f, .8f);
        Color3f diffuse = new Color3f(0.5f, 0.15f, .15f);
        Color3f specular = new Color3f(0.1f, 0.8f, 0.5f);
        appearance.setMaterial(new Material(ambient,
                new Color3f(0.5f, 0.5f, 0.5f), diffuse, specular, 1.0f));
        for (float yoffset = -0.6f; yoffset < 0.1f; yoffset += 0.2f) {
            for (float offset = -0.2f; offset < 0.3f; offset += 0.2f) {

                TransformGroup tg = new TransformGroup();
                Transform3D transform = new Transform3D();
                Box btn = new Box(0.05f, 0.05f, 0.05f, appearance);

                Vector3f vector = new Vector3f(offset, yoffset, .06f);
                transform.setTranslation(vector);
                tg.setTransform(transform);
                tg.addChild(btn);
                group.addChild(tg);
            }
        }

        return group;
    }

    public BranchGroup sceneSetup() {
        // створюємо групу об'єктів
        BranchGroup objRoot = new BranchGroup();
        // створюємо об'єкт, що будемо додавати до групи
        treeTransformGroup = new TransformGroup();
        treeTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        objRoot.addChild(treeTransformGroup);
        // налаштовуємо освітлення
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 500.0);
        Color3f light1Color = new Color3f(0f, 0f, 0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);
        // встановлюємо навколишнє освітлення
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        return objRoot;
    }
}