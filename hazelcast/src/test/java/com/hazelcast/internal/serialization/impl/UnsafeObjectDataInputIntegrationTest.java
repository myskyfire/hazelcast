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

package com.hazelcast.internal.serialization.impl;

import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.test.HazelcastParametersRunnerFactory;
import com.hazelcast.test.annotation.ParallelTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Assume;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.ByteOrder;

@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(HazelcastParametersRunnerFactory.class)
@Category({QuickTest.class, ParallelTest.class})
public class UnsafeObjectDataInputIntegrationTest
        extends AbstractDataStreamIntegrationTest<UnsafeObjectDataOutput, UnsafeObjectDataInput> {

    private UnsafeObjectDataOutput output;

    @Override
    protected void assumptions() {
        Assume.assumeTrue("UnsafeObjectDataInput can be only used with native byte order: " + ByteOrder.nativeOrder(),
                ByteOrder.nativeOrder() == byteOrder);
    }

    @Override
    protected byte[] getWrittenBytes() {
        return output.toByteArray();
    }

    @Override
    protected UnsafeObjectDataOutput getDataOutput(InternalSerializationService serializationService) {
        output = new UnsafeObjectDataOutput(300, serializationService);
        return output;
    }

    @Override
    protected UnsafeObjectDataInput getDataInputFromOutput() {
        return new UnsafeObjectDataInput(getWrittenBytes(), serializationService);
    }
}
