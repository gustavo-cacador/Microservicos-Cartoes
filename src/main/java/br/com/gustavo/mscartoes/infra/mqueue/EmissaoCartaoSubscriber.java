package br.com.gustavo.mscartoes.infra.mqueue;

import br.com.gustavo.mscartoes.domain.Cartao;
import br.com.gustavo.mscartoes.domain.ClienteCartao;
import br.com.gustavo.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import br.com.gustavo.mscartoes.infra.repository.CartaoRepository;
import br.com.gustavo.mscartoes.infra.repository.ClienteCartaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queue.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            var mapper = new ObjectMapper();

            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();
            ClienteCartao clienteCartao = new ClienteCartao();

            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());

            clienteCartaoRepository.save(clienteCartao);

        } catch (Exception e) {
            log.error("Erro ao receber solicitacao de emissao de cartao: {} ", e.getMessage());
        }
    }
}
