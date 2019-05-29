abstract class Animated extends Actor{
    private int animationPeriod;

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    protected int getAnimationPeriod() {

        return this.animationPeriod;
    }

    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {


        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, this.createAnimationAction(0),
                this.getAnimationPeriod());
        return;

    }


    protected void setAnimationPeriod(int animationPeriod) {
        this.animationPeriod = animationPeriod;
    }
}
