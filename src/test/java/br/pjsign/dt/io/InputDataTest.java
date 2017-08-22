package br.pjsign.dt.io;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.build.AttributeBuild;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InputDataTest {

    private InputData input;

    @Before
    public void setUp() throws Exception {
        this.input = new InputData("src/main/resources/trainProdSelection.arff");
        this.input.load();
    }

    @Test
    public void loadInstance() throws Exception {
        final List<Instance> instanceList = this.input.getInstanceSet();
        for (Instance i: instanceList) {
            System.out.println(i);
        }
        assertEquals(186, instanceList.size());
    }

    @Test
    public void loadAttribute() throws Exception {
        final List<Attribute> attributeList = this.input.getAttributeSet();
        for (Attribute a: attributeList) {
            System.out.println(a);
        }
        assertEquals(6, attributeList.size());
    }

    @Test
    public void loadTargetAttribute() throws Exception {
        assertEquals("label", this.input.getTargetAttribute().getName());
        assertEquals(Attribute.DISCRETE_TYPE, this.input.getTargetAttribute().getType());
        assertEquals(5, this.input.getTargetAttribute().getValues().size());
    }
}