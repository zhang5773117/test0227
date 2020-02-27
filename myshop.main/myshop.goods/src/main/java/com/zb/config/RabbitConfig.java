package com.zb.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String QUEUE_QG="qg_queue";
    public static final String EXCHANGE_TOPIC_INFORM="exchange_topic_qg";

    @Bean(EXCHANGE_TOPIC_INFORM)
    public Exchange createExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFORM).durable(true).build();
    }
    @Bean(QUEUE_QG)
    public Queue createQGQueue(){
        Queue queue =new Queue(QUEUE_QG);
        return queue;
    }

    @Bean
    public Binding bindingEmail(@Qualifier(EXCHANGE_TOPIC_INFORM) Exchange exchange ,@Qualifier(QUEUE_QG) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.qg.#").noargs();
    }

}