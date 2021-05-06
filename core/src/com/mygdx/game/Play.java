package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Play implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Player player;

    private ShapeRenderer shapeSpace;

    @Override
    public void render(float delta){
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 2);
		camera.update();

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());

        // check user input
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        	if(Player.collisionY || Player.collisionX)
        	{
        		player.setX(player.getX() - 0);
        	}
        	else
        	{
        		player.setX(player.getX() - 1);
        	}
            
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setX(player.getX() + 1);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setY(player.getY() + 1);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setY(player.getY() - 1);

        shapeSpace.setProjectionMatrix(camera.combined);

        // render objects
//        for(MapObject object : map.getLayers().get("collision").getObjects()){
//            if(object instanceof RectangleMapObject){
//                Rectangle rect = ((RectangleMapObject) object).getRectangle();
//                shapeSpace.begin(ShapeRenderer.ShapeType.Filled);
//                shapeSpace.rect(rect.x, rect.y, rect.width, rect.height);
//                shapeSpace.setColor(0,0,0,0);
//                shapeSpace.end();
//            } else if(object instanceof CircleMapObject){
//                Circle circle = (((CircleMapObject) object).getCircle());
//                shapeSpace.begin(ShapeRenderer.ShapeType.Filled);
//                shapeSpace.circle(circle.x, circle.y, circle.radius);
//                shapeSpace.setColor(0,0,0,0);
//                shapeSpace.end();
//            } else if(object instanceof PolylineMapObject){
//                Polyline line = (((PolylineMapObject) object).getPolyline());
//                shapeSpace.begin(ShapeRenderer.ShapeType.Line);
//                shapeSpace.setColor(0,0,0,0);
//                shapeSpace.polyline(line.getTransformedVertices());
//                shapeSpace.end();
//            } else if(object instanceof PolygonMapObject){
//                Polygon polygon = (((PolygonMapObject) object).getPolygon());
//                shapeSpace.begin(ShapeRenderer.ShapeType.Line);
//                shapeSpace.polygon(polygon.getTransformedVertices());
//                shapeSpace.setColor(0,0,0,0);
//                shapeSpace.end();
//            } else if(object instanceof EllipseMapObject){
//                Ellipse ellipse = (((EllipseMapObject) object).getEllipse());
//                shapeSpace.begin(ShapeRenderer.ShapeType.Filled);
//                shapeSpace.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
//                shapeSpace.setColor(0,0,0,0);
//                shapeSpace.end();
//            }
//        }

        renderer.getBatch().end();
    }

    @Override
    public void show(){
        map = new TmxMapLoader().load("Map1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        shapeSpace = new ShapeRenderer();
        shapeSpace.setColor(0,0,0,0); //sets the collision lines clear
        Gdx.gl.glClearColor(0,0,0,0); //trying to set lines to clear


        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("charRed.png")) , (TiledMapTileLayer) map.getLayers().get(0));
        //player.setPosition(-10, 100);
        player.setPosition(11 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 14) * player.getCollisionLayer().getTileHeight());

        // frames
        Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(2);

        // get frame tiles
        Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet("tiles").iterator();
        while(tiles.hasNext()){
            TiledMapTile tile = tiles.next();
            if(tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class)
            .equals("<TILE_NAME>")){
                frameTiles.add((StaticTiledMapTile) tile);
            }
        }

        // background layer
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Background");

        // create the animated tile
        AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 3f, frameTiles);

        // replace frames with animated tile
        for(int i = 0; i < layer.getWidth(); i++){
            for(int j = 0; j < layer.getHeight(); j++){
                TiledMapTileLayer.Cell cell = layer.getCell(i,j);
                if(cell.getTile().getProperties().containsKey("animation") && cell.getTile().getProperties()
                    .get("animation", String.class).equals("<TILE_NAME>")){
                    cell.setTile(animatedTile);
                }

            }
        }



    }

    @Override
    public void hide(){
        dispose();
    }

    @Override
    public void resize(int width, int height){
    	camera.viewportWidth = width / 3.05f;
		camera.viewportHeight = height / 2.2f;
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void dispose () {
        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();
    }
}
