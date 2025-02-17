/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tez.runtime.library.common.shuffle.orderedgrouped;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.Decompressor;

import org.apache.hadoop.thirdparty.com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.mock;

/**
 * A dummy codec. It passes everything to underlying stream
 */
public class DummyCompressionCodec implements CompressionCodec, Configurable {
  @VisibleForTesting
  int createInputStreamCalled = 0;
  private Configuration conf;

  @Override
  public CompressionOutputStream createOutputStream(OutputStream out) throws IOException {
    return new DummyCompressionOutputStream(out);
  }

  @Override
  public CompressionOutputStream createOutputStream(OutputStream out, Compressor compressor) throws IOException {
    return new DummyCompressionOutputStream(out);
  }

  @Override
  public Class<? extends Compressor> getCompressorType() {
    return Compressor.class;
  }

  @Override
  public Compressor createCompressor() {
    return mock(Compressor.class);
  }

  @Override
  public CompressionInputStream createInputStream(InputStream in) throws IOException {
    return new DummyCompressionInputStream(in);
  }

  @Override
  public CompressionInputStream createInputStream(InputStream in, Decompressor decompressor) throws IOException {
    createInputStreamCalled += 1;
    return new DummyCompressionInputStream(in);
  }

  @Override
  public Class<? extends Decompressor> getDecompressorType() {
    return Decompressor.class;
  }

  @Override
  public Decompressor createDecompressor() {
    return mock(Decompressor.class);
  }

  @Override
  public String getDefaultExtension() {
    return null;
  }

  class DummyCompressionOutputStream extends CompressionOutputStream {

    protected DummyCompressionOutputStream(OutputStream out) {
      super(out);
    }

    @Override
    public void write(int b) throws IOException {
      out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      out.write(b, off, len);
    }

    @Override
    public void finish() throws IOException {
      //no-op
    }

    @Override
    public void resetState() throws IOException {
      //no-op
    }
  }

  class DummyCompressionInputStream extends CompressionInputStream {

    protected DummyCompressionInputStream(InputStream in) throws IOException {
      super(in);
    }

    @Override
    public int read() throws IOException {
      return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return in.read(b, off, len);
    }

    @Override
    public void resetState() throws IOException {
      //no-op
    }
  }

  @Override
  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  @Override
  public Configuration getConf() {
    return conf;
  }
}
