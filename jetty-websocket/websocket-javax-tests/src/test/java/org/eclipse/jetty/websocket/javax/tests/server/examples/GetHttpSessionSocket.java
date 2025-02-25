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

package org.eclipse.jetty.websocket.javax.tests.server.examples;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/example", configurator = GetHttpSessionConfigurator.class)
public class GetHttpSessionSocket
{
    private Session wsSession;
    @SuppressWarnings("unused")
    private HttpSession httpSession;

    @OnOpen
    public void open(Session session, EndpointConfig config)
    {
        this.wsSession = session;
        this.httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
    }

    @OnMessage
    public void echo(String msg) throws IOException
    {
        wsSession.getBasicRemote().sendText(msg);
    }
}
