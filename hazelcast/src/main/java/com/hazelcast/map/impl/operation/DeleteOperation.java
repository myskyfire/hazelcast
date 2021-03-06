/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.map.impl.operation;

import com.hazelcast.internal.cluster.Versions;
import com.hazelcast.map.impl.MapDataSerializerHook;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.impl.Versioned;

import java.io.IOException;

public class DeleteOperation extends BaseRemoveOperation implements Versioned {
    private boolean success;

    public DeleteOperation(String name, Data dataKey) {
        super(name, dataKey);
    }

    public DeleteOperation(String name, Data dataKey, boolean disableWanReplicationEvent) {
        super(name, dataKey, disableWanReplicationEvent);
    }

    public DeleteOperation() {
    }

    @Override
    public void run() {
        success = recordStore.delete(dataKey, getCallerProvenance());
    }

    @Override
    public Object getResponse() {
        return success;
    }

    @Override
    public void afterRun() {
        if (success) {
            super.afterRun();
        }
    }

    @Override
    public boolean shouldBackup() {
        return success;
    }

    @Override
    public void onWaitExpire() {
        sendResponse(false);
    }

    @Override
    public int getId() {
        return MapDataSerializerHook.DELETE;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);

        // RU_COMPAT_3_10
        if (out.getVersion().isGreaterOrEqual(Versions.V3_11)) {
            out.writeBoolean(disableWanReplicationEvent);
        }
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);

        // RU_COMPAT_3_10
        if (in.getVersion().isGreaterOrEqual(Versions.V3_11)) {
            disableWanReplicationEvent = in.readBoolean();
        }
    }
}
