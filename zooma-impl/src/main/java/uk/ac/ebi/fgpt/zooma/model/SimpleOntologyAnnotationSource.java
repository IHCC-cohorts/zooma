package uk.ac.ebi.fgpt.zooma.model;

import java.net.URI;

/**
 * A basic implementation of an Annotation Source, declaring the source of an annotation as an ontology
 *
 * @author Tony Burdett
 * @date 04/10/13
 */
public class SimpleOntologyAnnotationSource extends SimpleAnnotationSource {
    private String title;
    private String description;

    public SimpleOntologyAnnotationSource(URI source, String name, String title, String description) {
        super(source, name, Type.ONTOLOGY);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {if (title == null){title = "";} return title;}

    public String getDescription() {if (description == null){description = "No description";} return description;}

    @Override
    public String toString() {
        return "SimpleAnnotationSource{" +
                "source=" + getURI() +
                ", type=" + getType() +
                ", name='" + getName() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
