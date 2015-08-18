package uk.ac.ebi.fgpt.zooma.model;

/**
 * A ZOOMA prediction for a new annotation, based on a search and the best available matched existing annotation ZOOMA
 * could find.  Annotation predictions are extensions of an {@link Annotation} but with an indication of confidence in
 * how likely ZOOMA considers this prediction to be the correct one.
 *
 * @author Tony Burdett
 * @date 14/08/15
 */
public interface AnnotationPrediction extends Annotation {
    /**
     * A measure of the confidence ZOOMA has in the quality of this prediction.  May be high, good, medium or low.  You
     * should only really consider HIGH confidence matches to be worthy of use in automated processes - other levels of
     * confidence should be reviewed before accepting.  Low confidence matches can be used to identify areas of need.
     *
     * @return the confidence ZOOMA has in this annotation prediction
     */
    Confidence getConfidence();

    enum Confidence {
        HIGH,
        GOOD,
        MEDIUM,
        LOW
    }
}
