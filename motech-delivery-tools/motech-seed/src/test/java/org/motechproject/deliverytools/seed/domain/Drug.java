package org.motechproject.deliverytools.seed.domain;


import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'Drug'")
public class Drug extends MotechBaseDataObject {
    @JsonProperty
    private String type = "Drug";
    @JsonProperty
    private String name;

    public Drug() {
    }

    public Drug(String name) {
        this.name = name;
    }
}
