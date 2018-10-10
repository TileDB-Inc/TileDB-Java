package io.tiledb.java.api;

import java.util.Iterator;

public class AttributeIterator implements Iterator<Attribute> {

  public ArraySchema schema;
  public long nattr;
  public long idx;

  public AttributeIterator(ArraySchema schema) {
    long nattr = 0L;
    try {
      nattr = schema.getAttributeNum();
    } catch (TileDBError err) {
      throw new RuntimeException(err);
    }
    this.schema = schema;
    this.nattr = nattr;
    this.idx = 0L;
  }

  @Override
  public boolean hasNext() {
    return idx < nattr;
  }

  @Override
  public Attribute next() {
    Attribute attribute;
    try {
      attribute = schema.getAttribute(idx);
    } catch (TileDBError err) {
      throw new RuntimeException(err);
    }
    idx++;
    return attribute;
  }
}
