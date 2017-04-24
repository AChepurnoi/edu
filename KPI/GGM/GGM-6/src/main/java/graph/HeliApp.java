package graph;

import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.awt.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class HeliApp extends JFrame
{
    //The canvas to be drawn upon.
    public Canvas3D myCanvas3D;

    public HeliApp() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);
        simpUniv.getViewingPlatform().setNominalViewingTransform();
        createSceneGraph(simpUniv);
        addLight(simpUniv);
        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);
        setTitle("Helicopter");
        setSize(700, 700);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }
    public static void main(String[] args)
    {
        HeliApp cockroach = new HeliApp();
    }
    public void createSceneGraph(SimpleUniverse su)
    {
        ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
        Scene cockroachScene = null;
        try
        {
            cockroachScene = f.load("GGM-6/files/helicopter.obj");
        }
        catch (Exception e)
        {
            System.out.println("File loading failed:" + e);
        }
        Transform3D scaling = new Transform3D();
        scaling.setScale(1.0/6);
        Transform3D tfRoach = new Transform3D();
        tfRoach.rotX(Math.PI/2);
        tfRoach.mul(scaling);
        TransformGroup tgRoach = new TransformGroup(tfRoach);
        TransformGroup sceneGroup = new TransformGroup();
//        sceneGroup.addChild(cockroachScene.getSceneGroup());

        Hashtable roachNamedObjects = cockroachScene.getNamedObjects();
        Enumeration enumer = roachNamedObjects.keys();
        String name;
        while (enumer.hasMoreElements())
        {
            name = (String) enumer.nextElement();
            System.out.println("Name: "+name);
        }
        Appearance lightApp = new Appearance();
        setToMyDefaultAppearance(lightApp,new Color3f(0f,0.1f,0f));

        Appearance lightApp1 = new Appearance();
        setToMyDefaultAppearance(lightApp1,new Color3f(0.5f,0.1f,0f));

        Shape3D wheel1 = (Shape3D) roachNamedObjects.get("big_propeller");
        wheel1.setAppearance(lightApp);
        Shape3D wheel2 = (Shape3D) roachNamedObjects.get("small_propeller");
        wheel2.setAppearance(lightApp);
        Shape3D platinum ;

        TransformGroup helic = new TransformGroup();
        roachNamedObjects.forEach((k, v) -> {
            if(k.equals("big_propeller") || k.equals("small_propeller")){
                System.out.println("VSE HOROSHO");
            }else helic.addChild(((Shape3D)v).cloneTree());
        });

        TextureLoader loader = new TextureLoader("GGM-6/files/metal.jpg", "LUMINACE", new Container());
        Texture texture = loader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 1.0f, 0.0f));

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        Appearance ap = new Appearance();

        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);

//        platinum.setAppearance(lightApp1);


//        TransformGroup wheels = new TransformGroup();
        TransformGroup wheel1gr = new TransformGroup();
        TransformGroup wheel2gr = new TransformGroup();
        wheel1gr.addChild(wheel1.cloneTree());
        wheel2gr.addChild(wheel2.cloneTree());
//        Transform3D translateTransform= new Transform3D();
//        rotateTransformY.rotY(Math.PI/2);
//        translateTransform.mul(rotateTransformY);

        BoundingSphere bounds = new BoundingSphere(new Point3d(120.0,250.0,100.0),Double.MAX_VALUE);
        BranchGroup theScene = new BranchGroup();
        Transform3D tCrawl = new Transform3D();
        Transform3D tCrawl1 = new Transform3D();
        tCrawl.rotY(-Math.PI/2);
        tCrawl1.rotX(-Math.PI/2);
        long crawlTime = 15000;//Ñ‡Ð°Ñ, Ð·Ð° ÑÐºÐ¸Ð¹ Ñ‚Ð°Ñ€Ð³Ð°Ð½ Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð²Ð·Ðµ ÐµÐºÑ€Ð°Ð½
        Alpha crawlAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                0, crawlTime,0,0,0,0,0);
        float crawlDistance = -5.0f; //Ð²Ñ–Ð´ÑÑ‚Ð°Ð½ÑŒ, Ð½Ð° ÑÐºÑƒ Ð¿Ñ€Ð¾ÑÑƒÐ½ÐµÑ‚ÑŒÑÑ Ð¾Ð±â€™Ñ”ÐºÑ‚
        PositionInterpolator posICrawl = new PositionInterpolator(crawlAlpha,
                sceneGroup,tCrawl, 9.0f, crawlDistance);

        BoundingSphere bs = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
        posICrawl.setSchedulingBounds(bs);
        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        sceneGroup.addChild(posICrawl);

        Alpha upRamp = new Alpha();

        upRamp.setIncreasingAlphaDuration(800);
        upRamp.setLoopCount(-1);

        Transform3D wheelSpinner = new Transform3D();
        Transform3D transform1 = new Transform3D();
        Transform3D transform2 = new Transform3D();

        wheelSpinner.rotX(Math.PI*2);
        wheelSpinner.setTranslation(new Vector3f(0.5f,0f,0f));
