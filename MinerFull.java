import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.*;

public class MinerFull extends Moving {

    private int resourceLimit;
    private PathingStrategy pathing = new SingleStepPathingStrategy();

    public MinerFull(String id, Point position,
                     List<PImage> images, int resourceLimit,
                     int actionPeriod, int animationPeriod) {
        this.setId(id);
        this.setPosition(position);
        this.setImages(images);
        this.setImageIndex(0);
        this.resourceLimit = resourceLimit;
        this.setActionPeriod(actionPeriod);
        this.setAnimationPeriod(animationPeriod);
    }





    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world,
                           Entity target, EventScheduler scheduler) {
        if (adjacent(getPosition(), target.getPosition())) {
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

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore) {
        MinerNotFull miner = createMinerNotFull(getId(), resourceLimit,
                getPosition(), getActionPeriod(), getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public Point nextPosition(WorldModel world,
                              Point destPos) {
        Predicate<Point> canPassThrough = new Predicate<Point>()
        {
            public boolean test(Point p)
            {
                Optional<Entity> occupant = world.getOccupant(p);
                if (occupant != null)
                {
                    return true;
                }
                return false;

            }
        };

        BiPredicate<Point, Point> withinReach = new BiPredicate<Point, Point>() {
            @Override
            public boolean test(Point point, Point point2) {
                return false;
            }
        };


        List<Point> path = pathing.computePath(getPosition(), destPos, canPassThrough,withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        Point newPos = path.get(0);
        return newPos;
    }

/*
    public Point nextPosition(WorldModel world,
                               Point destPos) {
        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz,
                getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(),
                    getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = getPosition();
            }
        }

        return newPos;
    }
*/
    public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                                  Point position, int actionPeriod, int animationPeriod,
                                                  List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }








}
