package kuvaldis.algorithm.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on DFS, as it takes less memory. We don't care about execution stack depth, consider it infinite.
 *
 */
public abstract class AbstractBacktrack<E, R, D> implements Backtrack<R, D> {

    @Override
    public R backtrack(D input) {
        prepare(input);
        final List<E> solutionsList = new ArrayList<>();
        doBacktrack(solutionsList, 0, input);
        return constructResult(input);
    }

    protected void prepare(D input) { }

    protected boolean doBacktrack(final List<E> solutionsList, final int start, final D input) {
        int solutionLength = start;
        if (isSolution(solutionsList, solutionLength, input)) {
            processSolution(solutionsList, solutionLength, input);
        } else {
            final List<E> candidates = constructCandidates(solutionsList, ++solutionLength, input);
            for (E candidate : candidates) {
                addToSolutionsList(solutionsList, solutionLength - 1, candidate);
                if (makeMove(solutionsList, solutionLength, input)) {
                    return true;
                }
                if (doBacktrack(solutionsList, solutionLength, input)) {
                    return true;
                }
                if (unmakeMove(solutionsList, solutionLength, input)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addToSolutionsList(final List<E> solutionsList, final int addPosition, final E candidate) {
        if (solutionsList.size() - 1 < addPosition) {
            solutionsList.add(candidate);
        } else {
            solutionsList.set(addPosition, candidate);
        }
    }

    protected boolean makeMove(List<E> solutionsList, int k, D input) {
        return false;
    }

    protected boolean unmakeMove(List<E> solutionsList, int k, D input) {
        return false;
    }

    protected abstract R constructResult(D input);

    protected abstract boolean isSolution(List<E> solutionsList, int k, D input);

    protected abstract void processSolution(List<E> solutionsList, int k, D input);

    protected abstract List<E> constructCandidates(List<E> solutionsList, int k, D input);
}