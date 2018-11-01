package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class FilterTest {

    @Test
    public void testNoneFiler() throws Exception {
        try (Context ctx = new Context();
             NoneFilter filter = new NoneFilter(ctx)) {
        }
    }

    @Test
    public void testGzipFilter() throws Exception {
        try(Context ctx = new Context()) {
            try(GzipFilter filter = new GzipFilter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(GzipFilter filter = new GzipFilter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testZstdFilter() throws Exception {
        try(Context ctx = new Context()) {
            try(ZstdFilter filter = new ZstdFilter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(ZstdFilter filter = new ZstdFilter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testLZ4Filter() throws Exception {
        try(Context ctx = new Context()) {
            try(LZ4Filter filter = new LZ4Filter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(LZ4Filter filter = new LZ4Filter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testBzip2Filter() throws Exception {
        try(Context ctx = new Context()) {
            try(Bzip2Filter filter = new Bzip2Filter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(Bzip2Filter filter = new Bzip2Filter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testRleFilter() throws Exception {
        // Rle accepts compression level, but it is an ignored parameter
        try(Context ctx = new Context()) {
            try(RleFilter filter = new RleFilter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(RleFilter filter = new RleFilter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testDoubleDeltaFilter() throws Exception {
        // DD accepts compression level, but it is an ignored parameter
        try(Context ctx = new Context()) {
            try(DoubleDeltaFilter filter = new DoubleDeltaFilter(ctx, 5)) {
                Assert.assertEquals(filter.getLevel(), 5);
            }
            try(DoubleDeltaFilter filter = new DoubleDeltaFilter(ctx)) {
                Assert.assertEquals(filter.getLevel(), -1);
            }
        }
    }

    @Test
    public void testBitShuffle() throws Exception {
        try(Context ctx = new Context();
            BitShuffleFilter filter = new BitShuffleFilter(ctx)) {
        }
    }

    @Test
    public void testByteShuffle() throws Exception {
        try(Context ctx = new Context();
            ByteShuffleFilter filter = new ByteShuffleFilter(ctx)) {
        }
    }

    @Test
    public void testBitWidthReductionFilter() throws Exception {
        try(Context ctx = new Context()) {
            try (BitWidthReductionFilter filter = new BitWidthReductionFilter(ctx, 1024)) {
                Assert.assertEquals(filter.getWindow(), 1024);
            }
            try (BitWidthReductionFilter filter = new BitWidthReductionFilter(ctx)) {
                Assert.assertTrue(filter.getWindow() > 0);
            }
        }
    }

    @Test
    public void testPositiveDeltaFilter() throws Exception {
        try(Context ctx = new Context()) {
            try (PositiveDeltaFilter filter = new PositiveDeltaFilter(ctx, 1024)) {
                Assert.assertEquals(filter.getWindow(), 1024);
            }
            try (PositiveDeltaFilter filter = new PositiveDeltaFilter(ctx)) {
                Assert.assertTrue(filter.getWindow() > 0);
            }
        }
    }

}
