package org.lenskit.mooc.cbf;

import org.lenskit.api.Result;
import org.lenskit.api.ResultMap;
import org.lenskit.basic.AbstractItemScorer;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.entities.CommonAttributes;
import org.lenskit.data.ratings.Rating;
import org.lenskit.results.Results;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class TFIDFItemScorer extends AbstractItemScorer {
    private final DataAccessObject dao;
    private final TFIDFModel model;
    private final UserProfileBuilder profileBuilder;

    /**
     * Construct a new item scorer.  LensKit's dependency injector will call this constructor and
     * provide the appropriate parameters.
     *
     * @param dao The data access object, for looking up users' ratings.
     * @param m   The precomputed model containing the item tag vectors.
     * @param upb The user profile builder for building user tag profiles.
     */
    @Inject
    public TFIDFItemScorer(DataAccessObject dao, TFIDFModel m, UserProfileBuilder upb) {
        this.dao = dao;
        model = m;
        profileBuilder = upb;
    }

    /**
     * Generate item scores personalized for a particular user.  For the TFIDF scorer, this will
     * prepare a user profile and compare it to item tag vectors to produce the score.
     *
     * @param user   The user to score for.
     * @param items  A collection of item ids that should be scored.
     */
    @Nonnull
    @Override
    public ResultMap scoreWithDetails(long user, @Nonnull Collection<Long> items){
        // Get the user's ratings
        List<Rating> ratings = dao.query(Rating.class)
                                  .withAttribute(CommonAttributes.USER_ID, user)
                                  .get();

        if (ratings == null) {
            // the user doesn't exist, so return an empty ResultMap
            return Results.newResultMap();
        }

        // Create a place to store the results of our score computations
        List<Result> results = new ArrayList<>();

        // Get the user's profile, which is a vector with their 'like' for each tag
        Map<String, Double> userVector = profileBuilder.makeUserProfile(ratings);
        // TODO by LCC: userNorm
        double uNorm = 0.0;
        for(String tag: userVector.keySet()){
            uNorm += userVector.get(tag) * userVector.get(tag);
        }
        uNorm = Math.sqrt(uNorm);


        for (Long item: items) {
            Map<String, Double> iv = model.getItemVector(item);

            // TODO Compute the cosine of this item and the user's profile, store it in the output list
            double iNorm = 0.0;
            for(String tag: iv.keySet()){
                iNorm += iv.get(tag) * iv.get(tag);
            }
            iNorm = Math.sqrt(iNorm);
            double cosine = 0.0;
            for(String tag: iv.keySet()){
                cosine += iv.get(tag) * userVector.getOrDefault(tag, 0.0);
            }
            if(uNorm * iNorm == 0.0) continue;
            cosine = cosine / (uNorm * iNorm);
            results.add(Results.create(item, cosine));

            // TODO And remove this exception to say you've implemented it
            // If the denominator of the cosine similarity is 0, skip the item

//            throw new UnsupportedOperationException("stub implementation");
        }

        return Results.newResultMap(results);
    }
}
































































