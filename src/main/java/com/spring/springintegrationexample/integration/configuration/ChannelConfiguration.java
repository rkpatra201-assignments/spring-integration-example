package com.spring.springintegrationexample.integration.configuration;

import com.spring.springintegrationexample.integration.transformer.ItemMessageTransformer;
import com.spring.springintegrationexample.model.Item;
import com.spring.springintegrationexample.utils.ItemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.AggregatingMessageHandler;
import org.springframework.integration.aggregator.DefaultAggregatingMessageGroupProcessor;
import org.springframework.integration.aggregator.ExpressionEvaluatingCorrelationStrategy;
import org.springframework.integration.aggregator.TimeoutCountSequenceSizeReleaseStrategy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.expression.ValueExpression;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.splitter.MethodInvokingSplitter;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.SimpleMessageGroupFactory;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ChannelConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelConfiguration.class);

    /*
     define channels: these are the paths between source and destination for messages.
     source publishes message over a channel and unaware about who are the subscribers.
     A channel can have multiple subscribers.
     */


    /**
     * 1. this channel configured with a subscriber called as splittableOutputMessageHandler
     * 2. any message coming to this channel will go to splittableOutputMessageHandler
     * @return MessageChannel
     */
    @Bean(name="splittableMessageChannel")
    public MessageChannel splittableMessageChannel() {
        DirectChannel dc = new DirectChannel();
        dc.subscribe(splittableOutputMessageHandler());
        return dc;
    }

    @Bean(name = "messageAggregatorChannel")
    public MessageChannel messageAggregatorChannel() {
        DirectChannel dc = new DirectChannel();
        dc.subscribe(outputMessageAggregatorPolicy());
        return dc;
    }

    // define handlers
    @Autowired
    private ItemMessageTransformer itemMessageTransformer;

    /**
     * 1. read message from the channel: "request.message.channel"
     * 2. read the stream and extract the item list
     * 3. publish the item list to splittableMessageChannel:
     * @return
     */
    @Bean
    @Splitter(inputChannel = "request.message.channel")
    public AbstractMessageSplitter abstractMessageSplitter() {
        MethodInvokingSplitter splitter = new MethodInvokingSplitter(itemMessageTransformer, "prepareItemList");
        splitter.setOutputChannel(splittableMessageChannel());
        return splitter;
    }


    /**
     * 1. it receives message and if required it can apply the required logic on message.
     * 2. it again publish the message to aggregator channel
     * @return
     */
    @Bean(name = "splittableOutputMessageHandler")
    @ServiceActivator(inputChannel = "response.message.splitter.channel")
    public MessageHandler splittableOutputMessageHandler() {
        ServiceActivatingHandler serviceActivatingHandler = new ServiceActivatingHandler(
                new MessageProcessor<Object>() {
                    @Override
                    public Object processMessage(Message<?> message) {
                        Item item = (Item)message.getPayload();
                        // attach key: because it was a raw message. this key will help in grouping
                        item.setKey(ItemUtils.getItemKey(item));
                        LOGGER.info("message Payload= " + message.getPayload());
                        return message;
                    }
                });
        serviceActivatingHandler.setOutputChannel(messageAggregatorChannel());
        return serviceActivatingHandler;
    }


    @Bean(name = "outputMessageAggregatorPolicy")
    @ServiceActivator(inputChannel = "request.message.aggregator.channel")
    public MessageHandler outputMessageAggregatorPolicy() {
        AggregatingMessageHandler messageHandler = new AggregatingMessageHandler(new DefaultAggregatingMessageGroupProcessor(), messageGroupStore());
        messageHandler.setReleaseStrategy(new TimeoutCountSequenceSizeReleaseStrategy(60, 3000));
        messageHandler.setOutputChannelName("response.message.aggregator.channel");
        messageHandler.setGroupTimeoutExpression(new ValueExpression<>(5000L));
        messageHandler.setCorrelationStrategy(new ExpressionEvaluatingCorrelationStrategy("payload.key"));
        return messageHandler;
    }

    @Bean
    public MessageGroupStore messageGroupStore() {
        SimpleMessageStore simpleMessageStore = new SimpleMessageStore();
        simpleMessageStore.setMessageGroupFactory(new SimpleMessageGroupFactory());
        return simpleMessageStore;
    }

    @Bean
    @ServiceActivator(inputChannel = "response.message.aggregator.channel")
    public MessageHandler aggregateableOutputMessageHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                // todo: writer
                List payLoadList = ((ArrayList) message.getPayload());
                LOGGER.info("aggregate Payload size: {}, Payload: {}",  payLoadList.size(), payLoadList);
            }
        };
    }
}
