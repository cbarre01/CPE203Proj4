import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {



    public Obstacle(String id, Point position,
                    List<PImage> images) {
        this.setPosition(position);
        this.setImages(images);
        this.setImageIndex(0);
    }



    public static Obstacle createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(id, position, images
        );
    }





}

