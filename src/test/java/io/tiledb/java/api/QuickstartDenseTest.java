/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 TileDB, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.tiledb.java.api;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.math.BigInteger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.tiledb.libtiledb.*;
import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.CompressorType.*;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.*;

@SuppressWarnings("ALL")
public class QuickstartDenseTest {

  private Context ctx;
  private String arrayURI = "my_dense_array";
  private boolean callbackCalled = false;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @Test
  public void testQuickstartDense() throws Exception {
    arrayCreate();
    arrayWrite();
    arrayRead();
    arrayReadIncomplete();
    arrayReadAsync();
  }

  public void arrayCreate() throws Exception {
    // Create getDimensions
    Dimension<Long> d1 = new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Dimension<Long> d2 = new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 4l), 2l);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    Attribute a2 = new Attribute(ctx, "a2", Character.class);
    a2.setCellValNum(tiledb.tiledb_var_num());
    Attribute a3 = new Attribute(ctx, "a3", Float.class);
    a3.setCellValNum(2);
    a1.setCompressor(new Compressor(TILEDB_BLOSC_LZ4, -1));
    a2.setCompressor(new Compressor(TILEDB_GZIP, -1));
    a3.setCompressor(new Compressor(TILEDB_ZSTD, -1));

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);
    schema.addAttribute(a3);

    schema.check();

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {

    Array my_dense_array = new Array(ctx,arrayURI, TILEDB_WRITE);

    // Prepare cell buffers
    NativeArray a1_data = new NativeArray(
        ctx,
        new int[]{
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15
        },
        Integer.class);
    NativeArray a2_offsets = new NativeArray(
        ctx,
        new long[]{
            0, 1, 3, 6, 10, 11, 13, 16,
            20, 21, 23, 26, 30, 31, 33, 36
        },
        Long.class);
    NativeArray buffer_var_a2 = new NativeArray(
        ctx,
        "abbcccdddd"+
            "effggghhhh"+
            "ijjkkkllll"+
            "mnnooopppp",
        String.class);

    NativeArray buffer_a3 = new NativeArray(
        ctx,
        new float[]{
            0.1f,  0.2f,  1.1f,  1.2f,  2.1f,  2.2f,  3.1f,  3.2f,
            4.1f,  4.2f,  5.1f,  5.2f,  6.1f,  6.2f,  7.1f,  7.2f,
            8.1f,  8.2f,  9.1f,  9.2f,  10.1f, 10.2f, 11.1f, 11.2f,
            12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
        },
        Float.class);

    // Create query
    Query query = new Query(my_dense_array, TILEDB_WRITE);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submit();
    query.close();
  }

  private void arrayRead() throws Exception {
    // Print non-empty getDomain

    Array my_dense_array = new Array(ctx, arrayURI);
    HashMap<String, Pair> dom = my_dense_array.nonEmptyDomain();

    Assert.assertEquals(dom.get("d1").getFirst(),  BigInteger.valueOf(1l));
    Assert.assertEquals(dom.get("d1").getSecond(), BigInteger.valueOf(4l));
    Assert.assertEquals(dom.get("d2").getFirst(),  BigInteger.valueOf(1l));
    Assert.assertEquals(dom.get("d2").getSecond(), BigInteger.valueOf(4l));

    //for (Map.Entry<String, Pair> e : dom.entrySet()) {
    //  System.out.println(e.getKey() + ": ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    //}

    // Print maximum buffer elements for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new long[]{1l, 4l, 1l, 4l}, Long.class);
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.maxBufferElements(subarray);

    Assert.assertEquals(max_sizes.get("a1").getFirst(),  (Long) 0l);
    Assert.assertEquals(max_sizes.get("a1").getSecond(), (Long) 16l);
    Assert.assertEquals(max_sizes.get("a2").getFirst(),  (Long) 16l);
    Assert.assertEquals(max_sizes.get("a2").getSecond(), (Long) 56l);
    Assert.assertEquals(max_sizes.get("a3").getFirst(), (Long) 0l);
    Assert.assertEquals(max_sizes.get("a3").getSecond(), (Long) 32l);

    //for (Map.Entry<String, Pair<Long,Long>> e : max_sizes.entrySet()){
    //  System.out.println(e.getKey() + " ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    //}

    // Create query
    Query query = new Query(my_dense_array, TILEDB_READ);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setSubarray(subarray);
    query.setBuffer("a1",
        new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(),Integer.class));
    query.setBuffer("a2",
        new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Long.class),
        new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
    query.setBuffer("a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));

    // Submit query
    query.submit();
    //System.out.println("Query submitted: " + query.submit() );

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] a1_buff = (int[]) query.getBuffer("a1");
    long[] a2_offsets = (long[]) query.getVarBuffer("a2");
    byte[] a2_data = (byte[]) query.getBuffer("a2");
    float[] a3_buff = (float[]) query.getBuffer("a3");

    // check a1
    Assert.assertArrayEquals(a1_buff, new int[]{0, 1, 2, 3, 4, 5, 6, 7,
	    					8, 9, 10, 11, 12, 13, 14, 15});
    // check a2
    String[] a2_expected = new String[]{"a", "bb", "ccc", "dddd",
	    				"e", "ff", "ggg", "hhhh",
					"i", "jj", "kkk", "llll",
					"m", "nn", "ooo", "pppp"};

    for (int i=0; i < a2_offsets.length; i++) {
        int end = (i == a2_offsets.length - 1) ? a2_data.length : (int) a2_offsets[i + 1];
        String a2_value = new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end));
	Assert.assertEquals(a2_value, a2_expected[i]);
    }

    // check a3
    float[] a3_expected = new float[]{0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
	    			      4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
    				      8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
                                      12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f};
    Assert.assertEquals(a3_buff.length, a3_expected.length);
    for (int i=0; i < a3_buff.length; i++) {
        Assert.assertEquals(a3_buff[i], a3_expected[i], 0.01f);
    }

    //System.out.println("Result num: " + a1_buff.length );
    //System.out.println(
    //    String.format("%9s","a1") +
    //        String.format("%11s","a2") +
    //        String.format("%11s","a3[0]") +
    //        String.format("%10s","a3[1]"));

    //for (int i =0; i< a1_buff.length; i++){
    //  int end = (i==a1_buff.length-1)? a2_data.length : (int) a2_offsets[i+1];
    //  System.out.println(
    //      String.format("%9s",a1_buff[i])+
    //          String.format("%11s",new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end)))+
    //          String.format("%11s",a3_buff[2*i])+
    //          String.format("%10s",a3_buff[2*i+1]));
    //}
  }


  private void arrayReadAsync() throws Exception {
    Array my_dense_array = new Array(ctx, arrayURI);

    // Calcuate maximum buffer sizes for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new long[]{1l, 4l, 1l, 4l}, Long.class);
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.maxBufferElements(subarray);


    // Create query
    Query query = new Query(my_dense_array, TILEDB_READ);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("a1",
        new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(),Integer.class));
    query.setBuffer("a2",
        new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Long.class),
        new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
    query.setBuffer("a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));


    // Submit query with callback
    query.submitAsync(new ReadCallback());

    // Wait for query to complete
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == QueryStatus.TILEDB_INPROGRESS);

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] a1_buff = (int[]) query.getBuffer("a1");
    long[] a2_offsets = (long[]) query.getVarBuffer("a2");
    byte[] a2_data = (byte[]) query.getBuffer("a2");
    float[] a3_buff = (float[]) query.getBuffer("a3");

    Assert.assertTrue(callbackCalled);

    // check a1
    Assert.assertArrayEquals(a1_buff, new int[]{0, 1, 2, 3, 4, 5, 6, 7,
	    					8, 9, 10, 11, 12, 13, 14, 15});
    // check a2
    String[] a2_expected = new String[]{"a", "bb", "ccc", "dddd",
	    				"e", "ff", "ggg", "hhhh",
					"i", "jj", "kkk", "llll",
					"m", "nn", "ooo", "pppp"};

    for (int i=0; i < a2_offsets.length; i++) {
        int end = (i == a2_offsets.length - 1) ? a2_data.length : (int) a2_offsets[i + 1];
        String a2_value = new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end));
	Assert.assertEquals(a2_value, a2_expected[i]);
    }

    // check a3
    float[] a3_expected = new float[]{0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
	    			      4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
    				      8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
                                      12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f};
    Assert.assertEquals(a3_buff.length, a3_expected.length);
    for (int i=0; i < a3_buff.length; i++) {
        Assert.assertEquals(a3_buff[i], a3_expected[i], 0.01f);
    }

    //System.out.println("Result num: " + a1_buff.length );
    //System.out.println(
    //    String.format("%9s","a1") +
    //        String.format("%11s","a2") +
    //        String.format("%11s","a3[0]") +
    //        String.format("%10s","a3[1]"));
    //for (int i=0; i< a1_buff.length; i++) {
    //  int end = (i==a1_buff.length-1)? a2_data.length : (int) a2_offsets[i+1];
    //  System.out.println(
    //      String.format("%9s",a1_buff[i])+
    //          String.format("%11s",new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end)))+
    //          String.format("%11s",a3_buff[2*i])+
    //          String.format("%10s",a3_buff[2*i+1]));
    //}
  }

  private void arrayReadIncomplete() throws Exception {
    Array my_dense_array = new Array(ctx, arrayURI);

    // Create query
    Query query = new Query(my_dense_array, TILEDB_READ);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    long[] subarray = {1l, 4l, 1l, 4l};
    query.setSubarray(new NativeArray(ctx, subarray, Long.class));
    query.setBuffer("a1", new NativeArray(ctx, 4, Integer.class));

    // Loop until the query is completed

    //System.out.println("a1\n---");
    int nsubmits = 0;
    do {
      //System.out.println("Reading cells...");
      query.submit();

      int[] a1_buff = (int[]) query.getBuffer("a1");
      int[] a1_expected = new int[]{nsubmits * 4 + 0,
	                            nsubmits * 4 + 1,
				    nsubmits * 4 + 2,
				    nsubmits * 4 + 3};
      Assert.assertArrayEquals(a1_buff, a1_expected);
      nsubmits += 1;
      //for (int i =0; i< a1_buff.length; i++){
      //  System.out.println(a1_buff[i]);
      //}
    } while (query.getQueryStatus() == QueryStatus.TILEDB_INCOMPLETE);
  }

  private class ReadCallback implements Callback {

    public ReadCallback() {
    }

    public void call() {
        callbackCalled = true;
    }
  }
}
