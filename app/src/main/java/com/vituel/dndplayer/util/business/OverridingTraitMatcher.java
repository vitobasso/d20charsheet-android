package com.vituel.dndplayer.util.business;

import com.vituel.dndplayer.model.AbstractEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Objects.equal;

/**
 * Created by Victor on 29/04/2015.
 */
public class OverridingTraitMatcher {

    public static final Pattern PATTERN = Pattern.compile("^([[:alpha:]'( ]+[[:alpha:]])(.*?\\d.*)?");

    private final Map<String, String> rootNamesMap;

    public OverridingTraitMatcher(List<? extends AbstractEntity> entities) {
        rootNamesMap = new HashMap<>();
        for (AbstractEntity entity : entities) {
            String name = entity.getName();
            mapPatternWhenMatches(name);
        }
    }

    private boolean mapPatternWhenMatches(String name) {
        String rootName = extractRootName(name);
        if (rootName != null) {
            rootNamesMap.put(name, rootName);
            return true;
        } else {
            return false;
        }
    }

    private String extractRootName(String traitName) {
        Matcher matcher = PATTERN.matcher(traitName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public boolean matchesAny(AbstractEntity entity, List<? extends AbstractEntity> others) {
        for (AbstractEntity other : others) {
            if (matches(entity, other)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(AbstractEntity left, AbstractEntity right) {
        String leftName = left.getName();
        String rightName = right.getName();
        return rootNamesMap.get(leftName) != null
                && rootNamesMap.get(rightName) != null
                && equal(rootNamesMap.get(leftName), rootNamesMap.get(rightName));
    }

}
