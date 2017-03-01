package uk.ac.ebi.spot.zooma.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 06/01/17
 */
@Configuration
public class QueueConfig {
    @Bean MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJsonObjectMapper(mapper);
        return converter;
    }

    @Bean Queue queueNeo() { return new Queue(Constants.Queues.ANNOTATION_SAVE_NEO, false); }
    @Bean Queue queueSolr() { return new Queue(Constants.Queues.ANNOTATION_SAVE_SOLR, false); }
    @Bean FanoutExchange exchange() { return new FanoutExchange(Constants.Exchanges.ANNOTATION_FANOUT); }
    @Bean Binding bindingNeo(Queue queueNeo, FanoutExchange exchange) { return BindingBuilder.bind(queueNeo).to(exchange); }
    @Bean Binding bindingSolr(Queue queueSolr, FanoutExchange exchange) { return BindingBuilder.bind(queueSolr).to(exchange); }
}
