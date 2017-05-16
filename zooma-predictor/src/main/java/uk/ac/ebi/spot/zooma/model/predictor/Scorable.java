package uk.ac.ebi.spot.zooma.model.predictor;

import java.io.Serializable;

/**
 * Interface for any objects that can be assigned a quality value
 *
 * @author Tony Burdett
 * @date 30/11/13
 */
public interface Scorable extends Serializable {
    /**
     * Returns a metric measuring the quality score of this object.
     *
     * @return a float representing the quality of this object
     */
    float getScore();
}