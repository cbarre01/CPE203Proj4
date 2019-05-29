import processing.core.PImage;

import java.util.List;

public class Blacksmith extends Entity {

    public Blacksmith(String id, Point position,
                           List<PImage> images) {
        this.setPosition(position);
        this.setImages(images);
        this.setImageIndex(0);
    }


    public static Blacksmith createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
        return new Blacksmith(id, position, images
        );
    }





}
