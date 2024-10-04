package com.pasc.lib.glide.manager;

import android.support.annotation.NonNull;
import com.pasc.lib.glide.RequestManager;
import java.util.Collections;
import java.util.Set;

/**
 * A {@link RequestManagerTreeNode} that returns no relatives.
 */
final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
    @NonNull
    @Override
    public Set<RequestManager> getDescendants() {
        return Collections.emptySet();
    }
}
