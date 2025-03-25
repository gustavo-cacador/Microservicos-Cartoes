package br.com.gustavo.mscartoes.application;

import br.com.gustavo.mscartoes.domain.Cartao;
import br.com.gustavo.mscartoes.dto.CartaoSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesController {

    private final CartaoService service;

    @GetMapping
    public String status() {
        return "ok";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request) {
        var cartao = request.toModel();
        service.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // http://localhost:PORT/cartoes?renda=5000
    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        List<Cartao> list = service.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }
}
