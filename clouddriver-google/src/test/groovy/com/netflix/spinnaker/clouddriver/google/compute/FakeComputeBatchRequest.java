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

import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.compute.ComputeRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FakeComputeBatchRequest<RequestT extends ComputeRequest<ResponseT>, ResponseT>
    implements ComputeBatchRequest<RequestT, ResponseT> {

  private List<QueuedRequest> requests = new ArrayList<>();

  @Override
  public void queue(
      GoogleComputeRequest<RequestT, ResponseT> request, JsonBatchCallback<ResponseT> callback) {
    requests.add(new QueuedRequest(request, callback));
  }

  @Override
  public void execute(String batchContext) throws IOException {
    for (QueuedRequest request : requests) {
      try {
        request.callback.onSuccess(request.request.execute(), new HttpHeaders());
      } catch (IOException | RuntimeException e) {
        request.callback.onFailure(new GoogleJsonError(), new HttpHeaders());
      }
    }
  }

  private final class QueuedRequest {
    GoogleComputeRequest<RequestT, ResponseT> request;
    JsonBatchCallback<ResponseT> callback;

    QueuedRequest(
        GoogleComputeRequest<RequestT, ResponseT> request, JsonBatchCallback<ResponseT> callback) {
      this.request = request;
      this.callback = callback;
    }
  }
}
