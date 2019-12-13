package diaporama.views.transitions;

import diaporama.ProgramParameters;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.util.Random;

/**
 * contains the transition animations for the images
 */
public enum Type {
    FADE(p -> {
        FadeTransition sft = new FadeTransition(Duration.millis(p.getImageFadeTime()));
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);
        return sft;
    }, p -> {
        FadeTransition eft = new FadeTransition(Duration.millis(p.getImageFadeTime()));
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);
        return eft;
    }),

    SCALE_HOR(p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleHorTime()));
        sft.setFromX(0.0);
        sft.setToX(1.0);
        sft.setCycleCount(1);
        return sft;
    }, p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleHorTime()));
        sft.setFromX(1.0);
        sft.setToX(0.0);
        sft.setCycleCount(1);
        return sft;
    }),
    SCALE_VERT(p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleVertTime()));
        sft.setFromY(0.0);
        sft.setToY(1.0);
        sft.setCycleCount(1);
        return sft;
    }, p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleVertTime()));
        sft.setFromY(1.0);
        sft.setToY(0.0);
        sft.setCycleCount(1);
        return sft;
    }),
    SCALE(p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleTime()));
        sft.setFromX(0.0);
        sft.setToX(1.0);
        sft.setFromY(0.0);
        sft.setToY(1.0);
        sft.setCycleCount(1);
        return sft;
    }, p -> {
        ScaleTransition sft = new ScaleTransition(Duration.millis(p.getImageScaleTime()));
        sft.setFromX(1.0);
        sft.setToX(0.0);
        sft.setFromY(1.0);
        sft.setToY(0.0);
        sft.setCycleCount(1);
        return sft;
    }),
    ;
    private static final Random rdm = new Random();
    private static final Type[] values = Type.values();

    private TransitionInstantiation startAnimation;
    private TransitionInstantiation endAnimation;

    Type(TransitionInstantiation startAnimation, TransitionInstantiation endAnimation) {
        this.startAnimation = startAnimation;
        this.endAnimation = endAnimation;
    }

    public Animation createStartAnimation(ProgramParameters param){
        return startAnimation.instantiateAnimation(param);
    }

    public Animation createEndAnimation(ProgramParameters param){
        return endAnimation.instantiateAnimation(param);
    }

    public static Type getRandomType(){
        int num = rdm.nextInt(values.length);

        return values[num];
    }

}
