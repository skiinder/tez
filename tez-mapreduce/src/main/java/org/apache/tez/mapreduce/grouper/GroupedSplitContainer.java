/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tez.mapreduce.grouper;


import java.util.List;

import org.apache.hadoop.thirdparty.com.google.common.collect.Lists;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.tez.dag.api.TezUncheckedException;


/**
 * An entity to hold grouped splits - either mapred or mapreduce.
 */
@InterfaceAudience.Private
public class GroupedSplitContainer {

  private final List<SplitContainer> wrappedSplits;
  private final String wrappedInputFormatName;
  private final String[] locations;
  private final String rack;
  long length = 0;

  public GroupedSplitContainer(int numSplits, String wrappedInputFormatName,
                               String[] locations, String rack) {
    this.wrappedSplits = Lists.newArrayListWithCapacity(numSplits);
    this.wrappedInputFormatName = wrappedInputFormatName;
    this.locations = locations;
    this.rack = rack;
  }


  public void addSplit(SplitContainer splitContainer) {
    wrappedSplits.add(splitContainer);
    try {
      length += splitContainer.getLength();
    } catch (Exception e) {
      throw new TezUncheckedException(e);
    }
  }

  public long getLength() {
    return length;
  }

  public String getWrappedInputFormatName() {
    return this.wrappedInputFormatName;
  }

  public List<SplitContainer> getWrappedSplitContainers() {
    return this.wrappedSplits;
  }

  public String[] getLocations() {
    return this.locations;
  }

  public String getRack() {
    return this.rack;
  }
}
