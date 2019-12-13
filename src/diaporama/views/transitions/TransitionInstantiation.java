package diaporama.views.transitions;

import diaporama.ProgramParameters;
import javafx.animation.Animation;

@FunctionalInterface
public interface TransitionInstantiation {
    Animation instantiateAnimation(ProgramParameters param);
}
