package org.parallax3d.myapp;

import org.parallax3d.parallax.AnimationAdapter;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.core.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.core.FontData;
import org.parallax3d.parallax.graphics.extras.core.TextGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.CubeShader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.loaders.FontLoadHandler;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.TypefacejsLoader;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;

public class MyAnimation extends AnimationAdapter implements TouchMoveHandler
{

    // Links to resources in /core/assets
    static final String font = "helvetiker_bold.typeface.js";
    static final String textures = "park2/*.jpg";

    PerspectiveCamera camera;

    // Main scene
    Scene scene;

    // Skybox
    Scene sceneCube;
    PerspectiveCamera cameraCube;

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
        camera.getPosition().setZ( 200 );

        // Init cube texture
        CubeTexture textureCube = new CubeTexture( textures );
        textureCube.setFormat(PixelFormat.RGB);

        // Init main material for reflection
        final MeshPhongMaterial material = new MeshPhongMaterial()
                .setSpecular( 0xffffff )
                .setShininess( 100 )
                .setEnvMap( textureCube )
                .setCombine( Texture.OPERATIONS.MIX )
                .setReflectivity( 0.95 )
                .setWrapAround(true);

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

        // Skybox
        ShaderMaterial sMaterial = new ShaderMaterial(new CubeShader())
                .setDepthWrite( false )
                .setSide( Material.SIDE.BACK );
        sMaterial.getShader().getUniforms().get("tCube").setValue( textureCube );

        sceneCube = new Scene();
        cameraCube = new PerspectiveCamera(
                45, // fov
                context.getAspectRation(), // aspect
                1, // near
                100000 // far
        );

        sceneCube.add( new Mesh( new BoxGeometry( 500, 500, 500 ), sMaterial ) );

        context.getRenderer().setAutoClear(false);
    }

    // Called each time when need to update the scene
    @Override
    public void onUpdate(RenderingContext context)
    {
        camera.getPosition().addX(( mouseX - camera.getPosition().getX() ) * .05);
        camera.getPosition().addY(( - mouseY - camera.getPosition().getY() ) * .05);

        camera.lookAt( scene.getPosition() );

        cameraCube.getRotation().copy( camera.getRotation() );

        context.getRenderer().render( sceneCube, cameraCube );
        context.getRenderer().render( scene, camera );
    }

    // Called when mouse is moved
    @Override
    public void onTouchMove(int screenX, int screenY, int pointer) {
        mouseX = (screenX - width / 2 );
        mouseY = (screenY - height / 2);
    }
}
