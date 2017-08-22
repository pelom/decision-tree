package br.pjsign.dt.build;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.impl.AttributeDefault;

import java.io.IOException;

public class AttributeBuild {

    public static Attribute createAttributeReal() throws IOException {
        return new AttributeDefault("Real value", Attribute.REAL_VALUE);
    }

    public static Attribute createAttributeDicrete() throws IOException {
        return new AttributeDefault("Real discrete",
                "{discrete1, discrete2, discrete3}");
    }
}
