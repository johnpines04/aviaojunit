package com.mycompany.aviao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AviaoTest {
    private boolean[] assentosCheckinConfirmado;
    private boolean[] assentosDisponiveis;
    private boolean erroNoProcessoDeReserva;
    
    public void inicializarAssentos(int numeroAssentos) {
        assentosDisponiveis = new boolean[numeroAssentos];
        assentosCheckinConfirmado = new boolean[numeroAssentos];
        for (int i = 0; i < numeroAssentos; i++) {
            assentosDisponiveis[i] = true;
            assentosCheckinConfirmado[i] = false;
        }
        erroNoProcessoDeReserva = false;
    }
    
    public boolean reservarAssento(int numeroAssento) {
        if (numeroAssento <= 0 || numeroAssento > assentosDisponiveis.length) {
            return false;
        }else if (assentosDisponiveis[numeroAssento - 1]) {
            assentosDisponiveis[numeroAssento - 1] = false;
            return true;
        } else if (erroNoProcessoDeReserva) {
            return false;
        } else {
            return false;
        }
    }
    
    public boolean reservarPassagem(String origem, String destino, String data, int numeroPassageiros) {
        if (origem != null && destino != null && data != null && numeroPassageiros > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean cancelarReserva(int numeroAssento) {
        if (numeroAssento <= 0 || numeroAssento > assentosDisponiveis.length) {
            return false;
        }

        assentosDisponiveis[numeroAssento - 1] = true;
        return true;
    }

    public boolean verificarDisponibilidadeAssento(int numeroAssento) {
        if (numeroAssento <= 0 || numeroAssento > assentosDisponiveis.length) {
            return false;
        }

        return assentosDisponiveis[numeroAssento - 1];
    }
    
    public boolean realizarCheckin(int numeroAssento) {
        if (numeroAssento <= 0 || numeroAssento > assentosDisponiveis.length) {
            return false;
        }

        assentosCheckinConfirmado[numeroAssento - 1] = true;
        return true;
    }

    public boolean verificarCheckin(int numeroAssento) {
        if (numeroAssento <= 0 || numeroAssento > assentosDisponiveis.length) {
            return false;
        }

        return assentosCheckinConfirmado[numeroAssento - 1];
    }
    
    public void simularErroNoProcessoDeReserva(boolean simularErro) {
        this.erroNoProcessoDeReserva = simularErro;
    }
    
    @Test
    public void testReservaPassagem() {
        AviaoTest sistema = new AviaoTest();

        String origem = "Sao Paulo";
        String destino = "Rio de Janeiro";
        String data = "2023-12-31";
        int numeroPassageiros = 2;

        boolean reservaBemSucedida = sistema.reservarPassagem(origem, destino, data, numeroPassageiros);

        assertTrue(reservaBemSucedida, "A reserva de passagem não foi bem-sucedida");
    }
    
    @Test
    public void testReservaAssentosDisponiveis() {
        AviaoTest companhia = new AviaoTest();

        companhia.inicializarAssentos(10);

        boolean primeiraReserva = companhia.reservarAssento(1);
        assertTrue(primeiraReserva, "A primeira reserva deveria ser bem-sucedida");

        boolean segundaReserva = companhia.reservarAssento(1);
        assertFalse(segundaReserva, "A segunda reserva deveria falhar devido à sobreposição");

        boolean reservaAssentoInvalido = companhia.reservarAssento(15);
        assertFalse(reservaAssentoInvalido, "A reserva em um assento inexistente deveria falhar");
    }
    
    @Test
    public void testCancelamentoReserva() {
        AviaoTest companhia = new AviaoTest();

        companhia.inicializarAssentos(10);

        int numeroAssento = 3;
        boolean reservaBemSucedida = companhia.reservarAssento(numeroAssento);
        assertTrue(reservaBemSucedida, "A reserva deveria ser bem-sucedida");

        boolean cancelamentoBemSucedido = companhia.cancelarReserva(numeroAssento);
        assertTrue(cancelamentoBemSucedido, "O cancelamento da reserva deveria ser bem-sucedido");

        boolean assentoDisponivelAposCancelamento = companhia.verificarDisponibilidadeAssento(numeroAssento);
        assertTrue(assentoDisponivelAposCancelamento, "O assento deveria estar disponível após o cancelamento");
    }
    
    @Test
    public void testConfirmacaoCheckin() {
        AviaoTest companhia = new AviaoTest();

        companhia.inicializarAssentos(10);

        int numeroAssento = 3;
        boolean reservaBemSucedida = companhia.reservarAssento(numeroAssento);
        assertTrue(reservaBemSucedida, "A reserva deveria ser bem-sucedida");

        boolean checkinBemSucedido = companhia.realizarCheckin(numeroAssento);
        assertTrue(checkinBemSucedido, "O check-in deveria ser bem-sucedido");

        boolean checkinConfirmado = companhia.verificarCheckin(numeroAssento);
        assertTrue(checkinConfirmado, "O check-in deveria estar confirmado");
    }
    
    @Test
    public void testFalhaReservaAssentoInvalido() {
        AviaoTest companhia = new AviaoTest();

        companhia.inicializarAssentos(10);

        int assentoInvalido = 15;
        boolean reservaFalha = companhia.reservarAssento(assentoInvalido);

        assertFalse(reservaFalha, "A reserva deveria falhar devido a um assento inválido");
    }

    @Test
    public void testFalhaReservaPorErroNoProcesso() {
        AviaoTest companhia = new AviaoTest();

        companhia.inicializarAssentos(10);

        companhia.simularErroNoProcessoDeReserva(true);

        int assento = 3;
        boolean reservaFalha = companhia.reservarAssento(assento);

        assertFalse(reservaFalha, "A reserva deveria falhar devido a um erro no processo");
    }
}