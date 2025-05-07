package com.api.order.message.producer;

import com.api.order.dto.PedidoDto;
import com.api.order.message.consumer.KafkaConsumerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerMessage {

    @Autowired
    private KafkaTemplate<String, PedidoDto> kafkaTemplate;

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumerMessage.class);

    public void sendMessage(PedidoDto pedidoDto, String topicName) {
        LOG.info("Enviando mensagem para o tópico '{}' com conteúdo '{}'", topicName, pedidoDto);
        try {
            kafkaTemplate.send(topicName, pedidoDto)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            LOG.error("Erro ao enviar mensagem para o tópico '{}': {}", topicName, ex.getMessage(), ex);
                        } else {
                            LOG.info("Mensagem enviada com sucesso para o tópico '{}' na partição {} com offset {}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            LOG.error("Erro inesperado ao enviar mensagem: {}", e.getMessage(), e);
        }
    }

}
