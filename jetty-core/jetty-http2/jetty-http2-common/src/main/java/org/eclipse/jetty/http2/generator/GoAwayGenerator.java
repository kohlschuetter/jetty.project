//
// ========================================================================
// Copyright (c) 1995-2022 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.http2.generator;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.eclipse.jetty.http2.frames.Frame;
import org.eclipse.jetty.http2.frames.FrameType;
import org.eclipse.jetty.http2.frames.GoAwayFrame;
import org.eclipse.jetty.http2.internal.Flags;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.RetainableByteBuffer;
import org.eclipse.jetty.util.BufferUtil;

public class GoAwayGenerator extends FrameGenerator
{
    public GoAwayGenerator(HeaderGenerator headerGenerator)
    {
        super(headerGenerator);
    }

    @Override
    public int generate(ByteBufferPool.Accumulator accumulator, Frame frame)
    {
        GoAwayFrame goAwayFrame = (GoAwayFrame)frame;
        return generateGoAway(accumulator, goAwayFrame.getLastStreamId(), goAwayFrame.getError(), goAwayFrame.getPayload());
    }

    public int generateGoAway(ByteBufferPool.Accumulator accumulator, int lastStreamId, int error, byte[] payload)
    {
        if (lastStreamId < 0)
            lastStreamId = 0;

        // The last streamId + the error code.
        int fixedLength = 4 + 4;

        // Make sure we don't exceed the default frame max length.
        int maxPayloadLength = Frame.DEFAULT_MAX_LENGTH - fixedLength;
        if (payload != null && payload.length > maxPayloadLength)
            payload = Arrays.copyOfRange(payload, 0, maxPayloadLength);

        int length = fixedLength + (payload != null ? payload.length : 0);
        RetainableByteBuffer header = generateHeader(FrameType.GO_AWAY, length, Flags.NONE, 0);
        ByteBuffer byteBuffer = header.getByteBuffer();

        byteBuffer.putInt(lastStreamId);
        byteBuffer.putInt(error);

        if (payload != null)
            byteBuffer.put(payload);

        BufferUtil.flipToFlush(byteBuffer, 0);
        accumulator.append(header);

        return Frame.HEADER_LENGTH + length;
    }
}
