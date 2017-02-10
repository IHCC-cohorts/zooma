package uk.ac.ebi.spot.zooma;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.spot.zooma.config.MongoConfig;
import uk.ac.ebi.spot.zooma.model.*;
import uk.ac.ebi.spot.zooma.model.mongo.*;
import uk.ac.ebi.spot.zooma.repository.mongo.AnnotationRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Created by olgavrou on 04/08/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
public class AnnotationRepositoryServiceIT {

    @Autowired
    AnnotationRepository annotationRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    @Before
    public void setup(){
        //Create an Annotation and store it in mongodb

        Study mongoStudy = new Study("Accession1");

        BiologicalEntity biologicalEntity = new BiologicalEntity("GSMTest1", mongoStudy);

        TypedProperty property = new TypedProperty("test type", "test value");
        String semanticTag = "http://www.ebi.ac.uk/efo/EFO_test";
        Collection<String> semanticTags = new ArrayList<>();
        semanticTags.add(semanticTag);

        //create provenance
        DatabaseAnnotationSource annotationSource = new DatabaseAnnotationSource("http://www.ebi.ac.uk/test", "test", "");

        MongoAnnotationProvenance annotationProvenance = new MongoAnnotationProvenance(annotationSource,
                MongoAnnotationProvenance.Evidence.MANUAL_CURATED,
                MongoAnnotationProvenance.Accuracy.NOT_SPECIFIED,
                "http://www.ebi.ac.uk/test", "Test annotator", LocalDateTime.now());

        MongoAnnotation annotationDocument = new MongoAnnotation(biologicalEntity,
                property,
                semanticTags,
                annotationProvenance,
                false);

        annotationRepository.save(annotationDocument);

        MongoAnnotation annotation = new MongoAnnotation(biologicalEntity,
                property,
                semanticTags,
                annotationProvenance,
                false);
        annotationRepository.save(annotation);
    }

    @After
    public void teardown(){
        //remove the annotations from the database
        mongoTemplate.getDb().dropDatabase();
    }

    @Test
    public void testFindAllDocuments(){
        List<MongoAnnotation> mongoAnnotations = annotationRepository.findAll();
        assertTrue("More than 0", mongoAnnotations.size() > 0);
    }

    @Test
    public void testGetBySemanticTags(){
        Collection<String> semanticTags = new HashSet<>();
        semanticTags.add("http://www.ebi.ac.uk/efo/EFO_test");

        Page<MongoAnnotation> mongoAnnotations = annotationRepository.findBySemanticTagIn(semanticTags, new PageRequest(0,20));
        List<MongoAnnotation> mongoAnnotationsContent = mongoAnnotations.getContent();
        assertTrue(mongoAnnotationsContent.size() > 0);
    }


    @Test
    public void testGetByProvenanceSourceName(){
        Page<MongoAnnotation> annotations = annotationRepository.findByProvenanceSourceName("test", new PageRequest(0, 20));
        List<MongoAnnotation> mongoAnnotationsContent = annotations.getContent();
        assertTrue(mongoAnnotationsContent.size() > 0);
        MongoAnnotation annotation = (MongoAnnotation) mongoAnnotationsContent.toArray()[0];
        assertTrue(annotation.getProvenance().getSource().getName().equals("test"));
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        Collection<MongoAnnotation> annotationDocumentList = annotationRepository.findAll();
        assertThat("Not empty list", annotationDocumentList.size(), is(not(0)));
    }

    @Test
    public void testGetByAnnotatedProperty() throws Exception {
        Page<MongoAnnotation> annotationDocument = annotationRepository.findByPropertyPropertyValue("test value", new PageRequest(0,20));
        List<MongoAnnotation> mongoAnnotationsContent = annotationDocument.getContent();

        MongoAnnotation annotation = (MongoAnnotation) mongoAnnotationsContent.toArray()[0];
        assertThat("The property value should be \"test value\"", annotation.getProperty().getPropertyValue(), is("test value"));

        annotationDocument = annotationRepository.findByPropertyPropertyTypeAndPropertyPropertyValue("test type", "test value", new PageRequest(0,20));

        MongoAnnotation mongoAnnotation = (MongoAnnotation) mongoAnnotationsContent.toArray()[0];
        assertThat("The property type should be \"test type\"", ((TypedProperty) mongoAnnotation.getProperty()).getPropertyType(), is("test type"));
    }

    @Test
    public void testUpdate() throws Exception{
        Page<MongoAnnotation> annotationDocument = annotationRepository.findByPropertyPropertyValue("test value", new PageRequest(0,20));
        List<MongoAnnotation> mongoAnnotationsContent = annotationDocument.getContent();

        MongoAnnotation annotation = (MongoAnnotation) mongoAnnotationsContent.toArray()[0];

        TypedProperty oldProperty = annotation.getProperty();
        String oldId = annotation.getId();

        annotation.setProperty(new TypedProperty("new type", "new value"));

        MongoAnnotation updatedAnnotation = annotationRepository.save(annotation);

        assertTrue(updatedAnnotation.getId().equals(oldId));
        assertTrue(updatedAnnotation.getProperty().getPropertyValue().equals("new value"));

        updatedAnnotation.setProperty(oldProperty);
        annotationRepository.save(updatedAnnotation);

    }

    @Test
    public void validateSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.print("");
    }

}