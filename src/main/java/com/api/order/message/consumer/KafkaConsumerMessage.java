package com.api.order.message.consumer;

import com.api.order.dto.PedidoDto;
import com.api.order.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerMessage {

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumerMessage.class);

    @Autowired
    private PedidoService pedidoService;

    @KafkaListener(topics = "pedido", groupId = "store-posts-group")
    public void listening(PedidoDto pedidoDto) {
        LOG.info("Mensagem recebida do tópico 'pedido': {}", pedidoDto);
        try {
            if (pedidoDto == null) {
                LOG.warn("Mensagem recebida está nula. Ignorando processamento.");
                return;
            }
            LOG.info("Iniciando criação do pedido...");
            PedidoDto pedidoSalvo = pedidoService.criarPedido(pedidoDto);
            LOG.info("Pedido criado com sucesso: {}", pedidoSalvo);
        } catch (IllegalArgumentException e) {
            LOG.warn("Dados inválidos recebidos: {}. Erro: {}", pedidoDto, e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Erro ao processar mensagem do tópico 'pedido': {}", e.getMessage(), e);
        }
    }

}
