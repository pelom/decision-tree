package br.pjsign.dt.impl;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.Node;
import br.pjsign.dt.build.AttributeBuild;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeDefaultTest {

    private Node treeBinary;

    private Node leftNode;
    private Node rightNode;
    private Attribute attribute;

    @Before
    public void setUp() throws Exception {
        this.attribute = AttributeBuild.createAttributeReal();
        this.treeBinary = new NodeDefault(this.attribute);

        this.leftNode = new NodeDefault("Left");
        this.rightNode = new NodeDefault("Right");
        this.treeBinary.addChild("LEFT",  this.leftNode);
        this.treeBinary.addChild("RIGHT",  this.rightNode);
    }

    @Test
    public void getAttribute() throws Exception {
        assertEquals(this.attribute.getName(), this.treeBinary.getAttribute().getName());
    }

    @Test
    public void addChild() throws Exception {
        final Node root = new NodeDefault(this.attribute);
        final Node node = new NodeDefault(this.attribute);
        root.addChild("Node", node);
        assertEquals(1, root.getChildren().values().size());
    }

    @Test
    public void getChildren() throws Exception {
        assertEquals(2, this.treeBinary.getChildren().values().size());
    }

    @Test
    public void getTargetLabel() throws Exception {
        assertEquals("Left", this.leftNode.getTargetLabel());
    }

    @Test
    public void setTargetLabel() throws Exception {
        this.leftNode.setTargetLabel("Novo");
        assertEquals("Novo", this.leftNode.getTargetLabel());
        this.leftNode.setTargetLabel("Left");
        assertEquals("Left", this.leftNode.getTargetLabel());
    }

    @Test
    public void getType() throws Exception {
        assertEquals(Node.ROOT, this.treeBinary.getType());
        assertEquals(Node.LEAF, this.leftNode.getType());
        assertEquals(Node.LEAF, this.rightNode.getType());
    }

    @Test
    public void setType() throws Exception {
        this.treeBinary.setType(Node.LEAF);
        assertEquals(Node.LEAF, this.treeBinary.getType());
        this.treeBinary.setType(Node.ROOT);
        assertEquals(Node.ROOT, this.treeBinary.getType());
    }

}