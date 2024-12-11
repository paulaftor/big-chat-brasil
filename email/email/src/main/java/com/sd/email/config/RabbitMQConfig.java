package com.sd.email.config;

import com.sd.email.listener.MensagemListener;
import com.sd.email.listener.UsuarioListener;
import com.sd.email.service.EmailService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private String exchangeMensagem = "mensagem-exchange";
    private String queueMensagem = "mensagem-queue";

    private String exchangeUsuario = "usuario-exchange";
    private String queueUsuario = "usuario-queue";


    /**
     * Especifica a exchange do tipo fanout para mensagens criadas
     * @return
     */
    @Bean
    public Exchange mensagensExchange() {
        return new FanoutExchange(exchangeMensagem); // Corrigido para usar exchangeMensagem
    }

    /**
     * Especifica a exchange do tipo fanout para usuários criados
     * @return
     */
    @Bean
    public Exchange usuarioExchange() {
        return new FanoutExchange(exchangeUsuario); // Adicionado para usuário
    }

    /**
     * Recebe a conexão através das propriedades presentes no application.properties
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    /**
     * Customizando o RabbitTemplate
     * @param connectionFactory
     * @param messageConverter
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter){
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * Configura o RabbitMQ imediatamente após a aplicação inicializar
     * @param rabbitAdmin
     * @return
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    // Cria a fila para mensagens
    @Bean
    public Queue mensagensQueue() {
        return new Queue(queueMensagem, true); // true significa que a fila será durável
    }

    // Cria a fila para usuários
    @Bean
    public Queue usuarioQueue() {
        return new Queue(queueUsuario, true); // true significa que a fila será durável
    }

    // Liga a fila de mensagens à exchange de mensagens
    @Bean
    public Binding bindingMensagens(Queue mensagensQueue, FanoutExchange mensagensExchange) {
        return BindingBuilder.bind(mensagensQueue).to(mensagensExchange);
    }

    // Liga a fila de usuários à exchange de usuários
    @Bean
    public Binding bindingUsuario(Queue usuarioQueue, FanoutExchange usuarioExchange) {
        return BindingBuilder.bind(usuarioQueue).to(usuarioExchange);
    }


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
