package br.pjsign.dt.impl;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.build.AttributeBuild;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeDefaultTest {

    private Attribute attribute;

    @Before
    public void setUp() throws Exception {
        this.attribute = AttributeBuild.createAttributeDicrete();
    }

    @Test
    public void deveCriarAttributeContinuousType() throws Exception {
        final Attribute attribute =
                new AttributeDefault("Real value", Attribute.REAL_VALUE);
        assertEquals(Attribute.CONTINUOUS_TYPE, attribute.getType());
    }

    @Test
    public void deveCriarAttributeDiscreteType() throws Exception {
        assertEquals(Attribute.DISCRETE_TYPE, this.attribute.getType());
    }

    @Test
    public void deveRetornaAttributeName() throws Exception {
        final Attribute attribute = new AttributeDefault("Name",
                "{discrete1, discrete2, discrete3}");
        assertEquals("Name", attribute.getName());
    }

    @Test
    public void deveRetornaAttributeValues() throws Exception {
        final String v1 = "discrete1";
        final String v2 = "discrete2";
        final String v3 = "discrete3";
        assertEquals(v1, this.attribute.getValues().get(0));
        assertEquals(v2, this.attribute.getValues().get(1));
        assertEquals(v3, this.attribute.getValues().get(2));
    }
}