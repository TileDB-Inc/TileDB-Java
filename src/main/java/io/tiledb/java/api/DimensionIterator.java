package io.tiledb.java.api;

import java.util.Iterator;

public class DimensionIterator implements Iterator<Dimension> {

    private Domain domain;
    private long ndim;
    private long idx;

    public DimensionIterator(Domain domain) {
        this.domain = domain;
        try {
            this.ndim = domain.getRank();
        } catch (TileDBError err) {
            throw new RuntimeException(err);
        }
        this.idx = 0;
    }

    @Override
    public boolean hasNext() {
        return idx < ndim;
    }

    @Override
    public Dimension next() {
        Dimension dim;
        try {
            dim = domain.getDimension(idx);
        } catch (TileDBError err) {
            throw new RuntimeException(err);
        }
        idx++;
        return dim;
    }
}
