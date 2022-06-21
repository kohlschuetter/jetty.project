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

package org.eclipse.jetty.util;

// LUDO: javadocs
public interface Retainable
{
    public void retain();

    public boolean release();

    // LUDO: move here the retain/release logic currently in RetainableByteBuffer,
    // so that it can be used e.g. in PathContentSource.read() (see there).
    // Call the inner class e.g. ReferenceCounter.
}
