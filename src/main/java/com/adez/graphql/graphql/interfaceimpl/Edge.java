package com.adez.graphql.graphql.interfaceimpl;

import graphql.relay.ConnectionCursor;

public class Edge implements graphql.relay.Edge{
	
	public Edge(Object node, ConnectionCursor cursor) {
        this.node = node;
        this.cursor = cursor;
    }

    Object node;
    ConnectionCursor cursor;

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public ConnectionCursor getCursor() {
        return cursor;
    }

    public void setCursor(ConnectionCursor cursor) {
        this.cursor = cursor;
    }
}
