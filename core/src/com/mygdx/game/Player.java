package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    private Vector2 velocity = new Vector2();
    private float speed = 60 * 2;
    private float gravity = 60 *1.8f;
    private TiledMapTileLayer collisionLayer;
    static boolean collisionX = false;
    static boolean collisionY = false;

    public Player(Sprite sprite, TiledMapTileLayer tiledMapLayer){ // ,TiledMapTileLayer collisionLayer
        super(sprite);
        this.collisionLayer = tiledMapLayer;

        setScale(0.2F);
    }


    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta){
        if(delta > 1 / 60f){
            delta = 1 / 60f;
        }

        // applies gravity by delta or fps of game
        //velocity.y -= gravity * delta;

        // clamp velocity
        if(velocity.y > speed){
            velocity.y = speed;
        }else if(velocity.y < speed){
            velocity.y = -speed;
        }

        // save old position
        float oldX = getX();
        float oldY = getY();
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();


        // move on x
        setX(getX() + velocity.x * delta);
        if(velocity.x < 0){
            // top left
            collisionX = isCellBlocked(getX(), getY() + getHeight());
            collisionX = true;
            // middle left
            if(!collisionX)
            {
            	collisionX = isCellBlocked(getX(), getY() + getHeight()/2);
            	collisionX = true;
            }
          
            // bottom left
            if(!collisionX)
            {
            	collisionX = isCellBlocked(getX()+ getWidth(), getY());
            	collisionX = true;
            }
            
        }else if(velocity.x > 0){
            // top right
            collisionX = isCellBlocked(getX() + getWidth(), getY() + getHeight());
            collisionX = true;
            // middle right
            if(!collisionX)
            {
            	collisionX = isCellBlocked(getX() + getWidth(), getY() + getHeight()/2);
            	collisionX = true;
            }
          
            // bottom right
            if(!collisionX)
            {
            	collisionX = isCellBlocked(getX()+ getWidth(), getY());
            	collisionX = true;
            }
        }

        //react to x collision
        if(collisionX)
        {
        	setX(oldX);
        	velocity.x = 0;
        }
        // move on y
        setY(getY() + velocity.y * delta);
        if(velocity.y < 0){
        	collisionY = isCellBlocked(getX(), getY());
        	collisionY = true;
            // top middle
            if(!collisionY)
            {
            	collisionY = isCellBlocked(getX() + getWidth()/2, getY());
            	collisionY = true;
            }
          
            // top right
            if(!collisionY)
            {
            	collisionY = isCellBlocked(getX() + getWidth(), getY());
            	collisionY = true;
            }
            
        }else if(velocity.y > 0){
            // top left
            collisionY = isCellBlocked(getX(), getY() + getHeight());
            collisionY = true;
            // top middle
            if(!collisionY)
            {
            	collisionY = isCellBlocked(getX() + getWidth()/2, getY() + getHeight());
            	collisionY = true;
            }
          
            // top right
            if(!collisionY)
            {
            	collisionY = isCellBlocked(getX() + getWidth(), getY() + getHeight());
            	collisionY = true;
            }
            
        }
        //react to Y collision
        if(collisionY)
        {
        	setY(oldY);
        	velocity.y = 0;
        }
    }
    private boolean isCellBlocked(float x ,float y)
    {
    	Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y/collisionLayer.getTileHeight()));
    	return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
    }
    public Vector2 getVelocity()
    {
    	return velocity;
    }
    
    public void setVelocity(Vector2 velocity)
    {
    	this.velocity = velocity;
    }
    public float getSpeed()
    {
    	return speed;
    }
    
    public void setSpeed(float speed)
    {
    	this.speed = speed;
    }
    
    public float getGravity()
    {
    	return gravity;
    }
    
    public void setGravity()
    {
    	this.gravity = gravity;
    }
    public TiledMapTileLayer getCollisionLayer()
    {
    	return collisionLayer;
    }
    public void setCollisionLayer(TiledMapTileLayer  collisionLayer)
    {
    	this.collisionLayer = collisionLayer;
    }

}
