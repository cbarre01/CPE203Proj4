import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MinerNotFull extends Moving {

    private int resourceLimit;
    private int resourceCount;
    private PathingStrategy pathing = new SingleStepPathingStrategy();

    public MinerNotFull(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod) {
        this.setId(id);
        this.setPosition(position);
        this.setImages(images);
        this.setImageIndex(0);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.setActionPeriod(actionPeriod);
        this.setAnimationPeriod(animationPeriod);
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(),
                Ore.class);
        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
        }
    }


    public boolean moveTo(WorldModel world,
                           Entity target, EventScheduler scheduler) {
        if (adjacent(getPosition(), target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            MinerFull miner = createMinerFull(getId(), resourceLimit,
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public Point nextPosition(WorldModel world,
                              Point destPos) {
        Predicate<Point> canPassThrough = new Predicate<Point>()
        {
            public boolean test(Point p)
            {
                if (world.isOccupied(p))
                {
                    return false;
                }
                return true;

            }
        };

        BiPredicate<Point, Point> withinReach = new BiPredicate<Point, Point>() {
            @Override
            public boolean test(Point point, Point point2) {
                return false;
            }
        };


        List<Point> path = pathing.computePath(getPosition(), destPos, canPassThrough,withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        Point newPos = getPosition();
        if (path.size() > 0)
        {
            newPos = path.get(0);
        }
        return newPos;
    }


    private MinerFull createMinerFull(String id, int resourceLimit,
                                   Point position, int actionPeriod, int animationPeriod,
                                   List<PImage> images) {
        return new MinerFull(id, position, images,
                resourceLimit, actionPeriod, animationPeriod);
    }



    public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }


}


