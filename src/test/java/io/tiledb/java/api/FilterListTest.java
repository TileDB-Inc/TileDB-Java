package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class FilterListTest {

    @Test
    public void testFilterList() throws Exception {
        try (Context ctx = new Context();
             FilterList filterList = new FilterList(ctx)) {
            Assert.assertEquals(filterList.getNumFilters(), 0L);
            try (ByteShuffleFilter bsFilter = new ByteShuffleFilter(ctx);
                 ZstdFilter zstdFilter = new ZstdFilter(ctx, 5)) {
                filterList.addFilter(bsFilter);
                filterList.addFilter(zstdFilter);
            }
            Assert.assertEquals(filterList.getNumFilters(), 2L);
            try (Filter filter1 = filterList.getFilter(0L);
                 Filter filter2 = filterList.getFilter(1L)) {
                Assert.assertTrue(filter1 instanceof ByteShuffleFilter);
                Assert.assertTrue(filter2 instanceof ZstdFilter);
                Assert.assertEquals(((ZstdFilter) filter2).getLevel(), 5L);
            }
        }
    }

    @Test
    public void testFilterListChunksize() throws Exception {
        try (Context ctx = new Context();
             FilterList filterList = new FilterList(ctx).setMaxChunkSize(1024L)) {
            Assert.assertEquals(filterList.getMaxChunkSize(), 1024L);
        }
    }
}
