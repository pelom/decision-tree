package br.pjsign.dt.impl;

import br.pjsign.dt.Instance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class InstanceDefaultTest {

    private Instance instance;

    @Before
    public void setUp() throws Exception {
        this.instance = new InstanceDefault();
        this.instance.setAttribute("InstanceName1", "InstanceValue1");
        this.instance.setAttribute("InstanceName2", "InstanceValue2");
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void getInstanceIndex() throws Exception {
//        assertEquals(3, this.instance.getInstanceIndex());
//    }

    @Test
    public void getAttribute() throws Exception {
        assertEquals("InstanceValue1", this.instance.getAttribute("InstanceName1"));
    }

    @Test
    public void setAttribute() throws Exception {
        this.instance.setAttribute("InstanceName3", "InstanceValue3");
        assertEquals("InstanceValue3", this.instance.getAttribute("InstanceName3"));
    }

    @Test
    public void getAllAttribute() throws Exception {
        final Collection<String> coll = this.instance.getAllAttribute();
        assertEquals(2, coll.size());
    }

}