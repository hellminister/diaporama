package diaporama.views;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.ImageLoader;
import diaporama.views.transitions.Type;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class creates, manages and plays the starting and ending transition when an image changes
 */
public class RandomImageTransitioning extends Transitioning<ImageView, ImageLoader> {
    private static final Logger LOG = Logger.getLogger(RandomImageTransitioning.class.getName());

    private final Map<Type, Animation> startTransition;

    private final PauseTransition showTransition;

    private final Map<Type, Animation> endTransition;

    private final String transitionType;
    private final Type onlyMe;

    /**
     * creates and sets the elements of this transition
     *
     * @param prod the media producer
     * @param ds   the owning DiaporamaScreen
     * @param param  The program parameters
     */
    protected RandomImageTransitioning(ImageLoader prod, DiaporamaScreen ds, ProgramParameters param) {
        super(prod, new ImageView(), ds);

        transitionType = param.getImageTransitionType();

        Type onlyMe1;
        try{
            onlyMe1 = Type.valueOf(transitionType);
        } catch (IllegalArgumentException ex) {
            onlyMe1 = null;
        }

        onlyMe = onlyMe1;

        startTransition = new EnumMap<>(Type.class);
        endTransition = new EnumMap<>(Type.class);

        for (Type t : Type.values()){
            startTransition.put(t, t.createStartAnimation(param));
            endTransition.put(t, t.createEndAnimation(param));
        }

        showTransition = new PauseTransition(Duration.millis(param.getImageShowTime()));
        showTransition.setCycleCount(1);
    }

    /**
     * gets the image to show and chooses the next start and end animations
     * @throws InterruptedException no image is available
     */
    @Override
    protected void changeMediaToShow() throws InterruptedException {
        shower.setImage(producer.getNext());
        transition.getChildren().clear();

        Animation start;
        Animation end;

        if (onlyMe != null){
            start = startTransition.get(onlyMe);
            end = endTransition.get(onlyMe);
        } else if ("LinkedRandom".equals(transitionType)){
            Type type = Type.getRandomType();
            start = startTransition.get(type);
            end = endTransition.get(type);
            LOG.info(() -> "Got " + type);
        } else {
            start = startTransition.get(Type.getRandomType());
            LOG.info(() -> start.getClass().getName() + " Start");
            end = endTransition.get(Type.getRandomType());
            LOG.info(() -> end.getClass().getName() + " End");
        }

        transition.getChildren().addAll(start, showTransition, end);
    }

}
