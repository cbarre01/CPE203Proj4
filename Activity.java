public class Activity implements Action {
    private Actor entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Actor entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {

        entity.executeActivity(world,
                imageStore, scheduler);

    }


}
