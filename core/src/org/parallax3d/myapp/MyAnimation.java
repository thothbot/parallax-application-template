package org.parallax3d.myapp;

import org.parallax3d.parallax.AnimationAdapter;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.core.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.core.FontData;
import org.parallax3d.parallax.graphics.extras.core.TextGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.loaders.FontLoadHandler;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.TypefacejsLoader;
import org.parallax3d.parallax.math.Color;

public class MyAnimation extends AnimationAdapter implements TouchMoveHandler
{
    // Links to resources in /core/assets
    static final String font = "helvetiker_bold.typeface.js";

    PerspectiveCamera camera;

    // Main scene
    Scene scene;

    Mesh particleLight;

    int width = 0, height = 0;
    int mouseX = 0, mouseY = 0;

    // Called when animation is loaded and each time when rendering context is resized
    @Override
    public void onResize(RenderingContext context) {
        width = context.getWidth();
        height = context.getHeight();
    }

    // Called for init animation
    @Override
    public void onStart(RenderingContext context)
    {
        // Init main scene
        scene = new Scene();
        // Init camera
        camera = new PerspectiveCamera(
                45, // fov
                context.getAspectRation(), // aspect
                1, // near
                2000 // far
        );

        // Set position of camera instead of (0,0,0)
        camera.getPosition().set( 200, 200, 200 );

        // Init main material for reflection
        final MeshPhongMaterial material = new MeshPhongMaterial()
                .setSpecular( 0xffffff )
                .setShininess( 100 )
                .setShading(Material.SHADING.SMOOTH);

        // Load font
        new TypefacejsLoader(font, new FontLoadHandler() {
            @Override
            public void onFontLoaded(Loader loader, FontData fontData) {

                // Some parameters for extrusion, which is used in text geometry
                ExtrudeGeometry.ExtrudeGeometryParameters params = new ExtrudeGeometry.ExtrudeGeometryParameters();
                params.amount = 10;
                params.curveSegments = 3;
                params.height = 5;
                params.bevelThickness = 2;
                params.bevelSize = 1;
                params.bevelEnabled = true;

                // Init text geometry, using text, font data, font size and extrusion parameters
                TextGeometry geometry = new TextGeometry("Parallax", fontData, 30, params);
                geometry.center();
                geometry.computeFaceNormals();
                // Add text object with defined material into the scene
                scene.add(new Mesh(geometry, material));
            }

        });

        // Lights
        MeshBasicMaterial particleLightMaterial = new MeshBasicMaterial();
        particleLightMaterial.setColor(new Color(0xffffff));
        particleLight = new Mesh( new SphereGeometry( 4, 15, 15 ), particleLightMaterial );
        scene.add( particleLight );

        scene.add( new AmbientLight( 0x404040 ) );

        DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 0.5 );
        directionalLight.getPosition().set( 1 ).normalize();
        scene.add( directionalLight );

        PointLight pointLight = new PointLight( 0x0011FF, 1, 500 );
        scene.add( pointLight );

        ((MeshBasicMaterial)particleLight.getMaterial()).setColor( pointLight.getColor() );
        pointLight.setPosition( particleLight.getPosition() );
    }

    // Called each time when need to update the scene
    @Override
    public void onUpdate(RenderingContext context)
    {
        double timer = context.getFrameId() * 0.0025;

        camera.getPosition().addX(( mouseX - camera.getPosition().getX() ) * .05);
        camera.getPosition().addY(( - mouseY - camera.getPosition().getY() ) * .05);

        camera.lookAt( scene.getPosition() );

        particleLight.getPosition().set(
                Math.cos( timer * 7 ) * 100,
                Math.sin( timer * 5 ) * 200,
                Math.sin( timer * 3 ) * 100 );

        context.getRenderer().render( scene, camera );
    }

    // Called when mouse is moved
    @Override
    public void onTouchMove(int screenX, int screenY, int pointer) {
        mouseX = (screenX - width / 2 );
        mouseY = (screenY - height / 2);
    }
}
