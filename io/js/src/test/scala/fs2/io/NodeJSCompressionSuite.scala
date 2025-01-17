/*
 * Copyright (c) 2013 Functional Streams for Scala
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fs2
package io

import fs2.CompressionSuite
import fs2.io.compression._
import fs2.io.internal.facade

class NodeJSCompressionSuite extends CompressionSuite {

  override def deflateStream(
      b: Array[Byte],
      level: Int,
      strategy: Int,
      nowrap: Boolean
  ): Array[Byte] = {
    val in = Chunk.array(b).toUint8Array
    val options = new facade.zlib.Options {}
    options.level = level
    options.strategy = strategy
    val out =
      if (nowrap)
        facade.zlib.gzipSync(in, options)
      else
        facade.zlib.deflateSync(in, options)
    Chunk.uint8Array(out).toArray
  }

  override def inflateStream(b: Array[Byte], nowrap: Boolean): Array[Byte] = {
    val in = Chunk.array(b).toUint8Array
    val options = new facade.zlib.Options {}
    val out =
      if (nowrap)
        facade.zlib.gunzipSync(in, options)
      else
        facade.zlib.inflateSync(in, options)
    Chunk.uint8Array(out).toArray
  }

}
