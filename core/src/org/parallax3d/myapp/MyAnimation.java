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
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.loaders.FontLoadHandler;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.TypefacejsLoader;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;

public class MyAnimation extends AnimationAdapter implements TouchMoveHandler
{

    static final String font = "helvetiker_bold.typeface.js";
    static final String textures = "park2/*.jpg";

    PerspectiveCamera camera;

    Scene scene;
    PointLight pointLight;

    int width = 0, height = 0;
    int mouseX = 0, mouseY = 0;

    @Override
    public void onResize(RenderingContext context) {
        width = context.getWidth();
        height = context.getHeight();
    }

    @Override
    public void onStart(RenderingContext context)
    {
        scene = new Scene();
        camera = new PerspectiveCamera(
                45, // fov
                context.getRenderer().getAbsoluteAspectRation(), // aspect
                1, // near
                2000 // far
        );

        camera.getPosition().setZ( 200 );

        CubeTexture reflectionCube = new CubeTexture( textures );
        reflectionCube.setFormat(PixelFormat.RGB);

        final MeshPhongMaterial material = new MeshPhongMaterial()
                .setSpecular( 0xffffff )
                .setShininess( 100 )
                .setEnvMap( reflectionCube )
                .setCombine( Texture.OPERATIONS.MIX )
                .setReflectivity( 0.9 )
                .setWrapAround(true)
                .setSide(Material.SIDE.DOUBLE);

        new TypefacejsLoader(font, new FontLoadHandler() {
            @Override
            public void onFontLoaded(Loader loader, FontData fontData) {

                ExtrudeGeometry.ExtrudeGeometryParameters params = new ExtrudeGeometry.ExtrudeGeometryParameters();
                params.amount = 10;
                params.curveSegments = 3;
                params.height = 5;
                params.bevelThickness = 2;
                params.bevelSize = 1;
                params.bevelEnabled = true;

                TextGeometry geometry = new TextGeometry("Parallax", fontData, 30, params);
                geometry.center();
                geometry.computeFaceNormals();
                scene.add(new Mesh(geometry, material));
            }

        });

        context.getRenderer().setGammaInput(true);
        context.getRenderer().setGammaOutput(true);
        context.getRenderer().setClearColor( 0x2196F3 );
    }

    @Override
    public void onUpdate(RenderingContext context)
    {
        double timer = context.getFrameId() * 0.0025;

        camera.getPosition().addX(( mouseX - camera.getPosition().getX() ) * .05);
        camera.getPosition().addY(( - mouseY - camera.getPosition().getY() ) * .05);

        camera.lookAt( scene.getPosition() );

        context.getRenderer().render(scene, camera);
    }

    @Override
    public void onTouchMove(int screenX, int screenY, int pointer) {
        mouseX = (screenX - width / 2 );
        mouseY = (screenY - height / 2);
    }
}
