package org.lenskit.mooc.cbf;

import org.lenskit.data.ratings.Rating;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Build a user profile from all positive ratings.
 */
public class WeightedUserProfileBuilder implements UserProfileBuilder {
    /**
     * The tag model, to get item tag vectors.
     */
    private final TFIDFModel model;

    @Inject
    public WeightedUserProfileBuilder(TFIDFModel m) {
        model = m;
    }

    @Override
    public Map<String, Double> makeUserProfile(@Nonnull List<Rating> ratings) {
        // Create a new vector over tags to accumulate the user profile
        Map<String,Double> profile = new HashMap<>();

        // TODO Normalize the user's ratings
        double mean = 0.0;
        for (Rating r: ratings){
            mean += r.getValue();
        }
        mean = mean / ratings.size();

        // TODO Build the user's weighted profile
        for (Rating r: ratings) {
            long item = r.getItemId();
            Map<String, Double> itemVector = model.getItemVector(item);
            for(String tag: itemVector.keySet()){
                profile.put(tag, profile.getOrDefault(tag, 0.0)
                        + (r.getValue() - mean) * itemVector.get(tag));
            }
        }

        // The profile is accumulated, return it.
        return profile;
    }
}