//        transform.set(0.0f);
        transform2.rotZ(-Math.PI/2);
        transform2.setTranslation(new Vector3f(0.5f, 0.075f, 0.82f));

        transform1.rotY(-Math.PI/2);
        transform1.setTranslation(new Vector3f(0.0f, 0.2f, -0.22f));

//        transform.mul(wheelSpinner, transform);
//        wheelSpinner.set

        RotationInterpolator mySpinner1 = new RotationInterpolator(upRamp, wheel1gr,
                transform1, 0.0f,(float)(Math.PI * 2.0) );
        RotationInterpolator mySpinner2 = new RotationInterpolator(upRamp, wheel2gr,
                transform2, 0.0f,(float)(Math.PI * 2.0) );

//        RotationPathInterpolator
        mySpinner1.setSchedulingBounds(bounds);
        mySpinner2.setSchedulingBounds(bounds);

        wheel1gr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        wheel2gr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        wheel1gr.addChild(mySpinner1);
        wheel2gr.addChild(mySpinner2);

        sceneGroup.addChild(helic);
        sceneGroup.addChild(wheel1gr);
        sceneGroup.addChild(wheel2gr);
        tgRoach.addChild(sceneGroup); // 000000000000000000000000
        theScene.addChild(tgRoach);//Ð´Ð¾Ð´Ð°Ð²Ð°Ð½Ð½Ñ Ð¾Ð±'Ñ”ÐºÑ‚Ñƒ Ñƒ ÑÑ†ÐµÐ½Ñƒ
//ÑÑ‚Ð²Ð¾Ñ€ÑŽÑ”Ð¼Ð¾ Ð±Ñ–Ð»Ð¸Ð¹ Ñ„Ð¾Ð½
        Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
        bg.setApplicationBounds(bounds);
        theScene.addChild(bg);
        theScene.compile();
//Ð´Ð¾Ð´Ð°Ñ”Ð¼Ð¾ ÑÑ†ÐµÐ½Ñƒ Ð´Ð¾ Ð²Ñ–Ñ€Ñ‚ÑƒÐ°Ð»ÑŒÐ½Ð¾Ð³Ð¾ Ð²ÑÐµÑÐ²Ñ–Ñ‚Ñƒ
        su.addBranchGraph(theScene);
    }
    //Ð¼ÐµÑ‚Ð¾Ð´ Ð´Ð»Ñ Ð³ÐµÐ½ÐµÑ€Ð°Ñ†Ñ–Ñ— Ð·Ð¾Ð²Ð½Ñ–ÑˆÐ½ÑŒÐ¾Ñ— Ð¿Ð¾Ð²ÐµÑ€Ñ…Ð½Ñ–
    public static void setToMyDefaultAppearance(Appearance app, Color3f col)
    {
        app.setMaterial(new Material(col,col,col,col,150.0f));
    }
    //Ð¼ÐµÑ‚Ð¾Ð´ Ð´Ð»Ñ Ð´Ð¾Ð´Ð°Ð²Ð°Ð½Ð½Ñ Ð¾ÑÐ²Ñ–Ñ‚Ð»ÐµÐ½Ð½Ñ
    public void addLight(SimpleUniverse su)
    {
        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
        Vector3f lightDir1 = new Vector3f(-1.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);
        bgLight.addChild(light1);
        su.addBranchGraph(bgLight);
    }
}
