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

package io.tiledb.spark.datasourcev2

import org.apache.spark.sql.SparkSession

object Test {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession
      .builder
        .master("local")
      .appName("Test")
      .getOrCreate()

    var df = sparkSession.read
      .format("io.tiledb.spark.datasourcev2")
      .option("arrayURI", "my_sparse_array")
//      .option("batchSize", "3")
      //add subarray filter
      .option("subarray.d1.min", 1)
      .option("subarray.d1.max", 2)
      .option("subarray.d2.min", 1)
      .option("subarray.d2.max", 4)
      .load()
      //select columns
      .select("d1","d2","a2")

    //print df schema
    df.schema.printTreeString()

    //add sql filters
//    df = df.filter("(d1>=1 and d1<=2) and (d2>=1 and d2<=4)")
//    df = df.filter(df("d2").geq(1))
//      .filter(df("d2").leq(2))

    //print df
    df.show()
  }
}
