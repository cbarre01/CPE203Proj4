import java.util.Random;

abstract class Actor extends Entity {

    private String id;
    private int actionPeriod;

    Random rand = new Random();

    protected Action createActivityAction(WorldModel world,
                                       ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }


    protected int getActionPeriod() {
        return actionPeriod;
    }
    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {


            scheduler.scheduleEvent( this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
            return;

    }

    protected String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }
}
