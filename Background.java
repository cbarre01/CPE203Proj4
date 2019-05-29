import java.util.List;
import processing.core.PImage;

final class Background
{
   private List<PImage> images;
   private int imageIndex;

   public Background(List<PImage> images)
   {
      this.images = images;
   }

   public List<PImage> getImages() {
      return images;
   }


   public int getImageIndex() {
      return imageIndex;
   }

   public PImage getCurrentImage()
   {
         return (this).getImages()
                 .get((this).getImageIndex());
   }




}
