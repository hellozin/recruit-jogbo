package univ.study.recruitjogbo.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import univ.study.recruitjogbo.message.RabbitMQ;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue confirmMailQueue() {
        return new Queue(RabbitMQ.CONFIRM_EMAIL_REQUEST, false);
    }

    @Bean
    Queue postCreateQueue() {
        return new Queue(RabbitMQ.POST_CREATE, false);
    }

    @Bean
    Queue postUpdateQueue() {
        return new Queue(RabbitMQ.POST_UPDATE, false);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitMQ.EXCHANGE);
    }

    @Bean
    List<Binding> bindings() {
        return Arrays.asList(
                BindingBuilder.bind(confirmMailQueue()).to(directExchange()).with(confirmMailQueue().getName()),
                BindingBuilder.bind(postCreateQueue()).to(directExchange()).with(postCreateQueue().getName()),
                BindingBuilder.bind(postUpdateQueue()).to(directExchange()).with(postUpdateQueue().getName()));
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
