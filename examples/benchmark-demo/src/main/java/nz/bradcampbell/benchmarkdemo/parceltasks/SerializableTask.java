/*
 * Copyright (C) 2016 Bradley Campbell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.bradcampbell.benchmarkdemo.parceltasks;

import android.os.Parcel;
import nz.bradcampbell.benchmarkdemo.model.serializable.SerializableResponse;

public class SerializableTask extends ParcelTask<SerializableResponse> {
  public SerializableTask(ParcelListener parcelListener, SerializableResponse response) {
    super(parcelListener, response);
  }

  @Override protected int writeThenRead(SerializableResponse response, Parcel parcel) {
    parcel.writeSerializable(response);
    parcel.setDataPosition(0);
    SerializableResponse out = (SerializableResponse) parcel.readSerializable();
    return out.users.size();
  }
}
