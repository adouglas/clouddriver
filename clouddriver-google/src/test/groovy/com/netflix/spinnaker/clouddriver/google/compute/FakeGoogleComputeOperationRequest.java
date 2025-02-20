/*
 * Copyright 2019 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.google.compute;

import com.google.api.services.compute.ComputeRequest;
import com.google.api.services.compute.model.Operation;
import com.netflix.spinnaker.clouddriver.data.task.Task;
import java.io.IOException;

public class FakeGoogleComputeOperationRequest<RequestT extends ComputeRequest<Operation>>
    extends FakeGoogleComputeRequest<RequestT, Operation>
    implements GoogleComputeOperationRequest<RequestT> {

  private boolean waited = false;

  public FakeGoogleComputeOperationRequest() {
    this(new Operation());
  }

  public FakeGoogleComputeOperationRequest(Operation response) {
    super(response);
  }

  @Override
  public Operation executeAndWait(Task task, String phase) throws IOException {
    waited = true;
    return execute();
  }

  public boolean waitedForCompletion() {
    return waited;
  }
}
